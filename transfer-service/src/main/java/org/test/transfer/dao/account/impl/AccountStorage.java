package org.test.transfer.dao.account.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.transfer.TransferDetails;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Application data storage and operations implementation.
 */
@Component
public class AccountStorage implements AccountOperations {

    private static final Logger logger = LoggerFactory.getLogger(AccountStorage.class);

    /**
     * Adding new account and get account info operations rely on ConcurrentHashMap implementation,
     * but atomic transfer requires additional locking
     */
    private final Map<Long, AccountEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    private final Object lock = new Object();

    /**
     * Puts data to concurrent map
     *
     * @param name    account name
     * @param balance account initial balance
     * @return account details
     */
    @Override
    public AccountDetails store(String name, BigDecimal balance) {
        AccountEntity accountEntity = new AccountEntity(newId(), name, balance);
        storage.putIfAbsent(accountEntity.id, accountEntity);
        return new AccountDetails(accountEntity.id, accountEntity.balance);
    }

    /**
     * Checks for account presence and balance then atomically replace accounts with new balances.
     * Transfer to the same account is forbidden
     *
     * @param fromId payer account id
     * @param toId   payee account id
     * @param amount amount
     * @return transfer details
     */
    @Override
    public TransferDetails transferAtomically(Long fromId, Long toId, BigDecimal amount) {
        if (Objects.equals(fromId, toId)) {
            throw new IllegalArgumentException("Accounts should be different");
        }

        if (!storage.containsKey(fromId)) {
            throw new IllegalArgumentException("Payer account not found");
        }

        if (!storage.containsKey(toId)) {
            throw new IllegalArgumentException("Payee account not found");
        }

        synchronized (lock) {
            AccountEntity fromAcc = storage.get(fromId);
            AccountEntity toAcc = storage.get(toId);
            logger.info("Transfer: thread={}, from={}, to={}, amount={}",
                    Thread.currentThread().getName(), fromAcc, toAcc, amount);

            if (fromAcc.balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            storage.replace(fromId, fromAcc.withdraw(amount));
            storage.replace(toId, toAcc.deposit(amount));

            return new TransferDetails.Builder()
                    .setFromAccountDetails(new AccountDetails(fromId, fromAcc.balance))
                    .setToAccountDetails(new AccountDetails(toId, toAcc.balance))
                    .build();
        }
    }

    @Override
    public Optional<AccountDetails> getAccount(Long id) {
        return Optional.ofNullable(storage.get(id))
                .map(e -> new AccountDetails(e.id, e.balance));
    }

    private Long newId() {
        return idGenerator.incrementAndGet();
    }

    /**
     * Support class representing data layer entities.
     */
    private static class AccountEntity {

        @Nonnull
        private final Long id;
        @Nonnull
        private final String name;
        @Nonnull
        private final BigDecimal balance;

        AccountEntity(@Nonnull Long id, @Nonnull String name, @Nonnull BigDecimal balance) {
            this.id = id;
            this.name = name;
            this.balance = balance;
        }

        AccountEntity withdraw(BigDecimal amount) {
            return new AccountEntity(id, name, balance.subtract(amount));
        }

        AccountEntity deposit(BigDecimal amount) {
            return new AccountEntity(id, name, balance.add(amount));
        }

        @Override
        public String toString() {
            return "Account: id=" + id + ", balance=" + balance;
        }

    }

}

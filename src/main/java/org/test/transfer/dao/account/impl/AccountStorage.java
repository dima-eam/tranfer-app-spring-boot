package org.test.transfer.dao.account.impl;

import org.springframework.stereotype.Component;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.transfer.TransferDetails;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Application data storage and operations implementation.
 */
@Component
public class AccountStorage implements AccountOperations {

    private final ConcurrentMap<Long, AccountEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

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
        storage.put(accountEntity.id, accountEntity);
        return new AccountDetails(accountEntity.id, accountEntity.balance);
    }

    /**
     * Checks for account presence and balance then atomically replace accounts with new balances
     *
     * @param fromId payer account id
     * @param toId   payee account id
     * @param amount amount
     * @return transfer details
     */
    @Override
    public TransferDetails transferAtomically(Long fromId, Long toId, BigDecimal amount) {
        AccountEntity fromAcc = storage.get(fromId);
        if (fromAcc == null) {
            throw new IllegalArgumentException("Payer account not found");
        }

        AccountEntity toAcc = storage.get(toId);
        if (toAcc == null) {
            throw new IllegalArgumentException("Payee account not found");
        }

        if (fromAcc.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Object lock1 = fromId.compareTo(toId) < 0 ? fromAcc : toAcc;
        Object lock2 = fromId.compareTo(toId) < 0 ? toAcc : fromAcc;
        synchronized (lock1) {
            synchronized (lock2) {
                storage.replace(fromId, fromAcc.withdraw(amount));
                storage.replace(toId, toAcc.deposit(amount));
            }
        }

        return new TransferDetails.Builder()
                .setFromAccountDetails(new AccountDetails(fromId, fromAcc.balance))
                .setToAccountDetails(new AccountDetails(toId, toAcc.balance))
                .build();
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

        public AccountEntity withdraw(BigDecimal amount) {
            return new AccountEntity(id, name, balance.subtract(amount));
        }

        public AccountEntity deposit(BigDecimal amount) {
            return new AccountEntity(id, name, balance.add(amount));
        }
    }
}

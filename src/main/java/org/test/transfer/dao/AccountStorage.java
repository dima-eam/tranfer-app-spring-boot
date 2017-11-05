package org.test.transfer.dao;

import org.springframework.stereotype.Component;
import org.test.transfer.model.AccountDetails;
import org.test.transfer.model.TransferDetails;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AccountStorage implements AccountOperations {

    private final ConcurrentMap<Long, AccountEntity> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public AccountDetails store(String name, BigDecimal balance) {
        AccountEntity accountEntity = new AccountEntity(newId(), name, balance);
        storage.put(accountEntity.id, accountEntity);
        return new AccountDetails(accountEntity.id, accountEntity.balance);
    }

    @Override
    public TransferDetails transferAtomically(Long from, Long to, BigDecimal amount) {
        AccountEntity fromAcc = storage.get(from);
        if (fromAcc == null) {
            throw new IllegalArgumentException("Payer account not found");
        }
        AccountEntity toAcc = storage.get(to);
        if (toAcc == null) {
            throw new IllegalArgumentException("Payee account not found");
        }
        if (fromAcc.balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        Object lock1 = from.compareTo(to) < 0 ? fromAcc : toAcc;
        Object lock2 = from.compareTo(to) < 0 ? toAcc : fromAcc;
        synchronized (lock1) {
            synchronized (lock2) {
                storage.replace(from, fromAcc.subtract(amount));
                storage.replace(to, toAcc.add(amount));
            }
        }

        return new TransferDetails.Builder()
                .setFromAccountDetails(new AccountDetails(from, fromAcc.balance))
                .setToAccountDetails(new AccountDetails(to, toAcc.balance))
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

        public AccountEntity subtract(BigDecimal amount) {
            return new AccountEntity(id, name, balance.subtract(amount));
        }

        public AccountEntity add(BigDecimal amount) {
            return new AccountEntity(id, name, balance.add(amount));
        }
    }
}

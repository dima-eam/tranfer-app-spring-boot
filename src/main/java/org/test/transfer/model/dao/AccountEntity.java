package org.test.transfer.model.dao;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

public class AccountEntity {

    @Nonnull
    private final Long id;
    @Nonnull
    private final String name;
    @Nonnull
    private BigDecimal balance;

    public AccountEntity(@Nonnull Long id, @Nonnull String name, @Nonnull BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    @Nonnull
    public Long getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(@Nonnull BigDecimal balance) {
        this.balance = balance;
    }
}

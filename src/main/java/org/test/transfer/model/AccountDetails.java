package org.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

public class AccountDetails {

    @Nonnull
    private final Long id;
    @Nonnull
    private final BigDecimal balance;

    public AccountDetails(@Nonnull Long id, @Nonnull BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "id");
        this.balance = balance;
    }

    @Nonnull
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @Nonnull
    @JsonProperty("balance")
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}

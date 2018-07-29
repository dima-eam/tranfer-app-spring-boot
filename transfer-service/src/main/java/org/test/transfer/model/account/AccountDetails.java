package org.test.transfer.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable account details entity with JSON serialization support.
 */
public class AccountDetails {

    /**
     * Account id
     */
    @Nonnull
    private final Long id;
    /**
     * Account balance
     */
    @Nonnull
    private final BigDecimal balance;

    public AccountDetails(@Nonnull Long id, @Nonnull BigDecimal balance) {
        this.id = Objects.requireNonNull(id, "id");
        this.balance = Objects.requireNonNull(balance, "balance");
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

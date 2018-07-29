package org.test.transfer.model.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.test.transfer.model.account.AccountDetails;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Immutable transfer details entity with JSON serialization support.
 */
public class TransferDetails {

    /**
     * Details of payer account
     */
    @Nonnull
    private final AccountDetails fromAccountDetails;
    /**
     * Details of payee account
     */
    @Nonnull
    private final AccountDetails toAccountDetails;

    private TransferDetails(@Nonnull AccountDetails fromAccountDetails, @Nonnull AccountDetails toAccountDetails) {
        this.fromAccountDetails = Objects.requireNonNull(fromAccountDetails, "fromAccountDetails");
        this.toAccountDetails = Objects.requireNonNull(toAccountDetails, "toAccountDetails");
    }

    @Nonnull
    @JsonProperty("fromAccountDetails")
    public AccountDetails getFromAccountDetails() {
        return fromAccountDetails;
    }

    @Nonnull
    @JsonProperty("toAccountDetails")
    public AccountDetails getToAccountDetails() {
        return toAccountDetails;
    }

    public static class Builder {

        private AccountDetails fromAccountDetails;
        private AccountDetails toAccountDetails;

        public Builder setFromAccountDetails(AccountDetails fromAccountDetails) {
            this.fromAccountDetails = fromAccountDetails;
            return this;
        }

        public Builder setToAccountDetails(AccountDetails toAccountDetails) {
            this.toAccountDetails = toAccountDetails;
            return this;
        }

        public TransferDetails build() {
            return new TransferDetails(fromAccountDetails, toAccountDetails);
        }
    }
}

package org.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferDetails {

    private final AccountDetails fromAccountDetails;
    private final AccountDetails toAccountDetails;

    private TransferDetails(AccountDetails fromAccountDetails, AccountDetails toAccountDetails) {
        this.fromAccountDetails = fromAccountDetails;
        this.toAccountDetails = toAccountDetails;
    }

    @JsonProperty("fromAccountDetails")
    public AccountDetails getFromAccountDetails() {
        return fromAccountDetails;
    }

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

package org.test.transfer.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@JsonDeserialize(builder = TransferRequest.Builder.class)
public class TransferRequest {

    @NotNull
    private final Long from;
    @NotNull
    private final Long to;
    @Min(0)
    private final BigDecimal amount;

    private TransferRequest(Long from, Long to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "from=" + from +
                ", to=" + to +
                ", amount=" + amount +
                '}';
    }

    public static class Builder {
        private Long from;
        private Long to;
        private BigDecimal amount;

        public Builder withFrom(Long from) {
            this.from = from;
            return this;
        }

        public Builder withTo(Long to) {
            this.to = to;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransferRequest build() {
            return new TransferRequest(from, to, amount);
        }
    }
}
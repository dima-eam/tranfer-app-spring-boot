package org.test.transfer.model.transfer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Immutable transfer request request entity with
 * JSON serialization/deserialization support
 * and request validation support.
 */
@ApiModel(description = "Immutable transfer request request entity")
@JsonDeserialize(builder = TransferRequest.Builder.class)
public class TransferRequest {

    /**
     * Payer account id
     */
    @ApiModelProperty(required = true, example = "1")
    @NotNull(message = "Payer account id cannot be null")
    private final Long fromId;
    /**
     * Payee account id
     */
    @NotNull(message = "Payee account id cannot be null")
    private final Long toId;
    /**
     * Transfer amount
     */
    @Min(0)
    private final BigDecimal amount;

    private TransferRequest(Long fromId, Long toId, BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public Long getFromId() {
        return fromId;
    }

    public Long getToId() {
        return toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", amount=" + amount +
                '}';
    }

    /**
     * Transfer request builder.
     */
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
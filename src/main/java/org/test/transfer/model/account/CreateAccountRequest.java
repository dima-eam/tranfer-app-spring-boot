package org.test.transfer.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * Immutable create account request entity with
 * JSON serialization/deserialization support
 * and request validation support.
 */
public class CreateAccountRequest {

    @NotBlank(message = "Name cannot be blank")
    private final String name;
    @Min(value = 0, message = "Balance cannot be negative")
    private final BigDecimal balance;

    @JsonCreator
    public CreateAccountRequest(@JsonProperty("name") String name,
                                @JsonProperty("balance") BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("balance")
    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }
}
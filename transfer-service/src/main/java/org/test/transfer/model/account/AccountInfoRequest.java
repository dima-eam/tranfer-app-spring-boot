package org.test.transfer.model.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Immutable account info request entity with
 * JSON serialization/deserialization support
 * and request validation support.
 */
public class AccountInfoRequest {

    /**
     * account id
     */
    @NotNull(message = "Account id cannot be null")
    private final Long id;

    @JsonCreator
    public AccountInfoRequest(@JsonProperty("id") Long id) {
        this.id = id;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AccountInfoRequest{" +
                "id=" + id +
                '}';
    }
}
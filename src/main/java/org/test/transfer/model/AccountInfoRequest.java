package org.test.transfer.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class AccountInfoRequest {

    @NotNull
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
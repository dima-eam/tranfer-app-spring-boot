package org.test.transfer.service;

import org.springframework.stereotype.Component;
import org.test.transfer.dao.AccountOperations;
import org.test.transfer.dao.AccountStorage;
import org.test.transfer.model.AccountDetails;
import org.test.transfer.model.AccountInfoRequest;
import org.test.transfer.model.CreateAccountRequest;

import java.util.Optional;

@Component
public class AccountService {

    private final AccountOperations accountOperations;

    public AccountService(AccountStorage accountOperations) {
        this.accountOperations = accountOperations;
    }

    public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {
        return accountOperations.store(createAccountRequest.getName(), createAccountRequest.getBalance());
    }

    public Optional<AccountDetails> getAccountInfo(AccountInfoRequest accountInfoRequest) {
        return accountOperations.getAccount(accountInfoRequest.getId());
    }
}
package org.test.transfer.service.account.impl;

import org.springframework.stereotype.Component;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.dao.account.impl.AccountStorage;
import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.account.AccountInfoRequest;
import org.test.transfer.model.account.CreateAccountRequest;
import org.test.transfer.service.account.AccountService;

import java.util.Optional;

/**
 * Account management service using some account storage.
 */
@Component
public class AccountServiceImpl implements AccountService {

    private final AccountOperations accountOperations;

    public AccountServiceImpl(AccountOperations accountOperations) {
        this.accountOperations = accountOperations;
    }

    @Override
    public AccountDetails createAccount(CreateAccountRequest createAccountRequest) {
        return accountOperations.store(createAccountRequest.getName(), createAccountRequest.getBalance());
    }

    @Override
    public Optional<AccountDetails> getAccountInfo(AccountInfoRequest accountInfoRequest) {
        return accountOperations.getAccount(accountInfoRequest.getId());
    }
}
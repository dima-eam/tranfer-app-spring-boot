package org.test.transfer.service.account;

import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.account.AccountInfoRequest;
import org.test.transfer.model.account.CreateAccountRequest;

import java.util.Optional;

/**
 * Account management service.
 */
public interface AccountService {

    /**
     * Create account from request.
     *
     * @param createAccountRequest request
     * @return creation details
     */
    AccountDetails createAccount(CreateAccountRequest createAccountRequest);

    /**
     * Retrieves account info.
     *
     * @param accountInfoRequest request
     * @return optional with possible value
     */
    Optional<AccountDetails> getAccountInfo(AccountInfoRequest accountInfoRequest);
}

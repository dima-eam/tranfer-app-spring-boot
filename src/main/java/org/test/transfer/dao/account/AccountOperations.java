package org.test.transfer.dao.account;

import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.transfer.TransferDetails;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Possible application operations.
 */
public interface AccountOperations {

    /**
     * Store account data
     *
     * @param name    account name
     * @param balance account initial balance
     * @return account details
     */
    AccountDetails store(String name, BigDecimal balance);

    /**
     * Transfer amount from one account to another
     *
     * @param fromId payer account id
     * @param toId   payee account id
     * @param amount amount
     * @return transfer details
     */
    TransferDetails transferAtomically(Long fromId, Long toId, BigDecimal amount);

    /**
     * Get account by id, or return empty optional
     *
     * @param id account id
     * @return optional with account details or empty optional, if not found
     */
    Optional<AccountDetails> getAccount(Long id);
}

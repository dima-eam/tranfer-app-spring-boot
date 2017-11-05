package org.test.transfer.dao;

import org.test.transfer.model.AccountDetails;
import org.test.transfer.model.TransferDetails;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountOperations {

    AccountDetails store(String name, BigDecimal balance);

    TransferDetails transferAtomically(Long from, Long to, BigDecimal amount);

    Optional<AccountDetails> getAccount(Long id);
}

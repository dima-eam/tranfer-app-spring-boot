package org.test.transfer.service.transfer.impl;

import org.springframework.stereotype.Component;
import org.test.transfer.dao.account.AccountOperations;
import org.test.transfer.dao.account.impl.AccountStorage;
import org.test.transfer.model.transfer.TransferDetails;
import org.test.transfer.model.transfer.TransferRequest;
import org.test.transfer.service.transfer.TransferService;

/**
 * Funds transfer service using some accounts storage.
 */
@Component
public class TransferServiceImpl implements TransferService {

    private final AccountOperations accountOperations;

    public TransferServiceImpl(AccountOperations accountOperations) {
        this.accountOperations = accountOperations;
    }

    @Override
    public TransferDetails transfer(TransferRequest transferRequest) {
        return accountOperations.transferAtomically(transferRequest.getFromId(),
                transferRequest.getToId(), transferRequest.getAmount());
    }

}

package org.test.transfer.service;

import org.springframework.stereotype.Component;
import org.test.transfer.dao.AccountOperations;
import org.test.transfer.dao.AccountStorage;
import org.test.transfer.model.TransferDetails;
import org.test.transfer.model.TransferRequest;

@Component
public class TransferService {

    private final AccountOperations accountOperations;

    public TransferService(AccountStorage accountOperations) {
        this.accountOperations = accountOperations;
    }

    public TransferDetails transfer(TransferRequest transferRequest) {
        return accountOperations.transferAtomically(transferRequest.getFrom(),
                transferRequest.getTo(), transferRequest.getAmount());
    }

}

package org.test.transfer.service.transfer;

import org.test.transfer.model.transfer.TransferDetails;
import org.test.transfer.model.transfer.TransferRequest;

/**
 * Account transfer service.
 */
public interface TransferService {

    /**
     * Transfer funds from one account to another
     *
     * @param transferRequest request
     * @return transfer details
     */
    TransferDetails transfer(TransferRequest transferRequest);
}

package org.test.transfer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.test.transfer.model.account.AccountDetails;
import org.test.transfer.model.transfer.TransferDetails;
import org.test.transfer.model.transfer.TransferRequest;
import org.test.transfer.service.transfer.impl.TransferServiceImpl;

import javax.annotation.Nullable;
import javax.validation.Valid;

import static org.test.transfer.controller.ErrorsHelper.validate;

/**
 * Transfer operations controller. Returns {@link ResponseEntity} with JSON payload.
 */
@RestController
@RequestMapping("/app/api/transfer")
public class TransferController {

    @Autowired
    private TransferServiceImpl transferService;

    /**
     * Validates request and process transfer.
     *
     * @param transferRequest request
     * @param errors               validation errors collector
     * @return result wrapped in ResponseEntity
     */
    @PostMapping("/payment")
    public ResponseEntity<TransferResult> transfer(@RequestBody @Valid TransferRequest transferRequest,
                                                   Errors errors) {
        return validate(errors)
                .map(m -> ResponseEntity.badRequest().body(TransferResult.withErrorMessage(m)))
                .orElseGet(() -> {
                    try {
                        TransferDetails transferDetails = transferService.transfer(transferRequest);
                        return ResponseEntity.ok(TransferResult.withDetails(transferDetails));
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body(TransferResult.withErrorMessage(e.getMessage()));
                    }
                });
    }

    private static class TransferResult {

        @Nullable
        private final String errorMessage;
        @Nullable
        private final AccountDetails fromAccountDetails;
        @Nullable
        private final AccountDetails toAccountDetails;

        private TransferResult(@Nullable String errorMessage,
                               @Nullable AccountDetails fromAccountDetails,
                               @Nullable AccountDetails toAccountDetails) {
            this.errorMessage = errorMessage;
            this.fromAccountDetails = fromAccountDetails;
            this.toAccountDetails = toAccountDetails;
        }

        @Nullable
        @JsonProperty("errorMessage")
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        @JsonProperty("fromAccountDetails")
        public AccountDetails getFromAccountDetails() {
            return fromAccountDetails;
        }

        @Nullable
        @JsonProperty("toAccountDetails")
        public AccountDetails getToAccountDetails() {
            return toAccountDetails;
        }

        static TransferResult withErrorMessage(String errorMessage) {
            return new TransferResult(errorMessage, null, null);
        }

        static TransferResult withDetails(TransferDetails transferDetails) {
            return new TransferResult(null, transferDetails.getFromAccountDetails(),
                    transferDetails.getToAccountDetails());
        }
    }
}

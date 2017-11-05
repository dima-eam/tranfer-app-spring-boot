package org.test.transfer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.test.transfer.model.AccountDetails;
import org.test.transfer.model.AccountInfoRequest;
import org.test.transfer.model.CreateAccountRequest;
import org.test.transfer.service.AccountService;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.function.Supplier;

import static org.test.transfer.controller.ErrorsHelper.validate;

/**
 * Account management controller. Returns {@link ResponseEntity} with JSON payload.
 */
@RestController
@RequestMapping("/app/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Validates request and creates new account if possible.
     *
     * @param createAccountRequest request
     * @param errors               validation errors collector
     * @return result wrapped in ResponseEntity
     */
    @PostMapping("/create")
    public ResponseEntity<AccountResult> createAccount(@RequestBody @Valid CreateAccountRequest createAccountRequest,
                                                       Errors errors) {
        return validateAndProcess(errors, () -> {
            AccountDetails accountDetails = accountService.createAccount(createAccountRequest);
            return ResponseEntity.ok(AccountResult.withDetails(accountDetails));
        });
    }

    /**
     * Validates request and return account info if possible.
     *
     * @param accountInfoRequest request
     * @param errors             validation errors collector
     * @return result wrapped in ResponseEntity
     */
    @GetMapping("/info")
    public ResponseEntity<AccountResult> getAccountInfo(@RequestBody @Valid AccountInfoRequest accountInfoRequest,
                                                        Errors errors) {
        return validateAndProcess(errors, () ->
                accountService.getAccountInfo(accountInfoRequest)
                        .map(ad -> ResponseEntity.ok(AccountResult.withDetails(ad)))
                        .orElseGet(() -> ResponseEntity.badRequest()
                                .body(AccountResult.withErrorMessage("Account not found")))
        );
    }

    private ResponseEntity<AccountResult> validateAndProcess(Errors errors,
                                                             Supplier<ResponseEntity<AccountResult>> process) {
        return validate(errors)
                .map(m -> ResponseEntity.badRequest().body(AccountResult.withErrorMessage(m)))
                .orElseGet(process);
    }

    /**
     * Support class for JSON serialization.
     */
    private static class AccountResult {

        /**
         * Possible validation error message.
         */
        @Nullable
        private final String errorMessage;
        /**
         * Account details if success created.
         */
        @Nullable
        private final AccountDetails accountDetails;

        private AccountResult(@Nullable String errorMessage, @Nullable AccountDetails accountDetails) {
            this.errorMessage = errorMessage;
            this.accountDetails = accountDetails;
        }

        @Nullable
        @JsonProperty("errorMessage")
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        @JsonProperty("accountDetails")
        public AccountDetails getAccountDetails() {
            return accountDetails;
        }

        static AccountResult withErrorMessage(String errorMessage) {
            return new AccountResult(errorMessage, null);
        }

        static AccountResult withDetails(AccountDetails accountDetails) {
            return new AccountResult(null, accountDetails);
        }
    }
}

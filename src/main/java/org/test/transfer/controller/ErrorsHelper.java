package org.test.transfer.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Error handling helper
 */
class ErrorsHelper {

    /**
     * Checks if there was validation errors and returns optional error message
     *
     * @param errors errors collector
     * @return optional with error message or empty optional
     */
    static Optional<String> validate(Errors errors) {
        if (errors.hasErrors()) {
            return Optional.of(errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));
        }

        return Optional.empty();
    }

}

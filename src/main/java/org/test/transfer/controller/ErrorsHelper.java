package org.test.transfer.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ErrorsHelper {

    static Optional<String> validate(Errors errors) {
        if (errors.hasErrors()) {
            return Optional.of(errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));
        }

        return Optional.empty();
    }
//
//    static <T> T wrapAndHandleErrors(Supplier<T> supplier) {
//        try {
//            return ResponseEntity.ok(supplier.get());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().
//        }
//    }
}

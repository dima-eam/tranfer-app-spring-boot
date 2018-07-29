package org.test.transfer.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler. Forms pretty JSON responses.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return buildErrorResponse(ex, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        return buildErrorResponse(ex, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception ex, WebRequest request) {
        String message = truncate(ex.getMessage());
        return handleExceptionInternal(ex, Error.fromString(message),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Drop exception details to not expose internal logic (naive approach)
     *
     * @param message exception message
     * @return truncated message
     */
    private static String truncate(String message) {
        int end = message.indexOf(':');
        end = end == -1 ? message.length() : end;
        return message.substring(0, end);
    }

    private static class Error {

        @JsonProperty("shortMessage")
        private String shortMessage;

        private Error(String shortMessage) {
            this.shortMessage = shortMessage;
        }

        static Error fromString(String message) {
            return new Error(message);
        }
    }

}
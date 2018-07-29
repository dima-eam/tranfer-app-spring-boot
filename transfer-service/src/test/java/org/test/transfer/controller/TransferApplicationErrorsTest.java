package org.test.transfer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class TransferApplicationErrorsTest extends AbstractTransferTest {

    @Test
    public void shouldFailCreateAccountWhenNegativeBalance() throws Exception {
        String request = "{\"name\":\"test1\",\"balance\":\"-10\"}";
        MockHttpServletResponse response = createAccount(request);

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

        String expected = "{\"errorMessage\":\"Balance cannot be negative\",\"accountDetails\":null}";
        assertEquals(response.getContentAsString(), expected);
    }

    @Test
    public void shouldFailGetAccountWhenNotExists() throws Exception {
        String request = "{\"id\":\"1\"}";
        MockHttpServletResponse response = getAccountInfo(request);

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

        String expected = "{\"errorMessage\":\"Account not found\",\"accountDetails\":null}";
        assertEquals(response.getContentAsString(), expected);
    }

    @Test
    public void shouldFailGetAccountWhenNoBody() throws Exception {
        MockHttpServletResponse response = getAccountInfo(null);

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
        String expected = "{\"shortMessage\":\"Required request body is missing\"}";
        assertEquals(response.getContentAsString(), expected);
    }

    @Test
    public void shouldFailTransferWhenAccountNotFound() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"2\",\"amount\":\"10.00\"}";
        MockHttpServletResponse response = transfer(request);

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

        String expected = "{\"errorMessage\":\"Payer account not found\",\"fromAccountDetails\":null,\"toAccountDetails\":null}";
        assertEquals(response.getContentAsString(), expected);
    }

    @Test
    public void shouldFailTransferWhenWrongHttpMethodUsed() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"2\",\"amount\":\"10.00\"}";

        MockHttpServletResponse response = transfer(request, getAuthorizedBuilder(TRANSFER_PAYMENT_URI));

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

        String expected = "{\"shortMessage\":\"Request method 'GET' not supported\"}";
        assertEquals(response.getContentAsString(), expected);
    }

    @Test
    public void shouldFailTransferWhenEqualAccounts() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"1\",\"amount\":\"10.00\"}";

        MockHttpServletResponse response = transfer(request);

        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());

        String expected = "{\"errorMessage\":\"Accounts should be different\",\"fromAccountDetails\":null,\"toAccountDetails\":null}";
        assertEquals(response.getContentAsString(), expected);
    }

//    @Test
    public void shouldFailTransferWhenWrongUser() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"1\",\"amount\":\"10.00\"}";

        MockHttpServletResponse response = transfer(request, "another");

        assertEquals(response.getStatus(), HttpStatus.UNAUTHORIZED.value());

        String expected = "{\"errorMessage\":\"Accounts should be different\",\"fromAccountDetails\":null,\"toAccountDetails\":null}";
        assertEquals(response.getContentAsString(), expected);
    }

}

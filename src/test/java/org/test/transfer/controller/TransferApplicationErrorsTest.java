package org.test.transfer.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferApplicationErrorsTest extends AbstractTransferTest {

    @Test
    public void shouldFailCreateAccountWhenNegativeBalance() throws Exception {
        String request = "{\"name\":\"test1\",\"balance\":\"-10\"}";
        MockHttpServletResponse response = createAccount(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String expected = "{\"errorMessage\":\"Balance cannot be negative\",\"accountDetails\":null}";
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    public void shouldFailGetAccountWhenNotExists() throws Exception {
        String request = "{\"id\":\"1\"}";
        MockHttpServletResponse response = getAccountInfo(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String expected = "{\"errorMessage\":\"Account not found\",\"accountDetails\":null}";
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    public void shouldFailTransferWhenAccountNotFound() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"2\",\"amount\":\"10.00\"}";
        MockHttpServletResponse response = transfer(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String expected = "{\"errorMessage\":\"Payer account not found\",\"fromAccountDetails\":null,\"toAccountDetails\":null}";
        assertEquals(expected, response.getContentAsString());
    }

}

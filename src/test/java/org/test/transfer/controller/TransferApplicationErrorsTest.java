package org.test.transfer.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferApplicationErrorsTest {

    private static final String CREATE_ACCOUNT_URI = "/app/api/account/create/";
    private static final String TRANSFER_PAYMENT_URI = "/app/api/transfer/payment";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldFailCreateAccountWhenNegativeBalance() throws Exception {
        String request = "{\"name\":\"test1\",\"balance\":\"-10\"}";
        MockHttpServletResponse response = makeRequest(request, CREATE_ACCOUNT_URI);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String expected = "{\"errorMessage\":\"Balance cannot be negative\",\"accountDetails\":null}";
        assertEquals(expected, response.getContentAsString());
    }

    @Test
    public void shouldFailTransferWhenAccountNotFound() throws Exception {
        String request = "{\"from\":\"1\",\"to\":\"2\",\"amount\":\"10.00\"}";
        MockHttpServletResponse response = makeRequest(request, TRANSFER_PAYMENT_URI);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        String expected = "{\"errorMessage\":\"Payer account not found\",\"fromAccountDetails\":null,\"toAccountDetails\":null}";
        assertEquals(expected, response.getContentAsString());
    }

    private MockHttpServletResponse makeRequest(String request, String uri) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        return result.getResponse();
    }

}

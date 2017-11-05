package org.test.transfer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferApplicationTestSuite {

    private static final String CREATE_ACCOUNT_URI = "/app/api/account/create/";
    private static final String ACCOUNT_INFO_URI = "/app/api/account/info/";
    private static final String TRANSFER_PAYMENT_URI = "/app/api/transfer/payment";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSuite() throws Exception {
        Long from = shouldSuccessCreateAccount("test1", new BigDecimal("100.00"));
        Long to = shouldSuccessCreateAccount("test2", new BigDecimal("50.00"));
        shouldSuccessTransfer(from, to, new BigDecimal("30"));
        shouldSuccessCheckBalance(from, new BigDecimal("70.00"));
        shouldSuccessCheckBalance(to, new BigDecimal("80.00"));
        shouldFailTransferWhenInsufficientFunds(from, to, new BigDecimal("71.00"));
    }

    private Long shouldSuccessCreateAccount(String name, BigDecimal balance) throws Exception {
        String request = String.format("{\"name\":\"%s\",\"balance\":\"%s\"}", name, balance.toPlainString());
        MockHttpServletResponse response = makePostRequest(request, CREATE_ACCOUNT_URI);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertTrue(jsonNode.findValue("errorMessage").isNull());
        assertFalse(jsonNode.findValue("accountDetails").isNull());
        return Long.valueOf(jsonNode.findValue("accountDetails").findValue("id").asText());
    }

    private void shouldSuccessTransfer(Long from, Long to, BigDecimal amount) throws Exception {
        String request = String.format("{\"from\":\"%d\",\"to\":\"%d\",\"amount\":\"%s\"}", from, to, amount.toPlainString());
        MockHttpServletResponse response = makePostRequest(request, TRANSFER_PAYMENT_URI);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private void shouldFailTransferWhenInsufficientFunds(Long from, Long to, BigDecimal amount) throws Exception {
        String request = String.format("{\"from\":\"%d\",\"to\":\"%d\",\"amount\":\"%s\"}", from, to, amount.toPlainString());
        MockHttpServletResponse response = makePostRequest(request, TRANSFER_PAYMENT_URI);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertFalse(jsonNode.findValue("errorMessage").isNull());
        assertTrue("Insufficient funds".equals(jsonNode.findValue("errorMessage").asText()));
    }

    private void shouldSuccessCheckBalance(Long from, BigDecimal fromBalance) throws Exception {
        String request = String.format("{\"id\":\"%d\"}", from);
        MockHttpServletResponse response = makeGetRequest(request, ACCOUNT_INFO_URI);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertTrue(jsonNode.findValue("errorMessage").isNull());
        assertFalse(jsonNode.findValue("accountDetails").isNull());
        BigDecimal actual = new BigDecimal(jsonNode.findValue("accountDetails").findValue("balance").asText());
        assertTrue(fromBalance.compareTo(actual) == 0);
    }

    private MockHttpServletResponse makePostRequest(String request, String uri) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(uri).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    private MockHttpServletResponse makeGetRequest(String request, String uri) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(uri).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    private MockHttpServletResponse makeRequest(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder).andReturn().getResponse();
    }

}

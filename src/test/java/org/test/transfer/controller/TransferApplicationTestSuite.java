package org.test.transfer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransferApplicationTestSuite extends AbstractTransferTest {

    @Test
    public void testSuite() throws Exception {
        Long from = shouldSuccessCreateAccount("test1", new BigDecimal("100.00"));
        shouldSuccessCheckBalance(from, new BigDecimal("100.00"));

        Long to = shouldSuccessCreateAccount("test2", new BigDecimal("50.00"));
        shouldSuccessCheckBalance(to, new BigDecimal("50.00"));

        shouldSuccessTransfer(from, to, new BigDecimal("30"));
        shouldSuccessCheckBalance(from, new BigDecimal("70.00"));
        shouldSuccessCheckBalance(to, new BigDecimal("80.00"));

        shouldFailTransferWhenInsufficientFunds(from, to, new BigDecimal("71.00"));
    }

    private Long shouldSuccessCreateAccount(String name, BigDecimal balance) throws Exception {
        String request = String.format("{\"name\":\"%s\",\"balance\":\"%s\"}", name, balance.toPlainString());
        MockHttpServletResponse response = createAccount(request);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertTrue(jsonNode.findValue("errorMessage").isNull());
        assertFalse(jsonNode.findValue("accountDetails").isNull());
        return Long.valueOf(jsonNode.findValue("accountDetails").findValue("id").asText());
    }

    private void shouldSuccessTransfer(Long from, Long to, BigDecimal amount) throws Exception {
        String request = String.format("{\"from\":\"%d\",\"to\":\"%d\",\"amount\":\"%s\"}", from, to, amount.toPlainString());
        MockHttpServletResponse response = transfer(request);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private void shouldFailTransferWhenInsufficientFunds(Long from, Long to, BigDecimal amount) throws Exception {
        String request = String.format("{\"from\":\"%d\",\"to\":\"%d\",\"amount\":\"%s\"}", from, to, amount.toPlainString());
        MockHttpServletResponse response = transfer(request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertFalse(jsonNode.findValue("errorMessage").isNull());
        assertTrue("Insufficient funds".equals(jsonNode.findValue("errorMessage").asText()));
    }

    private void shouldSuccessCheckBalance(Long from, BigDecimal fromBalance) throws Exception {
        String request = String.format("{\"id\":\"%d\"}", from);
        MockHttpServletResponse response = getAccountInfo(request);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        JsonNode jsonNode = OBJECT_MAPPER.reader().readTree(response.getContentAsString());
        assertTrue(jsonNode.findValue("errorMessage").isNull());
        assertFalse(jsonNode.findValue("accountDetails").isNull());
        BigDecimal actual = new BigDecimal(jsonNode.findValue("accountDetails").findValue("balance").asText());
        assertTrue(fromBalance.compareTo(actual) == 0);
    }

}

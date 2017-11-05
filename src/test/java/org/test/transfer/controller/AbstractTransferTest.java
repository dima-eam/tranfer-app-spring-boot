package org.test.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class AbstractTransferTest {

    private static final String CREATE_ACCOUNT_URI = "/app/api/account/create/";
    private static final String ACCOUNT_INFO_URI = "/app/api/account/info/";
    private static final String TRANSFER_PAYMENT_URI = "/app/api/transfer/payment";

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    protected MockHttpServletResponse createAccount(String request) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CREATE_ACCOUNT_URI).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    protected MockHttpServletResponse getAccountInfo(String request) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ACCOUNT_INFO_URI).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    protected MockHttpServletResponse transfer(String request) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRANSFER_PAYMENT_URI).content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    private MockHttpServletResponse makeRequest(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder).andReturn().getResponse();
    }

}

package org.test.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.testng.Assert.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc
class AbstractTransferTest extends AbstractTestNGSpringContextTests {

    private static final String OAUTH_TOKEN_URI = "/oauth/token";
    private static final String CREATE_ACCOUNT_URI = "/app/api/account/create/";
    private static final String ACCOUNT_INFO_URI = "/app/api/account/info/";
    static final String TRANSFER_PAYMENT_URI = "/app/api/transfer/payment";

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpServletResponse createAccount(String request) throws Exception {
        RequestBuilder requestBuilder = postAuthorizedBuilder(CREATE_ACCOUNT_URI).content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        return makeRequest(requestBuilder);
    }

    MockHttpServletResponse getAccountInfo(@Nullable String request) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = getAuthorizedBuilder(ACCOUNT_INFO_URI);
        if (request != null) {
            requestBuilder.content(request).contentType(MediaType.APPLICATION_JSON_VALUE);
        }
        return makeRequest(requestBuilder);
    }

    MockHttpServletResponse transfer(String request) throws Exception {
        return transfer(request, MockMvcRequestBuilders.post(TRANSFER_PAYMENT_URI), "test");
    }

    MockHttpServletResponse transfer(String request, String clientId) throws Exception {
        return transfer(request, MockMvcRequestBuilders.post(TRANSFER_PAYMENT_URI), clientId);
    }

    MockHttpServletResponse transfer(String request, MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return transfer(request, requestBuilder, "test");
    }

    private MockHttpServletResponse transfer(String request, MockHttpServletRequestBuilder requestBuilder, String clientId)
            throws Exception {
        String response = mockMvc.perform(postAuthorizedBuilder(OAUTH_TOKEN_URI)
                .param("grant_type", "password")
                .param("client_id", clientId)
                .param("username", "test")
                .param("password", "1234")
        ).andReturn().getResponse().getContentAsString();
        assertFalse(StringUtils.isEmpty(response));

        String token = OBJECT_MAPPER.readTree(response).get("access_token").asText();

        requestBuilder.header(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + token)
                .content(request).contentType(MediaType.APPLICATION_JSON_VALUE);

        return makeRequest(requestBuilder);
    }

    static MockHttpServletRequestBuilder getAuthorizedBuilder(String uri) {
        return withAuth(MockMvcRequestBuilders.get(uri));
    }

    private static MockHttpServletRequestBuilder postAuthorizedBuilder(String uri) {
        return withAuth(MockMvcRequestBuilders.post(uri));
    }

    private static MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder builder) {
        return builder.with(httpBasic("test", "1234"));
    }

    private MockHttpServletResponse makeRequest(RequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder).andReturn().getResponse();
    }

}

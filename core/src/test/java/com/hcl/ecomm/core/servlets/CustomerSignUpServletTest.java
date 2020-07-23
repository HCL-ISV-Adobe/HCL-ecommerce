package com.hcl.ecomm.core.servlets;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSignUpServletTest {
    private CustomerSignUpServlet customerSignUpServlet;

    @BeforeEach
    void setUp() {
        customerSignUpServlet = spy(new CustomerSignUpServlet());
    }

    @Test
    void doPost() throws JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        doReturn(getCustomerSigUpResponse()).when(customerSignUpServlet).getCustomerSignUp(any());

        customerSignUpServlet.doPost(request, response);
        verify(customerSignUpServlet, times(1)).doPost(request, response);
    }

    private String getPayload() {
        String payload = "{\n" +
                "  \"email\": \"jdoe@example.com\",\n" +
                "  \"firstname\": \"Jane\",\n" +
                "  \"lastname\": \"Doe\",\n" +
                "  \"password\": \"Password1\"\n" +
                "}";
        return payload;
    }

    private JSONObject getCustomerSigUpResponse() throws JSONException {
        String response = "{\n" +
                "  \"message\": {\n" +
                "    \"customerToken\": \"oq191aqcp7wb2ues48cuuukga1ig37g1\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }
}
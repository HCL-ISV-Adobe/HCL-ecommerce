package com.hcl.ecomm.core.servlets;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerSignInServletTest {
    private CustomerSignInServlet customerSignInServlet;

    @BeforeEach
    void setUp() {
        customerSignInServlet = spy(new CustomerSignInServlet());
    }

    @Test
    void doPost() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));

        doReturn(getCustomerSigInResponse()).when(customerSignInServlet).getcustomerSignin(any());
        doReturn(getCustomerProfile()).when(customerSignInServlet).getCustomerProfile(anyString());
        customerSignInServlet.doPost(request, response);
        verify(customerSignInServlet, times(1)).doPost(request, response);
    }

    private String getPayload() {
        String payload = "\n{\n" +
                "  \"username\": \"Vishal Gupta\",\n" +
                "  \"password\": \"hcl@12345678\"\n" +
                "}";
        return payload;
    }

    private JSONObject getCustomerSigInResponse() throws JSONException {
        String response = "{\n" +
                "  \"statusCode\": 200,\n" +
                "  \"customerToken\": \"oq191aqcp7wb2ues48cuuukga1ig37g1\"\n" +
                "}";
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject;
    }

    private JSONObject getCustomerProfile() throws JSONException {
        String customerProfile = "{\n" +
                "  \"message\": {\n" +
                "    \"email\": \"jdoe@example.com\",\n" +
                "    \"firstname\": \"Jane\",\n" +
                "    \"lastname\": \"Doe\",\n" +
                "    \"customerToken\": \"sdfQEER23dfexisiu123389bsnu\",\n" +
                "    \"website_id\": \"86276512\",\n" +
                "    \"store_id\": \"US_8926\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        JSONObject jsonObject = new JSONObject(customerProfile);
        return jsonObject;
    }
}
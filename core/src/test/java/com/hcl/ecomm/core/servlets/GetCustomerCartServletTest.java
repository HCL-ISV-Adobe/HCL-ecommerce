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
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCustomerCartServletTest {
    private GetCustomerCartServlet getCustomerCartServlet;

    @BeforeEach
    protected void setUp() {
        getCustomerCartServlet = spy(new GetCustomerCartServlet());
    }

    @Test
    void testDoGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getHeader("CustomerToken")).thenReturn("oasdkuhsiutjbsgJOFHYLASP");
        JSONObject responseObject = new JSONObject(getSubmitResponse());
        doReturn(responseObject).when(getCustomerCartServlet).getCustomerCart(anyString());
        getCustomerCartServlet.doGet(request, response);
        verify(getCustomerCartServlet, times(1)).doGet(request, response);
    }
    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": [{\n" +
                "    \"item_id\": 149,\n" +
                "    \"sku\": \"24-WG085\",\n" +
                "    \"qty\": 1,\n" +
                "    \"name\": \"Sprite Yoga Strap 6 foot\",\n" +
                "    \"price\": 14,\n" +
                "    \"product_type\": \"simple\",\n" +
                "    \"quote_id\": \"SAq0k6GhKYZTn0V86qSd5LUOICpg1Ok2\"\n" +
                "  }],\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
}
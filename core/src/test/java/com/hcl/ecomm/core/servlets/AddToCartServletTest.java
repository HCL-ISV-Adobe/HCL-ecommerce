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
class AddToCartServletTest {
    private AddToCartServlet addToCartServlet;

    @BeforeEach
    void setUp() {
        addToCartServlet = spy(new AddToCartServlet());
    }

    @Test
    void doPost() throws JSONException {

        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");

        JSONObject jsonObject = new JSONObject(getSubmitResponse());
        doReturn(jsonObject).when(addToCartServlet).getAddToCartResponse(any(), anyString());
        addToCartServlet.doPost(request, response);
        verify(addToCartServlet, times(1)).doPost(request, response);
    }

    @Test
    void doPut() throws JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");

        JSONObject jsonObject = new JSONObject(getSubmitResponse());
        doReturn(jsonObject).when(addToCartServlet).getUpdateCartItem(any(), any(), anyString());

        addToCartServlet.doPut(request, response);
        verify(addToCartServlet, times(1)).doPut(request, response);

    }

    private String getPayload() {
        String payload = "\n{\n" +
                "  \"itemid\": 7,\n" +
                "  \"price\": 56,\n" +
                "  \"cartid\": \"j7KaMe1zWFfopDFOVTdFZV0rokpjwzam\",\n" +
                "  \"qty\": 20,\n" +
                "  \"sku\": \"mehiwiext\"\n" +
                "}";
        return payload;
    }

    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": {\n" +
                "    \"cartid\": \"G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
}
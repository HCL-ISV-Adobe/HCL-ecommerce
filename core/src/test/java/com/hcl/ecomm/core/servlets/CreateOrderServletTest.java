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
class CreateOrderServletTest {
    private CreateOrderServlet createOrderServlet;

    @BeforeEach
    void setUp() {
        createOrderServlet = spy(new CreateOrderServlet());
    }

    @Test
    void doPut() throws ServletException, IOException, JSONException {

        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");

        JSONObject jsonObject = new JSONObject(getOrderResponse());

        doReturn(jsonObject).when(createOrderServlet).getCreateOrderItem(any(), any(), anyString());

        createOrderServlet.doPut(request, response);
        verify(createOrderServlet, times(1)).doPut(request, response);
    }

    private String getPayload() {
        String payload = "\n{\n" +
                "  \"cartId\": \"LYStiBjf9yFVd52CPxGrC7Lcob6g5VSJ\",\n" +
                "  \"region\": \"MH\",\n" +
                "  \"region_id\": 0,\n" +
                "  \"country_id\": \"IN\",\n" +
                "  \"street\": [\n" +
                "    \"Chakala(e)\"\n" +
                "  ],\n" +
                "  \"telephone\": \"1111111\",\n" +
                "  \"postcode\": \"12223\",\n" +
                "  \"code\": \"93752\",\n" +
                "  \"city\": \"Bengaluru\",\n" +
                "  \"firstname\": \"Nithesh\",\n" +
                "  \"lastname\": \"kumar\",\n" +
                "  \"email\": \"abc@abc.com\",\n" +
                "  \"region_code\": \"MH\",\n" +
                "  \"shipping_method_code\": \"flatrate\",\n" +
                "  \"shipping_carrier_code\": \"flatrate\"\n" +
                "}";
        return payload;
    }

    private String getOrderResponse() {
        String orderResponse = "{\n" +
                "  \"message\": {\n" +
                "    \"orderId\": \"1234567890\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return orderResponse;
    }
}
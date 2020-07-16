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
class ShippingInfoTest {

   private ShippingInfo shippingInfo;

    @BeforeEach
   protected  void setUp(){
        shippingInfo = spy (new ShippingInfo());
    }


    @Test
    void doPost() throws JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");
        JSONObject responseObject = new JSONObject(getSubmitResponse());
        doReturn(responseObject).when(shippingInfo).createShipInfo(any(),any(),anyString());
        shippingInfo.doPost(request,response);
        verify(shippingInfo, times(1)).doPost(request, response);
    }
    private String getPayload() {
        String payload = "\n{\n" +
                "  \"cartId\": \"gFd6lxo1Txa8pvz94AT7rJiwOeWxqfoV\",\n" +
                "  \"region\": \"MH\",\n" +
                "  \"region_id\": 0,\n" +
                "  \"country_id\": \"IN\",\n" +
                "  \"street\": [\n" +
                "  \"Chakala(e)\"\n" +
                "  ],\n" +
                "  \"telephone\": \"1111111\",\n" +
                "  \"postcode\": \"12223\",\n" +
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
    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": {\n" +
                "  \"payment_methods\": [{\n" +
                "  \"code\": \"checkmo\",\n" +
                "  \"title\": \"Check / Money order\"\n" +
                "  }]},\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
}
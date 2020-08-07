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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyAccountAddressServletTest {

    private MyAccountAddressServlet myAccountAddressServlet;

    @BeforeEach
    void setUp(){
        myAccountAddressServlet = spy(new MyAccountAddressServlet());
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {

        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");

        JSONObject jsonObject = new JSONObject(getAddressResponse());

        doReturn(jsonObject).when(myAccountAddressServlet).customerProfile(anyString());
        myAccountAddressServlet.doGet(request,response);
        verify(myAccountAddressServlet, times(1)).doGet(request, response);
    }

    private String getAddressResponse() {
        String addressResponse = "{\n" +
                "  \"message\": {\n" +
                "    \"email\": \"deepkumar@gmail.com\",\n" +
                "    \"firstname\": \"Deep\",\n" +
                "    \"lastname\": \"Kumar\",\n" +
                "    \"websiteId\": 2,\n" +
                "    \"addresses\": [\n" +
                "      {\n" +
                "        \"firstname\": \"Vishal\",\n" +
                "        \"lastname\": \"Gupta\",\n" +
                "        \"postcode\": \"17828\",\n" +
                "        \"city\": \"BANGALORE\",\n" +
                "        \"street\": [\n" +
                "          \"Mico Layout 10th A Cross, BTM 2nd Stage\"\n" +
                "        ],\n" +
                "        \"telephone\": \"0834047606\",\n" +
                "        \"country_id\": \"IN\",\n" +
                "        \"region\": {\n" +
                "          \"region\": \"KA\",\n" +
                "          \"region_code\": \"KA\",\n" +
                "          \"region_id\": \"4026\"\n" +
                "        },\n" +
                "        \"region_id\": \"0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"firstname\": \"Shubham\",\n" +
                "        \"lastname\": \"Mittal\",\n" +
                "        \"postcode\": \"17828\",\n" +
                "        \"city\": \"BANGALORE\",\n" +
                "        \"street\": [\n" +
                "          \"Mico Layout 10th A Cross, BTM 2nd Stage\"\n" +
                "        ],\n" +
                "        \"telephone\": \"0834047606\",\n" +
                "        \"country_id\": \"IN\",\n" +
                "        \"region\": {\n" +
                "          \"region\": \"KA\",\n" +
                "          \"region_code\": \"KA\",\n" +
                "          \"region_id\": \"4026\"\n" +
                "        },\n" +
                "        \"region_id\": \"0\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return addressResponse;
    }
}

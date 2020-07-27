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
class DeleteCartItemServletTest {

    private DeleteCartItemServlet deleteCartItemServlet;


    @BeforeEach
    protected  void setUp()  {
        deleteCartItemServlet = spy (new DeleteCartItemServlet());
    }

    @Test
    void doPut() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");
        JSONObject jsonObject = new JSONObject(getSubmitResponse());
        doReturn(jsonObject).when(deleteCartItemServlet).getDeleteCartItem(any(), anyString());
        deleteCartItemServlet.doPut(request,response);
        verify(deleteCartItemServlet, times(1)).doPut(request, response);
    }
    private String getPayload() {
        String payload = "\n{\n" +
                "  \"cartId\": \"gFd6lxo1Txa8pvz94AT7rJiwOeWxqfoV\",\n" +
                "  \"itemId\": 1\n" +
                "}";
        return payload;
    }
    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": {\n" +
                "    \"isDeleted\": \"true\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
}
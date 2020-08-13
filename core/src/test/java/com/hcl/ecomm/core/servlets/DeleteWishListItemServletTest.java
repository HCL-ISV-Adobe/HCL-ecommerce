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
class DeleteWishListItemServletTest {

    private DeleteWishListItemServlet deleteWishListItemServlet;


    @BeforeEach
    protected  void setUp()  {
        deleteWishListItemServlet = spy (new DeleteWishListItemServlet());
    }

    @Test
    void doPut() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("hipsyozpoki0zwl4utgf552a0dh3gwik");
        JSONObject jsonObject = new JSONObject(getSubmitResponse());
        doReturn(jsonObject).when(deleteWishListItemServlet).getdeleteWishListItem(any(),any());
        deleteWishListItemServlet.doDelete(request,response);
        verify(deleteWishListItemServlet, times(1)).doDelete(request,response);
    }
    private String getPayload() {
        String payload = "{\n" +
                "  \"itemId\":\"49\"\n" +
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
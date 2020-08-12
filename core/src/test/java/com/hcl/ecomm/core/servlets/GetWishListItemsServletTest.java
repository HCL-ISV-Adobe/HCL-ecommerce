package com.hcl.ecomm.core.servlets;

import com.adobe.cq.social.scf.JsonException;
import com.google.gson.JsonElement;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetWishListItemsServletTest {
    private GetWishListItemsServlet getWishListItemsServlet;


    @BeforeEach

    protected void setUp() {
        getWishListItemsServlet = spy(new GetWishListItemsServlet());
    }

    @Test
    void testDoGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getHeader("CustomerToken")).thenReturn("75gsg9vc8f1e0sjy492g3l816ay2ev9x");
        doReturn(getJsonObject()).when(getWishListItemsServlet).getWishListItems(anyString());

        getWishListItemsServlet.doGet(request, response);
        verify(getWishListItemsServlet, times(1)).doGet(request, response);
    }

    private JSONObject getJsonObject() throws JSONException {
       // String responseStream = "{\"item_id\":5\"}";
        JSONObject obj2 = new JSONObject();
        obj2.put("item_id",5);
        obj2.put("sku","24-MB01");
        obj2.put("name","Joust Duffle Bag");
        obj2.put("price",100);
        JSONArray arr = new JSONArray();
        arr.put(0,obj2);
        JSONObject obj1 = new JSONObject();
        obj1.put("items",arr);
        //JsonObject JsonObject = new JsonObject(responseStream);
        return obj1;
    }
}
package com.hcl.ecomm.core.servlets;

import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailsServletTest {
    private ProductDetailsServlet productDetailsServlet;

    @BeforeEach
    protected  void setUp()  {
        productDetailsServlet = spy (new ProductDetailsServlet());
    }
    @Test
    void doGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getParameter("sku")).thenReturn("24-WG085");

        doReturn(getSubmitResponse()).when(productDetailsServlet).getProductDetail(anyString());
        productDetailsServlet.doGet(request,response);
        verify(productDetailsServlet, times(1)).doGet(request, response);

    }
    private JSONArray getSubmitResponse() throws JSONException {
        String submitResponse =  "{\n" +
                "  \"id\": 1,\n" +
                "  \"sku\": \"24-WG085\",\n" +
                "  \"name\": \"Sprite Yoga Strap 6 foot\",\n" +
                "  \"price\": 14,\n" +
                "  \"product_type\": \"simple\",\n" +
                "  \"visibility\": 4,\n" +
                "  \"status\": 1,\n" +
                "  \"attribute_set_id\": 4,\n" +
                "  \"type_id\": \"simple\",\n" +
                "  \"extension_attributes\": {\n" +
                "    \"stock_item\": {\n" +
                "      \"qty\": 100,\n" +
                "      \"is_in_stock\": true\n" +
                "    }\n" +
                "  },\n" +
                "  \"product_links\": [\n" +
                "    {\n" +
                "      \"linked_product_sku\": \"12453\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        JSONObject jsonObject =new JSONObject(submitResponse);
        JSONArray jsonArray=new JSONArray();
        jsonArray.put(jsonObject);
        return jsonArray;
    }
}
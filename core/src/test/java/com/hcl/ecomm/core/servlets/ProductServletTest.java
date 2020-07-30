package com.hcl.ecomm.core.servlets;


import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServletTest {
    private ProductServlet productServlet;

    @BeforeEach
    protected  void setUp()  {
        productServlet = spy (new ProductServlet());
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        JSONArray jsonArray = new JSONArray(getSubmitResponse());
        doReturn(jsonArray).when(productServlet).getAllProductDetails();
        productServlet.doGet(request,response);
        verify(productServlet, times(1)).doGet(request, response);

    }
    private String getSubmitResponse() {
        String submitResponse =  "[{\n" +
                "    \"id\": 1,\n" +
                "    \"sku\": \"24-WG085\",\n" +
                "    \"qty\": 1,\n" +
                "    \"name\": \"Sprite Yoga Strap 6 foot\",\n" +
                "    \"price\": 14,\n" +
                "    \"product_type\": \"simple\",\n" +
                "    \"visibility\": 4,\n" +
                "    \"status\": 1,\n" +
                "    \"attribute_set_id\": 4,\n" +
                "    \"type_id\": \"simple\"\n" +
                "}]";
        return submitResponse;
    }
}
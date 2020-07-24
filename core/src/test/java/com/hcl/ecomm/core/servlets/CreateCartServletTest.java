package com.hcl.ecomm.core.servlets;


import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(AemContextExtension.class)
class CreateCartServletTest {

    private CreateCartServlet createCartServlet;

    @BeforeEach
    void setUp() throws JSONException {
        createCartServlet = spy(new CreateCartServlet());
    }

    @Test
    void doGet(AemContext context) throws ServletException, IOException, JSONException {

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        JSONObject jsonObject = new JSONObject(getStatusJson());
        request.setHeader("CustomerToken", "asdkuhsiutjbsgJOFHYLASP");
        doReturn(jsonObject).when(createCartServlet).getJSONFromCartService("asdkuhsiutjbsgJOFHYLASP");
        createCartServlet.doGet(request, response);
        assertEquals("{\"message\":{\"cartid\":\"G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea\"},\"status\":true}\r\n", response.getOutputAsString());
    }

    private String getStatusJson() {
        String response = "{\"message\":{\"cartid\":\"G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea\"},\"statusCode\":200}";
        return response;
    }
}
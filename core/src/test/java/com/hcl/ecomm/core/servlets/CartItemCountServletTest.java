package com.hcl.ecomm.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(AemContextExtension.class)
class CartItemCountServletTest {
    private CartItemCountServlet cartItemCountServlet;

    @BeforeEach
    void setUp() {
        cartItemCountServlet = spy(new CartItemCountServlet());
    }

    @Test
    void doGet(AemContext context) throws ServletException, IOException {

        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        request.setHeader("CustomerToken", "asdkuhsiutjbsgJOFHYLASP");
        HashMap<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("cartId", "G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea");
        request.setParameterMap(productMap);

        doReturn(3).when(cartItemCountServlet).getCartItemCount(anyString(), anyString());
        cartItemCountServlet.doGet(request, response);
        assertEquals("3", response.getOutputAsString());
    }
}
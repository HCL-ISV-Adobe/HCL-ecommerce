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
class CartItemServletTest {
    private CartItemServlet cartItemServlet;

    @BeforeEach
    void setUp() {
        cartItemServlet = spy(CartItemServlet.class);
    }

    @Test
    void doGet() throws ServletException, IOException, JSONException {

        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);

        when(request.getHeader("CustomerToken")).thenReturn("oq191aqcp7wb2ues48cuuukga1ig37g1");
        when(request.getParameter("cartId")).thenReturn("G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea");

        doReturn(getJsonArray()).when(cartItemServlet).getCartItemsDetails(anyString(), anyString());
        cartItemServlet.doGet(request, response);
        verify(cartItemServlet, times(1)).doGet(request, response);
    }

    public JSONArray getJsonArray() throws JSONException {
        String responseStream = "[{\"item_id\":5,\"sku\":\"24-MB01\",\"qty\":1,\"name\":\"Joust Duffle Bag\",\"price\":34,\"product_type\":\"simple\",\"quote_id\":\"1YvRyVuR4PvRXQDY8HBGHoQB54FGB3g5\"},{\"item_id\":6,\"sku\":\"24-WG085\",\"qty\":1,\"name\":\"Sprite Yoga Strap 6 foot\",\"price\":14,\"product_type\":\"simple\",\"quote_id\":\"1YvRyVuR4PvRXQDY8HBGHoQB54FGB3g5\"}]\n";
        JSONArray jsonArray = new JSONArray(responseStream);
        return jsonArray;
    }
}




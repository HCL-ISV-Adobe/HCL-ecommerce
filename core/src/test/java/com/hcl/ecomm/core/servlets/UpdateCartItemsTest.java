package com.hcl.ecomm.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemsTest {
    private UpdateCartItems updateCartItems;

    @BeforeEach
    protected  void setUp()  {
        updateCartItems = spy (new UpdateCartItems());
    }
    @Test
    void doGet() throws ServletException, IOException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getHeader("CustomerToken")).thenReturn("oasdkuhsiutjbsgJOFHYLASP");
        when(request.getParameter("payload")).thenReturn(getPayload());
        doReturn(getSubmitResponse()).when(updateCartItems).updateCartDetails(anyString(),anyString());
        updateCartItems.doGet(request, response);
        verify(updateCartItems, times(1)).doGet(request, response);
    }
    private String getPayload() {
        String payload =  "[{\n" +
                "  \"cartItem\": {\n" +
                "    \"quote_id\": \"txqESTAaPDek4AfkuFqBS0j2sk77hIvn\",\n" +
                "    \"item_id\": \"132\",\n" +
                "    \"sku\": \"24-WG085\",\n" +
                "    \"qty\": \"1\"\n" +
                "}}]";
        return payload;
    }
    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": [{\n" +
                "    \"item_id\": 149,\n" +
                "    \"sku\": \"24-WG085\",\n" +
                "    \"qty\": 1,\n" +
                "    \"name\": \"Sprite Yoga Strap 6 foot\",\n" +
                "    \"price\": 14,\n" +
                "    \"product_type\": \"simple\",\n" +
                "    \"quote_id\": \"SAq0k6GhKYZTn0V86qSd5LUOICpg1Ok2\"\n" +
                "  }],\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
}

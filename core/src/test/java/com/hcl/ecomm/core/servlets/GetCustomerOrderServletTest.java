package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.OrderService;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GetCustomerOrderServletTest {
    private GetCustomerOrderServlet getCustomerOrderServlet;

    @Reference
    private OrderService orderService;
    @BeforeEach
    protected void setUp() {
        getCustomerOrderServlet = spy(new GetCustomerOrderServlet());
    }

    @Test
    void doPost() throws ServletException, IOException, JSONException {
        MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
        MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
        when(request.getHeader("CustomerToken")).thenReturn("42agsapn08p2ndi4ebvrl0rhmygi2hy8");
        JSONObject responseObject = new JSONObject(getSubmitResponse());
        doReturn(responseObject).when(getCustomerOrderServlet).getCustomerOrder(any());
        getCustomerOrderServlet.doPost(request, response);
        verify(getCustomerOrderServlet, times(1)).doPost(request, response);
    }
    private String getSubmitResponse() {
        String submitResponse = "{\n" +
                "  \"message\": {\n" +
                "    \"cartid\": \"G26bBezJCUm9ZwVbtlkWVvIY7I8hYOea\"\n" +
                "  },\n" +
                "  \"statusCode\": 200\n" +
                "}";
        return submitResponse;
    }
    private String getPayload() {
        String payload = "{\n" +
                "  \"custEmail\":\"shubhamgarg8383@gmail.com\"\n" +
                "}";
        return payload;
    }
}
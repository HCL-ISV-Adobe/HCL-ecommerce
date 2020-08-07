package com.hcl.ecomm.core.servlets;

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

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@MockitoSettings(strictness = Strictness.LENIENT)
    @ExtendWith(MockitoExtension.class)
    public class CustomerChangePasswordServletTest {

        private CustomerChangePasswordServlet customerChangePasswordServlet;


        @BeforeEach
        protected  void setUp()  {
            customerChangePasswordServlet = spy (new CustomerChangePasswordServlet());
        }

        @Test
        void doPut() throws ServletException, IOException, JSONException {
            MockSlingHttpServletRequest request = mock(MockSlingHttpServletRequest.class);
            MockSlingHttpServletResponse response = mock(MockSlingHttpServletResponse.class);
            when(request.getReader()).thenReturn(new BufferedReader(new StringReader(getPayload())));
            when(request.getHeader("CustomerToken")).thenReturn("onoeyuminifo49pe1j7gfcn5c732jslu");
            JSONObject jsonObject = new JSONObject(getSubmitResponse());
            doReturn(jsonObject).when(customerChangePasswordServlet).changePassword(any());
            customerChangePasswordServlet.doPut(request,response);
            verify(customerChangePasswordServlet, times(1)).doPut(request, response);
        }
        private String getPayload() {
            String payload = "{\n" +
                    "\n" +
                    "  \"customerToken\": \"onoeyuminifo49pe1j7gfcn5c732jslu\",\n" +
                    "\n" +
                    "  \"custId\": \"24\",\n" +
                    "\n" +
                    "  \"currentPassword\": \"Admin123\",\n" +
                    "\n" +
                    "  \"newPassword\": \"Admin1234\"\n" +
                    "\n" +
                    "}";
            return payload;
        }
        private String getSubmitResponse() {
            String submitResponse = "{\n" +
                    "    \"message\": {\n" +
                    "        \"success\": \"Password Changed.\"\n" +
                    "    },\n" +
                    "    \"status\": true\n" +
                    "}";
            return submitResponse;
        }
    }


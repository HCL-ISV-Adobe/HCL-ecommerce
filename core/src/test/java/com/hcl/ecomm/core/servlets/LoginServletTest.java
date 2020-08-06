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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


@ExtendWith(AemContextExtension.class)
class LoginServletTest {
private LoginServlet loginServlet;


    @BeforeEach
    protected  void setUp()  {
        loginServlet = spy (new LoginServlet());
      }

    @Test
    void testDoGet(AemContext context) throws ServletException, IOException {

        MockSlingHttpServletResponse response = context.response();
        MockSlingHttpServletRequest request = context.request();
        doReturn("1vdvo8k6m63x8hthjdqzyimesmj97csy").when(loginServlet).getToken();
        loginServlet.doGet(request,response);
        assertEquals("Token Value : 1vdvo8k6m63x8hthjdqzyimesmj97csy",response.getOutputAsString());
    }
}
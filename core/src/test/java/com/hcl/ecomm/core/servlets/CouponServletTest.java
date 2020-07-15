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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(AemContextExtension.class)
class CouponServletTest {
    private CouponServlet couponServlet;

    @BeforeEach
    void setUp() {
        couponServlet = spy(new CouponServlet());
    }

    @Test
    void doGet(AemContext context) throws ServletException, IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        HashMap<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("coupon", "SHOP100");
        request.setParameterMap(productMap);

        doReturn("100").when(couponServlet).getDiscountValue("SHOP100");
        couponServlet.doGet(request, response);
        assertEquals("100", response.getOutputAsString());
    }
}
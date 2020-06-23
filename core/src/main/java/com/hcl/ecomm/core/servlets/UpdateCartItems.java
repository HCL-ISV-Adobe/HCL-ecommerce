package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.CartService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = { "sling.servlet.paths=/bin/hclecomm/updateCartItems",
                "sling.servlet.method=" + HttpConstants.METHOD_GET,
                "sling.servlet.extensions=json" })
public class UpdateCartItems extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger("UpdateCartItems.class");

    @Reference
    CartService cartService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String  cartItemResponse = "";
        String customerToken = request.getHeader("CustomerToken");
        try {
            String payload = request.getParameter("payload");
            if(payload!=null || !"".equals(payload))
            {
                cartItemResponse = cartService.updateCartDetails(payload, customerToken);
                LOG.info("cartItemCount : " + cartItemResponse);
            }
            else{
                cartItemResponse = "Item Details not provided";
            }
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(cartItemResponse);
        }
        catch(Exception e)
        {
            LOG.error("Exception while fetching cart details: " + e.getMessage());
        }
    }
}

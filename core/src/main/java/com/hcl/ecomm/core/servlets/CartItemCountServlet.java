package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.CartService;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class,
        property = { "sling.servlet.paths=/bin/hclecomm/cartItemsCount",
                "sling.servlet.method=" + HttpConstants.METHOD_GET,
                "sling.servlet.extensions=json" })

public class CartItemCountServlet extends SlingSafeMethodsServlet
{
    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(CartItemCountServlet.class);

    @Reference
    CartService cartService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String  cartItemResponse = "";
        try {
            String cartId = request.getParameter("cartId");
            LOG.info("Cart Id id : " + cartId);
            if(cartId!=null || !"".equals(cartId))
            {
                int cartItemCount = cartService.getCartItemCount(cartId);
                LOG.info("cartItemCount : " + cartItemCount);
                cartItemResponse = String.valueOf(cartItemCount);
            }
            else{
                cartItemResponse = "Cart Id not found";
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


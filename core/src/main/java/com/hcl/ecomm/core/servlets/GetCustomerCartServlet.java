package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.CartService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/getCustomerCart",
		"sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class GetCustomerCartServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4016057296495129474L;
	private static final Logger LOG = LoggerFactory.getLogger(GetCustomerCartServlet.class);

	@Reference
	CartService cartService;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		try {
			String customerToken = request.getHeader("CustomerToken");
			String cartItems = null;
			JsonArray responseStream = cartService.getCustomerCart(customerToken);
			LOG.info("responseStream is {}", responseStream.toString());

			response.setContentType("application/json");
			response.getWriter().write(responseStream.toString());
			response.setStatus(200);
		}
		catch (Exception e){
			LOG.error("error in product servlet {} ",e.getMessage());
			response.setStatus(500);
		}

	}
}
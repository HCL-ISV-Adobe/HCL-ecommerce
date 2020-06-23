package com.hcl.ecomm.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.ecomm.core.services.CreateCartService;
/**
 * Servlet that get created the cart in Magento by hitting the Magento service.
 */
@Component(
		service = Servlet.class,
		property = { 
				Constants.SERVICE_DESCRIPTION + "= Create Cart Servlet",
				"sling.servlet.paths=/bin/hclecomm/createCart", 
				"sling.servlet.method=" + HttpConstants.METHOD_GET,
			    "sling.servlet.extensions=json" })
public class CreateCartServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = -7980975418220422865L;

	private static final Logger LOG = LoggerFactory.getLogger(CreateCartServlet.class);

	@Reference
	private CreateCartService createCartService;

	
    /**
     * doGet gets to create cart in Magento and returned alphnumeric cart hashcode from Magento service.
     *
     * @param request
     *            - sling servlet request object
     * @param response
     *            - sling servlet response object
     */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();
		String customerToken = request.getHeader("CustomerToken");
		try {
			JSONObject createCartResponse = createCartService.createCart(customerToken);
			if (createCartResponse.has("statusCode") && createCartResponse.getInt("statusCode") == HttpStatus.OK_200) {
				responseObject.put("message", createCartResponse.getJSONObject("message"));
				responseObject.put("status", Boolean.TRUE);
			} else {
				responseObject.put("message", "something went wrong while create cart for guest user.");
				responseObject.put("status", Boolean.FALSE);
			}
		} catch (Exception e) {
			LOG.error("while while creating cart for user. Error={}", e);
		}
		response.getWriter().println(responseObject);
	}
}

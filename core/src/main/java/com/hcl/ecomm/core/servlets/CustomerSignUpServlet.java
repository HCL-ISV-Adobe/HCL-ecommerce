package com.hcl.ecomm.core.servlets;

import java.io.BufferedReader;

import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.CustomerService;

/**
 * Servlet that 
 * create customer, 
 * get customer details, 
 * forget password , 
 * change password 
 * change email
 * gets cart add item and update item related data by hitting the Magento service.
 * 
 * 
 */

@Component(
		service = Servlet.class, 
		property = { 
				 Constants.SERVICE_DESCRIPTION + "= Customer Signup Servlet",
				"sling.servlet.paths=/bin/hclecomm/customerSignup",
				"sling.servlet.method=" + HttpConstants.METHOD_POST,
				"sling.servlet.extensions=json"})
public class CustomerSignUpServlet extends SlingAllMethodsServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5026789683631316177L;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerSignUpServlet.class);
	
	@Reference
	private CustomerService customerService;
	
	
	  /**
     * doPost add the item in the Magento cart so that it can be persisted at their end.
     *
     * @param request
     *            - sling servlet request object
     * @param response
     *            - sling servlet response object
     */
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOG.debug("CustomerSignUpServlet doPost()  method start.");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();

		try {
			responseObject.put("message", "Request failed");
			responseObject.put("status", Boolean.FALSE);
			
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String payload = buffer.toString();
			if (StringUtils.isNotEmpty(payload)) {
				JSONObject jsonPayload =  new JSONObject(payload);
				if (isValidPayload(jsonPayload)) {
					JSONObject customerSignupObj = customerSignupObj(jsonPayload);
					JSONObject customerSignupResponse = customerService.customerSignup(customerSignupObj);
					if (customerSignupResponse.has("statusCode") && customerSignupResponse.getInt("statusCode") == HttpStatus.OK_200) {
						responseObject.put("message", customerSignupResponse.getJSONObject("message"));
						responseObject.put("status", Boolean.TRUE);
					} else {
						responseObject.put("message", "something went wrong while Customer Signup.");
					}
				}
			} else {
				responseObject.put("message", "Missing Parameter in payload.");
				responseObject.put("status", Boolean.FALSE);
			}
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.error("Error Occured while executing CustomerSignup doPost(). responseObject={} ", responseObject);
			LOG.error("Full Error={} ", e);
		}
		LOG.debug("CustomerSignup doPost()  method end.");
	}

	private boolean isValidPayload(JSONObject jsonPayload) {
		boolean isValidData=Boolean.TRUE;
		if(!jsonPayload.has("email") && !jsonPayload.has("firstname") && !jsonPayload.has("lastname") && !jsonPayload.has("password")){
			isValidData = Boolean.FALSE;
		}
		return isValidData;
	}
	
	private JSONObject customerSignupObj(JSONObject customerData) {
		JSONObject customerSignup = new JSONObject();
		JSONObject customer = new JSONObject();
		try {
			customer.put("email", customerData.getString("email"));
			customer.put("firstname", customerData.getString("firstname"));
			customer.put("lastname", customerData.getString("lastname"));
			customerSignup.put("customer", customer);
			customerSignup.put("password", customerData.getString("password"));
		} catch (JSONException e) {
			LOG.error("Error while executing. Error={}",e);
		}
		return customerSignup;
	}
}

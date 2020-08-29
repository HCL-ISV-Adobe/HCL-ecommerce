package com.hcl.ecomm.core.servlets;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.Servlet;

import com.hcl.ecomm.core.services.MailChimpService;
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
 * Servlet that provide the functionality to create new customer by hitting the Magento service.
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

	@Reference
	private MailChimpService mailChimpService;
	
	  /**
     * doPost create new customer so that it can be persisted at their end.
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
					//Adding customer to Mailchimp
					JSONObject customerValidDetails = customerDetailsAligned(jsonPayload);
					JSONObject mailChimpCustomerSignupResponse = getMailChimpSignup(customerValidDetails);
					if (mailChimpCustomerSignupResponse.has("statusCode") && mailChimpCustomerSignupResponse.getInt("statusCode") == HttpStatus.OK_200) {
						responseObject.put("MailChimp message", "customer signed up successfully.");
					} else {
						responseObject.put("message", "something went wrong while MailChimp Customer Signup.");
					}

					//Adding customer to Magento
					JSONObject customerSignupResponse = getCustomerSignUp(customerSignupObj);
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

	public JSONObject getCustomerSignUp(JSONObject customerSignupObj) {
		return customerService.customerSignup(customerSignupObj);
	}

	public JSONObject getMailChimpSignup(JSONObject customerDetails) throws UnsupportedEncodingException {
		return mailChimpService.mailChimpCustomerSignup(customerDetails);
	}

	private boolean isValidPayload(JSONObject jsonPayload) {
		boolean isValidData = Boolean.TRUE;
		if (!jsonPayload.has("email") && !jsonPayload.has("firstname") && !jsonPayload.has("lastname") && !jsonPayload.has("password")) {
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

	private JSONObject customerDetailsAligned(JSONObject customerData){
		JSONObject customerSignup = new JSONObject();
		JSONObject customer = new JSONObject();
		try {
			customer.put("FNAME", customerData.getString("firstname"));
			customer.put("LNAME", customerData.getString("lastname"));
			customer.put("PHONE", customerData.getString("phone"));
			customerSignup.put("merge_fields", customer);
			customerSignup.put("email_address",customerData.getString("email") );
			customerSignup.put("status","subscribed");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return customerSignup;
	}
}

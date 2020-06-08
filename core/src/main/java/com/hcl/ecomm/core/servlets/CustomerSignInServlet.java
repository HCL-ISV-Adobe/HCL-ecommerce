package com.hcl.ecomm.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

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

import com.hcl.ecomm.core.services.CustomerService;


@Component(
		service = Servlet.class, 
		property = { 
				 Constants.SERVICE_DESCRIPTION + "= Customer Signin Servlet",
				"sling.servlet.paths=/bin/hclecomm/customerSignin",
				"sling.servlet.method=" + HttpConstants.METHOD_POST,
				"sling.servlet.extensions=json"})
public class CustomerSignInServlet extends SlingAllMethodsServlet{


	private static final long serialVersionUID = 2731083214874663626L;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerSignInServlet.class);

	@Reference
	private CustomerService customerService;

	
    /**
     * doGet gets to create cart in Magento and returned alphnumeric cart hashcode from Magento service.
     *
     * @param request
     *            - sling servlet request object
     * @param response
     *            - sling servlet response object
     */
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("CustomerSignInServlet doPost()  method start.");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();
		JSONObject errorRes = new JSONObject();
		
		try {
			errorRes.put("error", "Request failed");
			responseObject.put("message", errorRes);
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
					JSONObject customerSigninObj = customerSigninObj(jsonPayload);
					JSONObject customerSigninRes = customerService.customerSignin(customerSigninObj);
					if (customerSigninRes.has("statusCode") && customerSigninRes.getInt("statusCode") == HttpStatus.OK_200) {
					String customerToken=customerSigninRes.getString("customerToken");
						JSONObject customerProfileRes = customerService.customerProfile(customerToken);
						if (customerProfileRes.has("statusCode") && customerProfileRes.getInt("statusCode") == HttpStatus.OK_200) {
							customerProfileRes.put("customerToken", customerToken);
							JSONObject profile =customerProfileRes.getJSONObject("message").put("customerToken", customerToken);
							responseObject.put("message", profile);
							responseObject.put("status", Boolean.TRUE);
						}
					} else {
						responseObject.put("message",errorRes);
					}
				}
			} else {
				responseObject.put("message", errorRes);
				responseObject.put("status", Boolean.FALSE);
			}
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.error("Error Occured while executing CustomerSignin doPost(). responseObject={} ", responseObject);
			LOG.error("Full Error={} ", e);
		}
		LOG.debug("CustomerSignin doPost()  method end.");
	}
	
	
	private boolean isValidPayload(JSONObject jsonPayload) {
		boolean isValidData=Boolean.TRUE;
		if(!jsonPayload.has("username") && !jsonPayload.has("password")){
			isValidData = Boolean.FALSE;
		}
		return isValidData;
	}
	
	private JSONObject customerSigninObj(JSONObject customerLoginData) {
		JSONObject customerSignin = new JSONObject();
		try {
			customerSignin.put("username", customerLoginData.getString("username"));
			customerSignin.put("password", customerLoginData.getString("password"));
		} catch (JSONException e) {
			LOG.error("Error while executing customerSigninObj. Error={}",e);
		}
		return customerSignin;
	}

}

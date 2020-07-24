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

/**
 * Servlet that provide the customer profile update functionality by hitting the Magento service.
 * 
 * 
 */

@Component(
		service = Servlet.class, 
		property = { 
				 Constants.SERVICE_DESCRIPTION + "= Customer Profile Update Servlet",
				"sling.servlet.paths=/bin/hclecomm/customerChangePassword",
				"sling.servlet.method=" + HttpConstants.METHOD_PUT,
				"sling.servlet.extensions=json"})
public class CustomerChangePasswordServlet extends SlingAllMethodsServlet{


	private static final long serialVersionUID = 2731083214874663626L;
	
	private static final Logger LOG = LoggerFactory.getLogger(CustomerChangePasswordServlet.class);

	@Reference
	private CustomerService customerService;

	

	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("ChangePasswordServlet doPut()  method start.");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();
		JSONObject jsonResponse = new JSONObject();
		
		try {
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
					JSONObject changePasswordRes = customerService.changePassword(jsonPayload);
					if (changePasswordRes.has("statusCode") && changePasswordRes.getInt("statusCode") == HttpStatus.OK_200) {
						responseObject.put("status", Boolean.TRUE);
						jsonResponse.put("success", "Password Changed.");
					}else if(changePasswordRes.has("statusCode") && changePasswordRes.getInt("statusCode") == HttpStatus.UNAUTHORIZED_401) {
						jsonResponse.put("error", "Current password not matching. Please try again");
					}else if(changePasswordRes.has("statusCode") && changePasswordRes.getInt("statusCode") == HttpStatus.BAD_REQUEST_400) {
						jsonResponse.put("error", "New Password lenth should be 8, one Upper char, One lower char and one digit.");
					} else {
						jsonResponse.put("error", "Something went wrong while change password.");
					}
				}else {
					jsonResponse.put("error", "Missing request paramter.");
				}
			} else {
				jsonResponse.put("error", "invalid payload.");
			}
			responseObject.put("message",jsonResponse);
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.error("Error Occured while executing ChangePassword doPut(). responseObject={} ", responseObject);
			LOG.error("Full Error={} ", e);
		}
		LOG.debug("ChangePassword doPut()  method end.");
	}

	private boolean isValidPayload(JSONObject jsonPayload) {
		boolean isValidData=Boolean.TRUE;
		if(!jsonPayload.has("customerToken") || !jsonPayload.has("custId") || !jsonPayload.has("currentPassword") || !jsonPayload.has("newPassword")){
			isValidData = Boolean.FALSE;
		}
		return isValidData;
	}

	
	
	

}

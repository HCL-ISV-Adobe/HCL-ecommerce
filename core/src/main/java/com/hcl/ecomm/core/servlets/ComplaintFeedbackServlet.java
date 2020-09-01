package com.hcl.ecomm.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
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
				"sling.servlet.paths=/bin/hclecomm/complaintFeedback",
				"sling.servlet.method=" + HttpConstants.METHOD_GET,
				"sling.servlet.extensions=json"})
public class ComplaintFeedbackServlet extends SlingAllMethodsServlet {


	private static final long serialVersionUID = 2731083214874663626L;
	
	private static final Logger LOG = LoggerFactory.getLogger(ComplaintFeedbackServlet.class);

	@Reference
	private CustomerService customerService;

	

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOG.debug("ComplaintFeedbackServlet doGet()  method start.Protocol=="+request.getProtocol());
		LOG.debug("ComplaintFeedbackServlet doGet()  method start.ServerName=="+request.getServerName());
		LOG.debug("ComplaintFeedbackServlet doGet()  method start.Port=="+request.getServerPort());
		LOG.debug("ComplaintFeedbackServlet doGet()  method start.PathInfo=="+request.getPathInfo());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			jsonResponse.put("error", "Request failed");
			responseObject.put("status", Boolean.FALSE);
			
			
			responseObject.put("message",jsonResponse);
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.error("Error Occured while executing ComplaintFeedbackServlet doGet(). responseObject={} ", responseObject);
		}
		LOG.debug("ComplaintFeedbackServlet doGet()  method end.");
	}
}

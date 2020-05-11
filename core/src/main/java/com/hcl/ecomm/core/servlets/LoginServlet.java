package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.LoginService;
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

@Component(
		service = Servlet.class,
		property = {
				"sling.servlet.extensions=json",
				"sling.servlet.paths=/bin/hclecomm/login",
				"sling.servlet.method="	 + HttpConstants.METHOD_GET,			
		}
	)
public class LoginServlet extends SlingSafeMethodsServlet{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);
	
	@Reference
	private LoginService loginService;
	
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		LOG.info("inside doGET method");
		
		LOG.info("loginService : " + loginService);
		
		
		String token = loginService.getToken();		
		response.getWriter().append("Token Value : "+token);
		
		
	}
	
	
}

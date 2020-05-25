package com.hcl.ecomm.core.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
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

import com.hcl.ecomm.core.services.DeleteCartItemService;


@Component(
		service = Servlet.class,
		property = { 
				Constants.SERVICE_DESCRIPTION + "= Delete Cart Item Servlet",
				"sling.servlet.paths=/bin/hclecomm/deletecartitem", 
				"sling.servlet.method=" + HttpConstants.METHOD_PUT,
			    "sling.servlet.extensions=json" })
public class DeleteCartItemServlet extends SlingAllMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7980975418220422865L;

	private static final Logger LOG = LoggerFactory.getLogger(DeleteCartItemServlet.class);

	@Reference
	private DeleteCartItemService deleteCartItemService;

	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
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
				if (isValidItem(jsonPayload)) {
					JSONObject deleteCartItemResponse = deleteCartItemService.deleteCartItem(jsonPayload.getString("cartId"), jsonPayload.getString("itemId"));
					if (deleteCartItemResponse.has("statusCode") && deleteCartItemResponse.getInt("statusCode") == HttpStatus.SC_OK) {
						responseObject.put("message", deleteCartItemResponse.getJSONObject("message"));
						responseObject.put("status", Boolean.TRUE);
					} else {
						responseObject.put("message", "something went wrong while deleted item from cart.");
					}
				}
			} else {
				responseObject.put("message", "Missing Parameter in payload.");
				responseObject.put("status", Boolean.FALSE);
			}
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.info("Error Occured while executing delete item from cart. Full Error={} ", e);
		}
	}
	
	private boolean isValidItem(JSONObject jsonPayload) {
		boolean isValidItem=Boolean.TRUE;
		if(!jsonPayload.has("cartId") && !jsonPayload.has("itemId")){
			isValidItem = Boolean.FALSE;
		}
		return isValidItem;
	}
}

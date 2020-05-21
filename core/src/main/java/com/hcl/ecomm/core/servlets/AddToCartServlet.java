package com.hcl.ecomm.core.servlets;

import javax.servlet.Servlet;

import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.ProductService;
import com.hcl.ecomm.core.utility.ProductUtility;

import org.apache.commons.lang3.StringUtils;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import org.osgi.framework.Constants;

@Component(
		service = Servlet.class, 
		property = { 
				 Constants.SERVICE_DESCRIPTION + "= Add to Cart Servlet",
				"sling.servlet.paths=/bin/hclecomm/addtocart",
				"sling.servlet.method=" + HttpConstants.METHOD_POST,
				"sling.servlet.extensions=json"})
public class AddToCartServlet extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 175460834851290225L;
	private static final Logger LOG = LoggerFactory.getLogger(AddToCartServlet.class);
	
	@Reference
	private AddToCartService cartService;
	
	@Override
	public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
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
				JSONObject jsonObject =  new JSONObject(payload);
				if (StringUtils.isNotEmpty(jsonObject.getString("cartid"))) {
					JSONObject cartItem = createCartItem( jsonObject);
					JSONObject addToCartResponse = cartService.addToCart(cartItem);
					
					responseObject.put("message", addToCartResponse);
					responseObject.put("status", Boolean.TRUE);
				}
			} else {
				responseObject.put("message", "Missing Parameter in payload");
				responseObject.put("status", Boolean.FALSE);
			}
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.info("Error Occured while executing AddToCartServlet Post. Full Error={} ", e);
		}
	}
	
	private boolean isValidProduct(JSONObject product) {
		boolean isValidData=Boolean.FALSE;
		
		return isValidData;
	}
	
	private JSONObject createCartItem(JSONObject product) {
		JSONObject item = new JSONObject();
		JSONObject cartItem = new JSONObject();
		try {
			cartItem.put("quote_id", product.getString("cartid"));
			cartItem.put("sku", product.getString("sku"));
			cartItem.put("qty", product.getInt("qty"));
			item.put("cartItem", cartItem);
		} catch (JSONException e) {
			LOG.error("Error while executing. Error={}",e);
		}
		return item;
	}
}

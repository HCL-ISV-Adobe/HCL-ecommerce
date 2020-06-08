package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.CreateOrderService;
import com.hcl.ecomm.core.services.DeleteCartItemService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Servlet that delete the existing cart item by hitting the Magento service.
 */
@Component(
		service = Servlet.class,
		property = { 
				Constants.SERVICE_DESCRIPTION + "= Create Order Servlet",
				"sling.servlet.paths=/bin/hclecomm/createOrder",
				"sling.servlet.method=" + HttpConstants.METHOD_PUT,
			    "sling.servlet.extensions=json" })
public class CreateOrderServlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = -7980975418220422865L;

	private static final Logger LOG = LoggerFactory.getLogger(CreateOrderServlet.class);

	@Reference
	private CreateOrderService createOrderService;


	@Override
	protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();

		try {
			responseObject.put("message", "Response");
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
				LOG.info("CreateOrder iNfo ()  payload={}",jsonPayload);
				if (isValidItem(jsonPayload)) {
					JSONObject createOrderItem = jsonItemObj( jsonPayload);
					JSONObject createOrderItemResponse = createOrderService.createOrderItem(createOrderItem,jsonPayload.getString("cartId"));
					if (createOrderItemResponse.has("statusCode") && createOrderItemResponse.getInt("statusCode") == HttpStatus.SC_OK) {
						LOG.info("createOrderItemResponse is {}" ,createOrderItemResponse);
						responseObject.put("message", createOrderItemResponse.getJSONObject("message"));
						responseObject.put("status", Boolean.TRUE);
					} else {
						responseObject.put("message", "something went wrong while Order Create from cart.");
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
		if(!jsonPayload.has("cartId")){
			isValidItem = Boolean.FALSE;
		}
		return isValidItem;
	}

	private JSONObject jsonItemObj(JSONObject shipData) {
		JSONObject orderItem = new JSONObject();
		JSONObject orderInfo = new JSONObject();
		try {
			orderItem.put("method",shipData.getString("code"));
			orderInfo.put("paymentMethod",orderItem);

		} catch (JSONException e) {
			LOG.error("Error while executing. Error={}",e);
		}


		return  orderInfo;
	}
}

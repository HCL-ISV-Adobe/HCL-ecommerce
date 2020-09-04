package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.CreateOrderService;
import com.hcl.ecomm.core.services.DeleteCartItemService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
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
 * Servlet that create order using shipping information by hitting the Magento service.
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
	private String NOT_AVAILABLE="NA";
	private String DEFAULT_MAIL="defaultstore@pickup.hcl.com";

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
			String customerToken = request.getHeader("CustomerToken");
			if (StringUtils.isNotEmpty(payload)) {
				JSONObject jsonPayload =  new JSONObject(payload);
				LOG.info("CreateOrder iNfo ()  payload={}",jsonPayload);
				if (isValidItem(jsonPayload)) {
					JSONObject createOrderItem = jsonItemObj( jsonPayload,customerToken);
					JSONObject createOrderItemResponse = getCreateOrderItem(createOrderItem, jsonPayload, customerToken, jsonPayload.get("delivercharges").toString(), jsonPayload.get("coupondiscount").toString(), jsonPayload.getJSONObject("storeAddress"));
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
			LOG.error("Error Occured while executing create order using shipinfo. Full Error={} ", e);
		}
	}
	public JSONObject getCreateOrderItem(JSONObject createOrderItem, JSONObject jsonPayload, String customerToken, String deliverCharges, String couponDiscount, JSONObject storeAddress) throws JSONException {
		return createOrderService.createOrderItem(createOrderItem, jsonPayload.getString("cartId"), customerToken, deliverCharges, couponDiscount, storeAddress);
	}

	private boolean isValidItem(JSONObject jsonPayload) {
		boolean isValidItem=Boolean.TRUE;
		if(!jsonPayload.has("cartId")){
			isValidItem = Boolean.FALSE;
		}
		return isValidItem;
	}

	private JSONObject jsonItemObj(JSONObject shipData, String customerToken) {
		JSONObject orderItem = new JSONObject();
		JSONObject orderInfo = new JSONObject();
		JSONObject billAddress = new JSONObject();
		try {
			if(customerToken != null && !customerToken.isEmpty()) {
				if(shipData.getString("email").equalsIgnoreCase(NOT_AVAILABLE) || shipData.getString("email").isEmpty() ){
					billAddress.put("email", DEFAULT_MAIL);
				}else{
					billAddress.put("email", shipData.getString("email"));
				}
				billAddress.put("city", shipData.getString("city"));
				billAddress.put("country_id", shipData.getString("country_id"));
				billAddress.put("firstname", shipData.getString("firstname"));
				billAddress.put("lastname", shipData.getString("lastname"));
				billAddress.put("postcode", shipData.getString("postcode"));
				billAddress.put("region", shipData.getString("region"));
				billAddress.put("region_code", shipData.getString("region_code"));
				billAddress.put("region_id", shipData.getInt("region_id"));
				billAddress.put("street", shipData.get("street"));
				billAddress.put("telephone", shipData.getString("telephone"));
				orderItem.put("method",shipData.getString("code"));
				orderInfo.put("billing_address",billAddress);
			}
			else
			{
				orderItem.put("method",shipData.getString("code"));
			}
			orderInfo.put("paymentMethod",orderItem);

		} catch (JSONException e) {
			LOG.error("Error while executing. Error={}",e);
		}


		return  orderInfo;
	}
}

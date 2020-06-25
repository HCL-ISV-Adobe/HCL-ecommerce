package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.ShippingInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;


@Component(
		service = Servlet.class, 
		property = { 
				 Constants.SERVICE_DESCRIPTION + "= Shipping Info Servlet",
				"sling.servlet.paths=/bin/hclecomm/shipinfo",
				"sling.servlet.method=" + HttpConstants.METHOD_POST,
				"sling.servlet.extensions=json"})
public class ShippingInfo extends SlingAllMethodsServlet{

	private static final long serialVersionUID = 175460834851290225L;
	private static final Logger LOG = LoggerFactory.getLogger(ShippingInfo.class);
	
	@Reference
	private ShippingInfoService shippingInfoService;
	

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		LOG.debug("Shipping INfo  doPost()  method start.");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		JSONObject responseObject = new JSONObject();
		try {
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
				LOG.info("Shipping iNfo ()  payload={}",jsonPayload);
				if (isValidPayload(jsonPayload,customerToken )) {
					JSONObject shipItem = jsonItemObj( jsonPayload);
					JSONObject shippingInfoResponse = shippingInfoService.createShipInfo(shipItem,jsonPayload.getString("cartId"), customerToken);
					if (shippingInfoResponse.has("statusCode") && shippingInfoResponse.getInt("statusCode") == HttpStatus.OK_200) {
						LOG.info("shippingInfoResponse is {}" ,shippingInfoResponse);
						JSONObject message= shippingInfoResponse.getJSONObject("message");
						JSONArray paymentMethods= message.getJSONArray("payment_methods");
						JSONObject paymentobj = new JSONObject();
						paymentobj.put("code",paymentMethods.getJSONObject(0).get("code"));
						responseObject.put("payment_methods", paymentobj);
						responseObject.put("status", Boolean.TRUE);
					} else {
						responseObject.put("message", "something went wrong while Shipping Info.");
					}
				}
			} else {
				responseObject.put("message", "Missing Parameter in payload.");
				responseObject.put("status", Boolean.FALSE);
			}
			response.getWriter().print(responseObject);
		} catch (Exception e) {
			LOG.error("Error Occured while executing Shipping Info Servlet={}", responseObject);
			LOG.error("Full Error={} ", e);
		}
		LOG.debug("shipping info doPost()  method end.");
	}
	
	
	private boolean isValidPayload(JSONObject jsonPayload, String customerToken) {
		boolean isValidData=Boolean.TRUE;
		if(!jsonPayload.has("cartId") && (null==customerToken || customerToken=="")){
			isValidData = Boolean.FALSE;
		}
		return isValidData;
	}
	
	private JSONObject jsonItemObj(JSONObject shipData) {
		JSONObject shipInfo = new JSONObject();
		JSONObject addressInfo = new JSONObject();
		//JSONObject shippAddress = new JSONObject();
		JSONObject billAddress = new JSONObject();
		try {

			billAddress.put("city", shipData.getString("city"));
			billAddress.put("country_id", shipData.getString("country_id"));
			billAddress.put("email", shipData.getString("email"));
			billAddress.put("firstname", shipData.getString("firstname"));
			billAddress.put("lastname", shipData.getString("lastname"));
			billAddress.put("postcode", shipData.getString("postcode"));
			billAddress.put("region", shipData.getString("region"));
			billAddress.put("region_code", shipData.getString("region_code"));
			billAddress.put("region_id", shipData.getInt("region_id"));
			billAddress.put("street", shipData.get("street"));
			billAddress.put("telephone", shipData.getString("telephone"));
			addressInfo.put("shippingAddress",billAddress);
			addressInfo.put("billingAddress",billAddress);
			addressInfo.put("shipping_method_code",shipData.getString("shipping_method_code"));
			addressInfo.put("shipping_carrier_code",shipData.getString("shipping_carrier_code"));
			shipInfo.put("addressInformation",addressInfo);
		} catch (JSONException e) {
			LOG.error("Error while executing. Error={}",e);
		}
		return shipInfo;
	}


}

package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.ShippingInfoService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/pickupstorelist",
		"sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class PickupStoreServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4016057296495129474L;
	private static final Logger LOG = LoggerFactory.getLogger(PickupStoreServlet.class);

	@Reference
	private ShippingInfoService shippingInfoService;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		LOG.debug("inside PickupStore Servlet  doGET method");
		try {

			JSONObject respObj = shippingInfoService.getPickupStoreList();
			JSONArray itemsArr  = respObj.getJSONArray("items");
			LOG.debug("itemsArr is {}", itemsArr.toString());
			JSONArray pickupStoreRes = new JSONArray();
			for(int i= 0 ;i<itemsArr.length(); i++){

				JSONObject locationList = new JSONObject();
				locationList.put("pickup_location_code",itemsArr.getJSONObject(i).get("pickup_location_code"));
				locationList.put("name",itemsArr.getJSONObject(i).get("name"));
				locationList.put("email",itemsArr.getJSONObject(i).get("email"));
				locationList.put("contact_name",itemsArr.getJSONObject(i).get("contact_name"));
				locationList.put("description",itemsArr.getJSONObject(i).get("description"));
				locationList.put("region",itemsArr.getJSONObject(i).get("region"));
				locationList.put("city",itemsArr.getJSONObject(i).get("city"));
				locationList.put("street",itemsArr.getJSONObject(i).get("street"));
				locationList.put("postcode",itemsArr.getJSONObject(i).get("postcode"));
				locationList.put("phone",itemsArr.getJSONObject(i).get("phone"));
				pickupStoreRes.put(locationList);

				}
			response.setContentType("application/json");
			response.getWriter().write(pickupStoreRes.toString());

		} catch (Exception e) {
			LOG.error(" Error while while getting Pick Up Store  list. Error={}", e);
		}


	}


}
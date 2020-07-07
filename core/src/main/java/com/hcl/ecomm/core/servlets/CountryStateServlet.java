package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.CartService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/countrystatelist",
		"sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class CountryStateServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4016057296495129474L;
	private static final Logger LOG = LoggerFactory.getLogger(CountryStateServlet.class);

	@Reference
	private ShippingInfoService shippingInfoService;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		LOG.info("inside CountryState Servlet  doGET method");
		try {

			JSONArray responseStream = shippingInfoService.getStateCountryList();
			LOG.info("responseStream is {}", responseStream.toString());

			JSONArray countryStateRes = new JSONArray();
			for(int i= 0 ;i<responseStream.length(); i++){

				JSONObject countryList = new JSONObject();
				countryList.put("name",responseStream.getJSONObject(i).get("name"));
				countryList.put("country_id",responseStream.getJSONObject(i).get("iso2"));
				countryList.put("phone_code",responseStream.getJSONObject(i).get("phone_code"));
				JSONArray statesList = new JSONArray();
				JSONObject stateList = new JSONObject();
				try {
					if(responseStream.getJSONObject(i).getJSONArray("states").length() != 0) {

						for (int j = 0; j < responseStream.getJSONObject(i).getJSONArray("states").length(); j++) {
							JSONObject stateListObj = new JSONObject();
							stateListObj.put("name", responseStream.getJSONObject(i).getJSONArray("states").getJSONObject(j).get("name"));
							stateListObj.put("region_code", responseStream.getJSONObject(i).getJSONArray("states").getJSONObject(j).get("state_code"));
							statesList.put(stateListObj);

						}
						countryList.put("states", statesList);
						countryStateRes.put(countryList);
					}
					else{
						stateList.put("name", responseStream.getJSONObject(i).get("name"));
						stateList.put("region_code", responseStream.getJSONObject(i).get("iso2"));
						statesList.put(stateList);
						countryList.put("states", statesList);
						countryStateRes.put(countryList);
					}


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			response.setContentType("application/json");
			response.getWriter().write(countryStateRes.toString());

		} catch (Exception e) {
			LOG.error(" Error while while getting country State list. Error={}", e);
		}


	}


}
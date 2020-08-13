package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.CartService;
import com.hcl.ecomm.core.services.ProductService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
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

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/cartproducts",
		"sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class CartItemServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4016057296495129474L;
	private static final Logger LOG = LoggerFactory.getLogger(CartItemServlet.class);

	@Reference
	CartService cartService;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		LOG.info("inside ProductServlet doGET method");
		try {
			String customerToken = request.getHeader("CustomerToken");
			String cartItems = null;
			String cartId = request.getParameter("cartId");
			JSONArray responseStream = getCartItemsDetails(cartId, customerToken);
			LOG.info("responseStream is {}", responseStream.toString());

			List<HashMap<String, Object>> list = new ArrayList<>();
			JSONArray cartArray=null;

			for(int i=0; i<responseStream.length();i++){
				HashMap<String, Object> productMap = new HashMap<String, Object>();
				JSONObject cartObj = responseStream.getJSONObject(i);
				LOG.info("cartObj is {}", cartObj);
				productMap.put("item_id",responseStream.getJSONObject(i).get("item_id"));
				productMap.put("sku", responseStream.getJSONObject(i).get("sku").toString());
				productMap.put("qty",responseStream.getJSONObject(i).get("qty"));
				productMap.put("name", responseStream.getJSONObject(i).get("name").toString());
				productMap.put("price",responseStream.getJSONObject(i).get("price"));
				productMap.put("quote_id",responseStream.getJSONObject(i).get("quote_id").toString());
				productMap.put("image_url", checkNullString(responseStream.getJSONObject(i).getJSONObject("extension_attributes").get("image_url").toString()));
				//productMap.put("image_url", "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png");
				list.add(productMap);
				cartArray = new JSONArray(list);
			}

			response.setContentType("application/json");
			response.getWriter().write(cartArray.toString());
			response.setStatus(200);
		}
		catch (Exception e){
			LOG.error("error in product servlet {} ",e.getMessage());
			//response.setStatus(500);
		}
	}
	public JSONArray getCartItemsDetails(String cartId,String customerToken){
		return cartService.getCartItemsDetails(cartId, customerToken);
	}
		public String checkNullString(String value) {
		LOG.debug("value :::::: {}", value);
		//return value.equals("null") ? "http://127.0.0.1/magento2/pub/media/catalog/product\\\\cache\\\\84e3ec616dfeead44f09ae682858fa68\\\\//t/h/thumnail-adi-tshirt-black.jpg" : value;
		return value.contains("placeholder")?"https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png" : value;
	}

}
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

			String cartItems = null;
			String cartId = request.getParameter("cartId");
			JsonArray responseStream = cartService.getCartItemsDetails(cartId);
			LOG.info("responseStream is {}", responseStream.toString());

			JsonArray itemsarr= responseStream.getAsJsonArray();

			List<HashMap<String, Object>> list = new ArrayList<>();
			JsonArray cartArray=new JsonArray();

			for(int i=0; i<itemsarr.size();i++){
				HashMap<String, Object> productMap = new HashMap<String, Object>();
				JsonObject cartObj = itemsarr.get(i).getAsJsonObject();
				LOG.info("cartObj is {}", cartObj);
				productMap.put("item_id",itemsarr.get(i).getAsJsonObject().get("item_id").getAsInt());
				productMap.put("sku", itemsarr.get(i).getAsJsonObject().get("sku").getAsString());
				productMap.put("qty",itemsarr.get(i).getAsJsonObject().get("qty").getAsInt());
				productMap.put("name", itemsarr.get(i).getAsJsonObject().get("name").getAsString());
				productMap.put("price",itemsarr.get(i).getAsJsonObject().get("price").getAsInt());
				productMap.put("quote_id",itemsarr.get(i).getAsJsonObject().get("quote_id").getAsString());
				//productMap.put("image_url", checkNullString(itemsarr.get(i).getAsJsonObject().get("extension_attributes").getAsJsonObject().get("image_url").getAsString()));
				productMap.put("image_url", "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png");
				list.add(productMap);
				 cartArray = new Gson().toJsonTree(list).getAsJsonArray();


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
		
		/*public String checkNullString(String value) {
		LOG.debug("value :::::: {}", value);
		//return value.equals("null") ? "http://127.0.0.1/magento2/pub/media/catalog/product\\\\cache\\\\84e3ec616dfeead44f09ae682858fa68\\\\//t/h/thumnail-adi-tshirt-black.jpg" : value;
		return value.contains("placeholder")?"http://127.0.0.1/magento2/pub/media/catalog/product\\\\cache\\\\84e3ec616dfeead44f09ae682858fa68\\\\//t/h/thumnail-adi-tshirt-black.jpg" : value;
	}*/


}
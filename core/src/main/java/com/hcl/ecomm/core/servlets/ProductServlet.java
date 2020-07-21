package com.hcl.ecomm.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.ProductService;
import com.hcl.ecomm.core.utility.ProductUtility;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/products",
		"sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class ProductServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 4016057296495129474L;
	private static final Logger LOG = LoggerFactory.getLogger(ProductServlet.class);

	@Reference
	ProductService productService;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		
		try {

			
			JsonArray responseStream = productService.getAllProductDetails();
			JsonArray itemsarr= responseStream.getAsJsonArray();
			LOG.info("itemsarr is {}", itemsarr.toString());
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			
			for( JsonElement j_el : itemsarr){
				HashMap<String, String> productMap = new HashMap<String, String>();
				String id = j_el.getAsJsonObject().get("id") !=null ? j_el.getAsJsonObject().get("id").getAsString() : "";
				String sku = j_el.getAsJsonObject().get("sku") !=null ? j_el.getAsJsonObject().get("sku").getAsString() : "";
				String name = j_el.getAsJsonObject().get("name") != null ? j_el.getAsJsonObject().get("name").getAsString() : "" ;
				String price = j_el.getAsJsonObject().get("price") != null ? j_el.getAsJsonObject().get("price").getAsString() : "0.0" ;
				
				productMap.put("id", id);
				productMap.put("sku", sku);
				productMap.put("name", name);
				productMap.put("price", price);
				list.add(productMap);
				LOG.info("Array list is {}",list.get(i).toString());

			}

			response.getWriter().write(list.toString());
		}
		catch (Exception e){
			LOG.error("error in product servlet {} ",e.getMessage());

		}

	}

	
}
package com.hcl.ecomm.core.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.ProductService;
import com.hcl.ecomm.core.utility.ProductUtility;
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
			JsonObject productObject = itemsarr.get(0).getAsJsonObject();
			LOG.info("productobject is {}", productObject);
			for(int i=0; i<itemsarr.size();i++){
				HashMap<String, String> productMap = new HashMap<String, String>();
				productMap.put("id",itemsarr.get(i).getAsJsonObject().get("id").getAsString());
				productMap.put("sku",itemsarr.get(i).getAsJsonObject().get("sku").getAsString());
				productMap.put("name",itemsarr.get(i).getAsJsonObject().get("name").getAsString());
				productMap.put("price",itemsarr.get(i).getAsJsonObject().get("price").getAsString());
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
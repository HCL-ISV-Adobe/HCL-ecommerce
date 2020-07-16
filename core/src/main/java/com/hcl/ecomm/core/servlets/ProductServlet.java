package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.ProductService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
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
			JSONArray responseStream = getAllProductDetails();
			LOG.info("itemsarr is {}", responseStream.toString());
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			for(int i=0; i<responseStream.length();i++){
				HashMap<String, String> productMap = new HashMap<String, String>();
				productMap.put("id",responseStream.getJSONObject(i).get("id").toString());
				productMap.put("sku",responseStream.getJSONObject(i).get("sku").toString());
				productMap.put("name",responseStream.getJSONObject(i).get("name").toString());
				productMap.put("price",responseStream.getJSONObject(i).get("price").toString());
				list.add(productMap);
				LOG.info("Array list is {}",list.get(i).toString());

			}

			response.getWriter().write(list.toString());
		}
		catch (Exception e){
			LOG.error("error in product servlet {} ",e.getMessage());

		}


	}
	public JSONArray getAllProductDetails() throws JSONException {
		return productService.getAllProductDetails();
	}
	
}
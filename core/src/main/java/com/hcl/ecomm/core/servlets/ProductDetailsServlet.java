package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
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

@Component(service = Servlet.class,
        property = { "sling.servlet.paths=/bin/hclecomm/productDetails",
        "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class ProductDetailsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 849553733784661541L;
    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsServlet.class);

    @Reference
    ProductService productService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        LOG.debug("Inside  ProductDetailsServlet doGet Method");

        try {
         
		String  sku = request.getParameter("sku");
        if(StringUtils.isNotEmpty(sku)) {
		
        JSONObject productResponse = getProductDetail(sku);
        LOG.info(" productResponse is {}", productResponse.toString());
        List<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> productMap = new HashMap<String, String>();
        productMap.put("sku", productResponse.get("sku").toString());
        productMap.put("name", productResponse.get("name").toString());
        productMap.put("price", productResponse.get("price").toString());
        productMap.put("stock", productResponse.getJSONObject("extension_attributes").getJSONObject("stock_item").get("is_in_stock").toString());
        productMap.put("qty", productResponse.getJSONObject("extension_attributes").getJSONObject("stock_item").get("qty").toString());
        productList.add(productMap);
        LOG.debug("ProductDetails  list is {}",productList.toString());

            String productDetailsJson = productList.toString();


        response.setContentType("application/json");
        response.getWriter().write(productDetailsJson);
		 }

        else{
            String productSku= "passing empty  sku parameter";
            response.getWriter().write(productSku);
        }

        }
        catch (Exception e){
            LOG.error("error in ProductDetailsServlet {} ",e.getMessage());
        }
    }
    public JSONObject getProductDetail(String sku){
        return productService.getProductDetail(sku);
    }
}


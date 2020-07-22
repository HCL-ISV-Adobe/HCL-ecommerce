package com.hcl.ecomm.core.servlets;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hcl.ecomm.core.services.ProductService;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Objects;

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
		JsonArray responseStream = productService.getProductDetail(sku);
        JsonObject productResponse = responseStream.getAsJsonArray().get(0).getAsJsonObject();
        LOG.info(" productResponse is {}", productResponse.toString());
        List<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> productMap = new HashMap<String, String>();
        String skuId = Objects.nonNull(productResponse.get("sku")) ? productResponse.get("sku").getAsString() : "";
        String name = Objects.nonNull(productResponse.get("name")) ? productResponse.get("name").getAsString() : "";
        String price = Objects.nonNull(productResponse.get("price")) ? productResponse.get("price").getAsString() : "0.0";
        String stock = "false";
        String qty = "0";
        JsonElement stock_item = productResponse.get("extension_attributes").getAsJsonObject().get("stock_item");
        if(stock_item != null){
            if(stock_item.getAsJsonObject().get("is_in_stock") != null){
                stock = stock_item.getAsJsonObject().get("is_in_stock").getAsString();
            }
            if(stock_item.getAsJsonObject().get("qty") != null){
                qty = stock_item.getAsJsonObject().get("qty").getAsString();
            }
        }
        productMap.put("sku", skuId);
        productMap.put("name", name);
        productMap.put("price", price);
        productMap.put("stock", stock);
        productMap.put("qty", qty);
        productMap.put("related_products_sku",getRelatedProductSkus(productResponse.get("product_links").getAsJsonArray()).toString());
		productList.add(productMap);
        LOG.debug("ProductDetails  list is {}",productList.toString());

        String productDetailsJson = new Gson().toJson(productList);


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
	
	private List<String> getRelatedProductSkus(JsonArray relatedProductArray){
        List<String> relatedProductSkuList = new ArrayList<>();

        for (int i = 0; i < relatedProductArray.size(); i++) {
            String productSku = relatedProductArray.get(i).getAsJsonObject().get("linked_product_sku").getAsString();
            relatedProductSkuList.add(productSku);
        }
        return relatedProductSkuList;
    }
}

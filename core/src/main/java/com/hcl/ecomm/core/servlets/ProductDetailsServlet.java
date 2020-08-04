package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.ProductService;
import org.apache.commons.lang3.StringUtils;
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
                JSONArray responseStream = getProductDetail(sku);
                LOG.info("JsonResponse : "+responseStream);
                List<HashMap<String, String>> productList = new ArrayList<>();
                JSONObject productObject = new JSONObject();

                String skuId = "";
                String name = "";
                String price = "0.0";
                String stock = "false";
                String qty = "0";
                JSONArray related_products_sku = new JSONArray();

                if(responseStream.length()>0){
                    JSONObject productResponse = responseStream.getJSONObject(0);
                    LOG.info(" productResponse is {}", productResponse.toString());

                    if(productResponse.has("sku") && productResponse.has("name") && productResponse.has("price")){
                        skuId = productResponse.get("sku").toString();
                        name = productResponse.get("name").toString();
                        price = productResponse.get("price").toString();
                    }

                    if(productResponse.getJSONObject("extension_attributes").has("stock_item")){
                        JSONObject stock_item=productResponse.getJSONObject("extension_attributes").getJSONObject("stock_item");
                        if(stock_item.has("is_in_stock")){
                            stock = stock_item.get("is_in_stock").toString();
                        }
                        if(stock_item.has("qty")){
                            qty = stock_item.get("qty").toString();
                        }
                    }


                    if(productResponse.has("product_links")){
                        related_products_sku = getRelatedProductSkus(productResponse.getJSONArray("product_links"));
                    }
                }

                productObject.put("sku", skuId);
                productObject.put("name", name);
                productObject.put("price", price);
                productObject.put("stock", stock);
                productObject.put("qty", qty);
                productObject.put("related_products_sku",related_products_sku);
                response.setContentType("application/json");
                response.getWriter().print(productObject);
            }
            else{
                String productSku= "passing empty  sku parameter";
                response.getWriter().print(productSku);
            }

        }
        catch (Exception e){
            LOG.error("error in ProductDetailsServlet {} ",e.getMessage());
        }
    }

    private JSONArray getRelatedProductSkus(JSONArray relatedProductArray) throws JSONException {
        JSONArray relatedProductSkuArray = new JSONArray();
        for (int i = 0; i < relatedProductArray.length(); i++) {
            String productSku = relatedProductArray.getJSONObject(i).get("linked_product_sku").toString();
            relatedProductSkuArray.put(productSku);
        }
        return relatedProductSkuArray;
    }
    public JSONArray getProductDetail(String sku){
        return  productService.getProductDetail(sku);
    }
}

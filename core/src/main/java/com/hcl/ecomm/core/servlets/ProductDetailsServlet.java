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
                List<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> productMap = new HashMap<String, String>();

                String skuId = "";
                String name = "";
                String price = "0.0";
                String stock = "false";
                String qty = "0";
                String related_products_sku = "";

                if(responseStream.length()>0){
                    JSONObject productResponse = responseStream.getJSONObject(0);
                    LOG.info(" productResponse is {}", productResponse.toString());

                    if(productResponse.get("sku") != null){
                        skuId = productResponse.get("sku").toString();
                    }
                    if(productResponse.get("name") != null){
                        name = productResponse.get("name").toString();
                    }
                    if(productResponse.get("price") != null){
                        price = productResponse.get("price").toString();
                    }

                    JSONObject stock_item=productResponse.getJSONObject("extension_attributes").getJSONObject("stock_item");
                    if(stock_item != null){
                        if(stock_item.get("is_in_stock") != null){
                            stock = stock_item.get("is_in_stock").toString();
                        }
                        if(stock_item.get("qty") != null ){
                            qty = stock_item.get("qty").toString();
                        }
                    }

                    if(productResponse.getJSONArray("product_links") != null){
                        related_products_sku = getRelatedProductSkus(productResponse.getJSONArray("product_links")).toString();
                    }
                }

                productMap.put("sku", skuId);
                productMap.put("name", name);
                productMap.put("price", price);
                productMap.put("stock", stock);
                productMap.put("qty", qty);
                productMap.put("related_products_sku",related_products_sku);
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

    private List<String> getRelatedProductSkus(JSONArray relatedProductArray) throws JSONException {
        List<String> relatedProductSkuList = new ArrayList<>();

        for (int i = 0; i < relatedProductArray.length(); i++) {
            String productSku = relatedProductArray.getJSONObject(i).get("linked_product_sku").toString();
            relatedProductSkuList.add(productSku);
        }
        return relatedProductSkuList;
    }
    public JSONArray getProductDetail(String sku){
        return  productService.getProductDetail(sku);
    }
}

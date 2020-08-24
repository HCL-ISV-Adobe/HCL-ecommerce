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

@Component(service = Servlet.class,
        property = {"sling.servlet.paths=/bin/hclecomm/productDetails",
                "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json"})
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

            String sku = request.getParameter("sku");
            if (StringUtils.isNotEmpty(sku)) {
                JSONObject productObject = new JSONObject();

                JSONObject productResponse = getProductDetail(sku);
                if (productResponse.length() == 0) {
                    response.getWriter().print("Product is not available in required website id");
                    return;
                }
                LOG.info("JsonResponse : " + productResponse);

                String stock = "false";
                String qty = "0";
                JSONArray related_products_sku = new JSONArray();
                JSONArray products_variations= new JSONArray();

                if (productResponse.getJSONObject("extension_attributes").has("stock_item")) {
                    JSONObject stock_item = productResponse.getJSONObject("extension_attributes").getJSONObject("stock_item");
                    if (stock_item.has("is_in_stock")) {
                        stock = stock_item.get("is_in_stock").toString();
                    }
                    if (stock_item.has("qty")) {
                        qty = stock_item.get("qty").toString();
                    }
                }
                if (productResponse.has("product_links")) {
                    related_products_sku = getRelatedProductSkus(productResponse.getJSONArray("product_links"));
                }
                if(productResponse.has("options")) {
                    if (productResponse.getJSONArray("options").length() != 0) {
                        products_variations = getRelatedProductVariation(productResponse.getJSONArray("options").getJSONObject(0).getJSONArray("values"));
                    } else {
                        JSONObject jo = new JSONObject();
                        jo.put("message", "Product Size and Price variations are not configured in magento");
                        products_variations.put(jo);
                    }
                }

                productObject.put("sku", productResponse.get("sku").toString());
                productObject.put("name", productResponse.get("name").toString());
                productObject.put("price", productResponse.get("price").toString());
                productObject.put("stock", stock);
                productObject.put("qty", qty);
                productObject.put("related_products_sku", related_products_sku);
                productObject.put("products_variations", products_variations);
                response.setContentType("application/json");
                response.getWriter().print(productObject);
            } else {
                String productSku = "passing empty sku parameter";
                response.getWriter().print(productSku);
            }

        } catch (Exception e) {
            LOG.error("error in ProductDetailsServlet {} ", e.getMessage());
        }
    }

    private JSONArray getRelatedProductVariation(JSONArray ProductOptionsArray)throws JSONException {
        JSONArray ProductVariationArray = new JSONArray();
        for (int i = 0; i < ProductOptionsArray.length(); i++) {
            JSONObject responseObject = new JSONObject();
            responseObject.put("Size",ProductOptionsArray.getJSONObject(i).get("title").toString());
            responseObject.put("Price",ProductOptionsArray.getJSONObject(i).get("price").toString());
            ProductVariationArray.put(responseObject);
        }
        return ProductVariationArray;
    }

    private JSONArray getRelatedProductSkus(JSONArray relatedProductArray) throws JSONException {
        JSONArray relatedProductSkuArray = new JSONArray();
        for (int i = 0; i < relatedProductArray.length(); i++) {
            String productSku = relatedProductArray.getJSONObject(i).get("linked_product_sku").toString();
            relatedProductSkuArray.put(productSku);
        }
        return relatedProductSkuArray;
    }

    public JSONObject getProductDetail(String sku) {
        return productService.getProductDetail(sku);
    }
}

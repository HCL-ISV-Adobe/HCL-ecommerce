package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;

@Component(service = Servlet.class,
        property = {"sling.servlet.paths=/bin/hclecomm/addToCompare",
                "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json"})
public class AddToCompareServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 7284054774851532818L;
    private static final Logger LOG = LoggerFactory.getLogger(ProductDetailsServlet.class);

    @Reference
    ProductService productService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws  IOException {

        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            if (StringUtils.isNotEmpty(payload)) {
                JSONObject jsonPayload =  new JSONObject(payload);
            String sku1 = jsonPayload.get("sku1").toString();
            String sku2 = jsonPayload.get("sku2").toString();
            if (StringUtils.isNotEmpty(sku1) && StringUtils.isNotEmpty(sku2)) {
                JSONObject productCompareObject1 = new JSONObject();
                JSONObject productCompareObject2 = new JSONObject();
                JSONArray productCompareArray = new JSONArray();

                JSONObject productResponse1 = productService.getProductDetail(sku1);
                JSONObject productResponse2 = productService.getProductDetail(sku2);
                if (productResponse1.length() == 0 || productResponse2.length() == 0) {
                    response.getWriter().print("Products is not available in required website id");
                    return;
                }
                LOG.info("JsonResponse1 : " + productResponse1);
                LOG.info("JsonResponse2 : " + productResponse2);

                String stock1 = "false";
                String stock2 = "false";

                if (productResponse1.getJSONObject("extension_attributes").has("stock_item") && productResponse2.getJSONObject("extension_attributes").has("stock_item")) {
                    JSONObject stock_item1 = productResponse1.getJSONObject("extension_attributes").getJSONObject("stock_item");
                    JSONObject stock_item2 = productResponse1.getJSONObject("extension_attributes").getJSONObject("stock_item");
                    if (stock_item1.has("is_in_stock") && stock_item2.has("is_in_stock")) {
                        stock1 = stock_item1.get("is_in_stock").toString();
                        stock2 = stock_item2.get("is_in_stock").toString();
                    }


                    productCompareObject1.put("sku", productResponse1.get("sku").toString());
                    productCompareObject2.put("sku", productResponse2.get("sku").toString());
                    productCompareObject1.put("name", productResponse1.get("name").toString());
                    productCompareObject2.put("name", productResponse2.get("name").toString());
                    productCompareObject1.put("price", productResponse1.get("price").toString());
                    productCompareObject2.put("price", productResponse2.get("price").toString());
                    productCompareObject1.put("stock", stock1);
                    productCompareObject2.put("stock", stock2);
                    productCompareArray.put(productCompareObject1);
                    productCompareArray.put(productCompareObject2);

                    response.setContentType("application/json");
                    response.getWriter().print(productCompareArray);
                } else {
                    String productSku = "passing empty sku parameter";
                    response.getWriter().print(productSku);
                }
            }
        }
    } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

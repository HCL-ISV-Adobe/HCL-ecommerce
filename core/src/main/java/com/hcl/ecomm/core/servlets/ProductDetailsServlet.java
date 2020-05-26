package com.hcl.ecomm.core.servlets;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
        LOG.info("Inside  ProductDetailsServlet doGet Method");

        try {
         
		String  sku = request.getParameter("sku");
		
        JsonObject productResponse = productService.getProductDetail(sku);
        LOG.info(" productResponse is {}", productResponse.toString());
        List<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> productMap = new HashMap<String, String>();
        productMap.put("sku", productResponse.get("sku").getAsString());
        productMap.put("name", productResponse.get("name").getAsString());
        productMap.put("price", productResponse.get("price").getAsString());
        productMap.put("stock", productResponse.get("extension_attributes").getAsJsonObject().get("stock_item").getAsJsonObject().get("is_in_stock").getAsString());
        productMap.put("qty",  productResponse.get("extension_attributes").getAsJsonObject().get("stock_item").getAsJsonObject().get("qty").getAsString());
        productList.add(productMap);
        LOG.info("ProductDetails  list is {}",productList.toString());

        String productDetailsJson = new Gson().toJson(productList);


        response.setContentType("application/json");
        response.getWriter().write(productDetailsJson);

        }
        catch (Exception e){
            LOG.error("error in ProductDetailsServlet {} ",e.getMessage());
        }
    }
}

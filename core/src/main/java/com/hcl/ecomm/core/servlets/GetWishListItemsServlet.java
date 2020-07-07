package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/getWishList",
        "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class GetWishListItemsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(GetWishListItemsServlet.class);

    @Reference
    private com.hcl.ecomm.core.services.WishListService WishListService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String customerToken = request.getHeader("CustomerToken");
            JsonObject responseStream = WishListService.getWishListItems(customerToken);
            LOG.info("responseStream is {}", responseStream.toString());

            JsonArray itemsarr= responseStream.get("items").getAsJsonArray();
            List<HashMap<String, Object>> list = new ArrayList<>();
            JsonArray wishlistArray=new JsonArray();

            for(int i=0; i<itemsarr.size();i++){
                HashMap<String, Object> itemdetails = new HashMap<String, Object>();
                itemdetails.put("item_id",itemsarr.get(i).getAsJsonObject().get("id").getAsInt());
                itemdetails.put("sku", itemsarr.get(i).getAsJsonObject().get("product").getAsJsonObject().get("sku").getAsString());
                itemdetails.put("name", itemsarr.get(i).getAsJsonObject().get("product").getAsJsonObject().get("name").getAsString());
                itemdetails.put("price",itemsarr.get(i).getAsJsonObject().get("product").getAsJsonObject().get("price").getAsInt());
                itemdetails.put("image_url", "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png");
                list.add(itemdetails);
                wishlistArray = new Gson().toJsonTree(list).getAsJsonArray();

            }
            response.getWriter().write(wishlistArray.toString());
          

        }
        catch (Exception e){
            LOG.error("error in GetWishListItemsServlet {} ",e.getMessage());
        }

    }
}

package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.WishListService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/getWishList",
        "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class GetWishListItemsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(GetWishListItemsServlet.class);

    @Reference
    private WishListService wishListService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

         try {
            String customerToken = request.getHeader("CustomerToken");
            JSONObject responseStream = getWishListItems(customerToken);
             LOG.debug("responseStream is {}", responseStream.toString());
             JSONArray wishlistitems= responseStream.getJSONArray("items");
            List<HashMap<String, Object>> list = new ArrayList<>();
            JSONArray wishlistArray=null;
            
            if(wishlistitems!=null) {
             for(int i=0; i<wishlistitems.length();i++) {
                HashMap<String, Object> itemdetails = new HashMap<String, Object>();
                JSONObject Obj = wishlistitems.getJSONObject(i);
                LOG.info("wishlistitems is {}", Obj);
                itemdetails.put("item_id", Obj.get("item_id"));
                itemdetails.put("sku", Obj.get("sku"));
                itemdetails.put("name", Obj.get("name"));
                itemdetails.put("price", Obj.get("price"));
                itemdetails.put("image_url", "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png");
                list.add(itemdetails);
                wishlistArray = new JSONArray(list);
             }
            }
             response.setContentType("application/json");
             response.getWriter().write(wishlistArray.toString());
             response.setStatus(200);
          

        }
        catch (Exception e){
            LOG.error("error in GetWishListItemsServlet {} ",e.getMessage());
        }

    }
    public JSONObject getWishListItems(String customerToken) throws JSONException {
        //wishListService.getWishListItems() returns jsonObject, to convert jsonObject to JSONObject
        JSONObject jo2 = new JSONObject(wishListService.getWishListItems(customerToken).toString());
        return jo2;
    }
}

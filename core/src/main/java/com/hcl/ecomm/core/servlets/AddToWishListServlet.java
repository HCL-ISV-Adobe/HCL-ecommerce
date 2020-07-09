package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.WishListService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Add To WishList Servlet",
                "sling.servlet.paths=/bin/hclecomm/addToWishlist",
                "sling.servlet.method=" + HttpConstants.METHOD_POST,
                "sling.servlet.extensions=json"})
public class AddToWishListServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 175460834851290225L;
    private static final Logger LOG = LoggerFactory.getLogger(AddToCartServlet.class);

    @Reference
    private WishListService wishListService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        LOG.debug("addtowishlist doPut()  method start.");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject responseObject = new JSONObject();
        try {
            responseObject.put("status", Boolean.FALSE);
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            String customerToken = request.getHeader("CustomerToken");

            if (StringUtils.isNotEmpty(payload)) {
                JSONObject jsonPayload =  new JSONObject(payload);
                if (isValidPayload(jsonPayload)) {
                    JSONObject addToWishlistResponse = wishListService.addToWishList(jsonPayload.getString("sku"),customerToken);
                    if (addToWishlistResponse.has("statusCode") && addToWishlistResponse.getInt("statusCode") == HttpStatus.OK_200) {
                        responseObject.put("message", addToWishlistResponse.getJSONObject("message"));
                        responseObject.put("status", Boolean.TRUE);
                    } else {
                        responseObject.put("message", addToWishlistResponse.getJSONObject("message"));
                    }
                    LOG.info("addtowishlist doPpst()  method start."+ addToWishlistResponse.toString());
                }
            } else {
                responseObject.put("message", "Missing ProductSku Parameter ");
                responseObject.put("status", Boolean.FALSE);
            }
            response.getWriter().print(responseObject);
        } catch (JSONException e) {
            LOG.error("Error Occured while executing AddToWishListServlet doPost() - add item in wishlist. responseObject={} ", responseObject);
            LOG.error("Full Error={} ", e);
        }
        LOG.debug("addtowishlist doPost()  method end.");
    }

    private boolean isValidPayload(JSONObject jsonPayload) {
        boolean isValidData=Boolean.TRUE;
        if(!jsonPayload.has("sku")){
            isValidData = Boolean.FALSE;
        }
        return isValidData;
    }
}

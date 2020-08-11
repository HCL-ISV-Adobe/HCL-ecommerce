package com.hcl.ecomm.core.servlets;

import com.google.gson.Gson;
import com.hcl.ecomm.core.pojo.Ratings;
import com.hcl.ecomm.core.services.RatingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/ratinglist",
        "sling.servlet.method=" + HttpConstants.METHOD_POST , "sling.servlet.extensions=json" })
public class RatingServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(RatingServlet.class);

    @Reference
    private RatingService ratingService;

    @Override
    protected void doPost(SlingHttpServletRequest request,SlingHttpServletResponse response)
            throws  ServletException, IOException{
        JSONObject responseObject = new JSONObject();
        try {


            responseObject.put("status", Boolean.FALSE);
            response.setContentType("application/json");

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            if (StringUtils.isNotEmpty(payload)) {
                JSONObject jsonPayload =  new JSONObject(payload);
                if (isValidItem(jsonPayload)) {
                    JSONObject ratItem = jsonItemObj(jsonPayload);
                    JSONObject ratingResponse = ratingService.saveRating(ratItem);
                    if (ratingResponse.has("message")) {
                        LOG.info("Rating Response is {}", ratingResponse);
                        responseObject.put("message", ratingResponse.get("message"));
                        responseObject.put("status", Boolean.TRUE);


                    } else {
                        responseObject.put("message", "something went wrong while submiting the rating.");
                    }
                }
            }else {
                responseObject.put("message", "Missing Parameter in payload.");
                responseObject.put("status", Boolean.FALSE);
            }

            response.getWriter().print(responseObject);

        }
        catch (Exception e){

            LOG.error("Error Occured while providing the rating for the product. Full Error={} ", e);

        }


    }


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {

            LOG.info("in do get method");
            response.setContentType("application/json");

            String  sku = request.getParameter("sku");
            List<Ratings> data = ratingService.getRatingDataSQL(sku);
            String ratingJson = new Gson().toJson(data);
            LOG.info("Data is "+ data);
            response.getWriter().write(ratingJson);

        }
        catch (Exception e){

            LOG.error(" Error while while getting Rating count  list. Error={}", e);
        }


    }

    private boolean isValidItem(JSONObject jsonPayload) {
        boolean isValidItem=Boolean.TRUE;
        if(!jsonPayload.has("sku")){
            isValidItem = Boolean.FALSE;
        }
        return isValidItem;
    }

    private JSONObject jsonItemObj(JSONObject itemData) throws JSONException {
        JSONObject rateItem = new JSONObject();
        float rating = BigDecimal.valueOf(itemData.getDouble("rating")).floatValue();

        try {
            rateItem.put("sku", itemData.getString("sku"));
            rateItem.put("name", itemData.getString("name"));
            rateItem.put("rating", rating);
        } catch (JSONException e) {
            LOG.error("Error while executing. Error={}",e);
        }
        return rateItem;
    }
}

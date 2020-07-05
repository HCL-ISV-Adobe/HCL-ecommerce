package com.hcl.ecomm.core.services.impl;

import com.google.gson.*;
import com.google.gson.JsonArray;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.AddToWishListService;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component(
        immediate = true,
        enabled = true,
        service = AddToWishListService.class)
public class AddToWishListServiceimpl implements AddToWishListService{

    private static final Logger LOG = LoggerFactory.getLogger(AddToWishListServiceimpl.class);

    @Reference
    LoginService loginService;

    @Activate
    private MagentoServiceConfig config;

    @Override
    public String getDomainName() {
        return loginService.getDomainName();
    }


    @Override
    public String getAddToWishListPath() {
        return config.customer_addToWishList_string();
    }

    @Override
    public String getWishListItemsPath() {
        return config.customer_getWishList_string();
    }


    private String responseStream = null;
    private String schema = "http";
    @Override
    public JSONObject addToWishList(JSONObject sku, String custToken) {
        LOG.info("addToWishList method start  product={}: " + sku);
        LOG.debug("addToWishList method start  product={}: " + sku);

        String addTowishlistPath = "";
        String authToken = "";
        JSONObject addToWishlistResponse = new JSONObject();;

        try {
            if(custToken != null && !custToken.isEmpty()) {
                authToken = custToken;
                addTowishlistPath = getAddToWishListPath();
            }
            String domainName = getDomainName();
            String Sku = sku.getString("sku");
           addTowishlistPath = addTowishlistPath.replace("sku", Sku);
            String url = schema + "://" + domainName + addTowishlistPath;
            LOG.info("addTowishlistPath : " + url);

            Integer statusCode;


            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json");
            httppost.setHeader("Authorization", "Bearer " +authToken);
            CloseableHttpResponse httpResponse = httpClient.execute(httppost);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            LOG.info("add to wishlist: magento statusCode ={}",statusCode);
            JSONObject wishlistresponse = new JSONObject();
            if(HttpStatus.SC_OK == statusCode){
                BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
                String str="";
                String output;
                while ((output = br.readLine()) != null) {

                    str +=output;
                }

                wishlistresponse.put("response",str);
                addToWishlistResponse.put("statusCode", statusCode);
                addToWishlistResponse.put("message", wishlistresponse);
            }else if(HttpStatus.SC_BAD_REQUEST == statusCode){
                BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
                String str="";
                String output;
                while ((output = br.readLine()) != null) {

                    str +=output;
                }

                wishlistresponse.put("response",str);
                addToWishlistResponse.put("statusCode", statusCode);
                addToWishlistResponse.put("message", wishlistresponse);
               // addToWishlistResponse.put("message", httpResponse.getEntity().getContent().toString());
                LOG.error("Error while add to wishlist. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
            }else{
                LOG.error("Error while add to wishlist. status code:{}",statusCode);
            }
        } catch (Exception e) {
            LOG.error("addToWishList method caught an exception " + e.getMessage());
        }

        return addToWishlistResponse;
    }





    @Override
    public JSONObject getWishListItems(String customerToken) {
        String token = "";
        String url = "";
        String domainName = loginService.getDomainName();
        JSONObject customerWishListResponse = new JSONObject();

        token = customerToken;
        String getwishlistPath = getWishListItemsPath();
        url = schema + "://" + domainName + getwishlistPath;

        JsonObject WishListItems = new JsonObject();
        LOG.debug("url : " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " +token);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            Integer statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode == 200)
            {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
                WishListItems = new Gson().fromJson(responseStream, JsonObject.class);

                customerWishListResponse.put("statusCode", statusCode);
                customerWishListResponse.put("message", WishListItems);
            }
            else if(statusCode == 404)
            {
                customerWishListResponse.put("statusCode", statusCode);
                customerWishListResponse.put("message", "No items Present in wishlist");
            }
            else
            {
                responseStream = "Failed to fetch wishlist details.";
            }
            //WishListItems = new Gson().fromJson(responseStream, JsonArray.class);
            LOG.info( "wishlist Items Response in Json Array : " + WishListItems);

        }
        catch (Exception e)
        {
            LOG.error("Exception while fetching wishlist details : " + e.getMessage());
        }
        return customerWishListResponse;
    }


}


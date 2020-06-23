package com.hcl.ecomm.core.services.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.*;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.CartService;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@Component(service =CartService.class, immediate = true)
public class CartServiceImpl implements CartService {

    @Reference
    LoginService loginService;

    @Activate
    private MagentoServiceConfig config;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    private JsonArray cartResponse= null;
    private String responseStream = null;
    private String schema = "http";

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public String getServicePath() {
        return config.cartFetch_servicePath_string();
    }

    @Override
    public JsonArray getCartItemsDetails(String cartId, String customerToken) {
        String token = "";
        String url = "";
        String domainName = loginService.getDomainName();
        if(customerToken != null && !customerToken.isEmpty()) {
            token = customerToken;
            url = schema + "://" + domainName + config.customer_getCart_string() ;
        }
        else
            {
            token = loginService.getToken();
            url = schema + "://" + domainName + getServicePath() + cartId + "/items";
        }
        JsonArray cartItems = null;
        LOG.debug("url : " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("Authorization", "Bearer " +token);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
            }
            else
            {
                responseStream = "Failed to fetch cart details.";
            }
            cartItems = new Gson().fromJson(responseStream, JsonArray.class);
            LOG.info( "Cart Items Response in Json Array : " + cartItems);

        }
        catch (Exception e)
        {
            LOG.error("Exception while fetching cart details : " + e.getMessage());
        }
        return cartItems;
    }

    @Override
    public int getCartItemCount(String cartId, String customerToken) {
        int cartItemCount = 0;
        JsonArray cartItemsArray = getCartItemsDetails(cartId, customerToken);
        cartItemCount = cartItemsArray.size();
        return cartItemCount;
    }

    @Override
    public String updateCartDetails(String payload, String customerToken) {
        String token = "";
        String url = "";
        String cartId = "";
        String domainName = loginService.getDomainName();
        String servicePath = config.cartUpdate_servicePath_string();
        if(customerToken != null && !customerToken.isEmpty()) {
            token = customerToken;
            url = schema + "://" + domainName + config.customer_updateCart_string() ;
        }
        else {
            token = loginService.getToken();
        }
        String bearerToken = "Bearer" + token;
        String finalToken = bearerToken.replaceAll("\"","");
        LOG.info("String payload : " + payload);
        try {
            JsonArray productArray = new JsonParser().parse(payload).getAsJsonArray();
            LOG.info("ProductArray : " +productArray);
            Iterator<JsonElement> iterator = productArray.iterator();
            while(iterator.hasNext()){
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                JsonObject cartItems = jsonObject.get("cartItem").getAsJsonObject();
                cartId = cartItems.get("quote_id").getAsString();
                if(customerToken == null || customerToken.isEmpty())
                {
                    url = schema + "://" + domainName + servicePath + cartId + "/items";
                }
                LOG.info("url : " + url);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(url);
                StringEntity input = new StringEntity(jsonObject.toString(),ContentType.APPLICATION_JSON);
                LOG.info("input : " + input);
                httpPost.setHeader("Authorization", "Bearer " +token);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setEntity(input);

                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
                if(httpResponse.getStatusLine().getStatusCode() == 200)
                {
                    responseStream = EntityUtils.toString(httpResponse.getEntity());
                }
                else
                {
                    responseStream = "Failed to fetch cart details.";
                }
                LOG.info( "Cart Items Response in Json Array : " + responseStream);
            }
        }
        catch (Exception e)
        {
            LOG.error("Exception while fetching cart details : " + e.getMessage());
        }
        return responseStream;
    }

    @Override
    public String applyCoupon(String couponApplied) {
        String couponDiscount = "";
        try
        {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(ResourceResolverFactory.SUBSERVICE, "userName");
            ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            Page listPage = pageManager.getPage(config.getCouponlistPath_string());
            Resource resource = listPage.adaptTo(Resource.class);
            Resource couponList = resource.getChild("jcr:content").getChild("list");
            Iterator<Resource> CouponListItems = couponList.listChildren();
            while (CouponListItems.hasNext()) {
                Resource item = CouponListItems.next();
                ValueMap itemProperty = item.getValueMap();
                String couponCode = (String) itemProperty.get("jcr:title");
                if ((couponApplied.trim()).equals(couponCode.trim())) {
                    couponDiscount = (String) itemProperty.get("value");
                    break;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage());
        }

        return couponDiscount;
    }

    @Override
    public JsonArray getCustomerCart(String customerToken) {
        String token = "";
        String url = "";
        String domainName = loginService.getDomainName();

            token = customerToken;
            url = schema + "://" + domainName + config.customer_getCart_string() ;

        JsonArray cartItems = null;
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
            }
            else if(httpResponse.getStatusLine().getStatusCode() == 404)
            {
                responseStream = "[]";
            }
            else
            {
                responseStream = "Failed to fetch cart details.";
            }
            cartItems = new Gson().fromJson(responseStream, JsonArray.class);
            LOG.info( "Cart Items Response in Json Array : " + cartItems);

        }
        catch (Exception e)
        {
            LOG.error("Exception while fetching cart details : " + e.getMessage());
        }
        return cartItems;
    }


}


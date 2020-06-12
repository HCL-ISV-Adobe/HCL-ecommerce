package com.hcl.ecomm.core.services.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.osgi.service.metatype.annotations.Designate;
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
    public JsonArray getCartItemsDetails(String cartId) {
        String token = loginService.getToken();
        String domainName = loginService.getDomainName();
        JsonArray cartItems = null;
        String url = schema + "://" + domainName + getServicePath() + cartId + "/items";
        LOG.info("url : " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");

        String bearerToken = "Bearer" + token;
        String finalToken = bearerToken.replaceAll("\"","");
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
    public int getCartItemCount(String cartId) {
        int cartItemCount = 0;
        JsonArray cartItemsArray = getCartItemsDetails(cartId);
        cartItemCount = cartItemsArray.size();
        return cartItemCount;
    }

    @Override
    public String updateCartDetails(String payload) {
        String token = loginService.getToken();
        String bearerToken = "Bearer" + token;
        String finalToken = bearerToken.replaceAll("\"","");

        String domainName = loginService.getDomainName();
        String servicePath = config.cartUpdate_servicePath_string();
        LOG.info("String payload : " + payload);
        try {
            JsonArray productArray = new JsonParser().parse(payload).getAsJsonArray();
            LOG.info("ProductArray : " +productArray);
            Iterator<JsonElement> iterator = productArray.iterator();
            while(iterator.hasNext()){
                JsonObject jsonObject = iterator.next().getAsJsonObject();
                JsonObject cartItems = jsonObject.get("cartItem").getAsJsonObject();
                String cartId = cartItems.get("quote_id").getAsString();
                LOG.info("cartId : " +cartId);
                String url = schema + "://" + domainName + servicePath + cartId + "/items";
                LOG.info("url : " + url);

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(url);

                StringEntity input = new StringEntity(jsonObject.toString(),ContentType.APPLICATION_JSON);
                //StringEntity input = new StringEntity(payload, ContentType.APPLICATION_JSON);
                LOG.info("input : " + input);
                httpPost.setHeader("Authorization", finalToken);
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
}

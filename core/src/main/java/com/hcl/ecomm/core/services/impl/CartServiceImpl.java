package com.hcl.ecomm.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.CartService;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =CartService.class)
//@Designate(ocd = ProductServiceConfig.class)
public class CartServiceImpl implements CartService {

    @Reference
    LoginService loginService;

    @Activate
    private MagentoServiceConfig config;

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
}

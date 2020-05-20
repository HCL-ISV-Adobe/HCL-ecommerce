package com.hcl.ecomm.core.services.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcl.ecomm.core.config.CartServiceConfig;
import com.hcl.ecomm.core.services.CartService;
import com.hcl.ecomm.core.services.LoginService;
import com.hcl.ecomm.core.utility.ProductUtility;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service =CartService.class)
@Designate(ocd = CartServiceConfig.class)
public class CartServiceImpl implements CartService {

    @Reference
    LoginService loginService;

    @Activate
    private CartServiceConfig config;

    private JsonArray cartResponse= null;
    private String responseStream = null;
    private String schema = "http";

    private static final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public String getDomainName() {
        return config.domainName_string();
    }

    @Override
    public String getServicePath() {
        return config.servicePath_string();
    }

    @Override
    public JsonArray getCartItemsDetails(String cartId) {
        String token = loginService.getToken();
        JsonArray cartItems = null;
        String url = schema + "://" + getDomainName() + getServicePath() + cartId + "/items";
        //sample url generated = "http://localhost:8081/magento/rest/us/V1/guest-carts/<cart-id>/items";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json");

        String bearerToken = "Bearer" + token;
        String finalToken = bearerToken.replaceAll("\"","");
        LOG.info("Final Token Value is : " + finalToken);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if(httpResponse.getStatusLine().getStatusCode() == 200)
            {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
                LOG.info( "Cart Item Response json String : " + responseStream);
            }
            else
            {
                responseStream = "Failed to fetch cart details.";
                LOG.info( "Failed to fetch cart details.");
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
        LOG.info("inside CartItemCount: ");
       JsonArray cartItemsArray = getCartItemsDetails(cartId);
       cartItemCount = cartItemsArray.size();
        LOG.info("Item Count : " + cartItemCount);
        return cartItemCount;
    }
}

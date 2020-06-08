package com.hcl.ecomm.core.services.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.LoginService;
import com.hcl.ecomm.core.services.ShippingInfoService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
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
        service = ShippingInfoService.class)
public class ShippingInfoServiceImpl implements ShippingInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

     @Reference
    LoginService loginService;

    @Activate
    private MagentoServiceConfig config;

    @Override
    public String getDomainName() {
        return loginService.getDomainName();
    }

    @Override
    public String getShippingInfoPath() {
        return  config.shippingInfo_servicePath_string();
    }

    @Override
    public JSONObject createShipInfo(JSONObject shipItem,String cartId) {

        LOG.debug("shippinginfo() method start {}" + shipItem);
        String scheme = "http";
        JSONObject updatedItem = new JSONObject();

        try {
            String domainName = getDomainName();
            String shippingInfoPath = getShippingInfoPath();

            shippingInfoPath = shippingInfoPath.replace("{cartId}", cartId);
            String url = scheme + "://" + domainName + shippingInfoPath;
            LOG.info("shippingInfo url ={}",url);
            Integer statusCode;
            JSONObject response = new JSONObject();
            StringEntity input = new StringEntity(shipItem.toString(),ContentType.APPLICATION_JSON);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(input);

            CloseableHttpResponse httpResponse = httpClient.execute(httpost);
            LOG.info("httpResponse is {}",httpResponse);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            LOG.info("Shipping Info : magento statusCode ={}",statusCode);

            if(HttpStatus.SC_OK == statusCode){
                BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
                String output;
                while ((output = br.readLine()) != null) {
                    response = new JSONObject(output);
                }
                updatedItem.put("statusCode", statusCode);
                updatedItem.put("message", response);
            }else if(HttpStatus.SC_BAD_REQUEST == statusCode){
                updatedItem.put("statusCode", statusCode);
                updatedItem.put("message", httpResponse.getEntity().getContent().toString());
                LOG.error("Error while Shipping Info . status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
            }else{
                LOG.error("Error while Shipping Info. status code:{}",statusCode);
            }
        } catch (Exception e) {
            LOG.error("error while executing shippingInfo() method. Error={}" + e);
        }
        LOG.debug("updateCartItem() method end  shippingINfo={}: " + updatedItem);
        return updatedItem;
    }




}

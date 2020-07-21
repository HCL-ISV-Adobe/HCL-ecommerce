package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.CreateOrderService;
import com.hcl.ecomm.core.services.LoginService;
import com.hcl.ecomm.core.services.OrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.http.HttpStatus;
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
        service = OrderService.class)

public class OrderServiceImpl implements OrderService {

    @Reference
    LoginService loginService;

    @Activate
    private MagentoServiceConfig config;

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public JSONObject getOrders(String customerEmail) {

        String scheme = "http";
        String domainName = loginService.getDomainName();
        String getOrderServicePath = config.customer_getOrder_string();
        String authToken = loginService.getToken();
        String url = scheme + "://" + domainName + getOrderServicePath
                + "?searchCriteria[filterGroups][0][filters][0][field]=" + "customer_email"
                + "&searchCriteria[filterGroups][0][filters][0][value]=" + customerEmail
                + "&searchCriteria[sortOrders][0][field]=" + "created_at"
                + "&searchCriteria[sortOrders][0][direction]=" + "DESC";

        LOG.info("createGuestCartPath  : " + url);
        JSONObject getOrderResponse = new JSONObject();

        try {
            Integer statusCode;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("Authorization", "Bearer " + authToken);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            LOG.info("Get Order: magento statusCode ={}",statusCode);
            if(HttpStatus.OK_200 == statusCode){
                String str = EntityUtils.toString(httpResponse.getEntity());

                getOrderResponse.put("statusCode", statusCode);
                getOrderResponse.put("message", str);
            }else if(HttpStatus.BAD_REQUEST_400 == statusCode){
                getOrderResponse.put("statusCode", statusCode);
                getOrderResponse.put("message", httpResponse.getEntity().getContent().toString());
                LOG.error("Error while  getting customer Orders. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
            }else{
                LOG.error("Error while getting customer orders. status code:{}",statusCode);
            }
        }
        catch (Exception e) {
            LOG.error("Error while fetching orders of the customer : " + e);
        }
        LOG.debug("getOrder method end  getOrderResponse={}: " + getOrderResponse);
        return getOrderResponse;
    }
}

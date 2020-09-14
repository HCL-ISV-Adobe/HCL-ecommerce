package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.constants.EmailTemplateConstants;
import com.hcl.ecomm.core.services.CreateOrderService;
import com.hcl.ecomm.core.services.CustomEmailService;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Component(
        immediate = true,
        enabled = true,
        service = CreateOrderService.class)

public class CreateOrderServiceImpl implements CreateOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrderServiceImpl.class);

    @Activate
    private MagentoServiceConfig config;

    @Reference
    LoginService loginService;

    @Reference
    CustomEmailService customEmailService;

    @Override
    public String getCreateOrderPath() {
        return config.createOrder_servicePath_string();
    }

    @Override
    public JSONObject createOrderItem(JSONObject orderItem, String cartId, String customerToken, String deliverCharges, String couponDiscount, JSONObject storeAddress) {

        LOG.debug("createOrderItem method={}, cartID= {}", orderItem, cartId);
        String scheme = "http";
        String token = StringUtils.EMPTY;
        String url = StringUtils.EMPTY;
        int statusCode = 0;
        JSONObject createOrderItemRes = new JSONObject();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        StringEntity input = new StringEntity(orderItem.toString(), ContentType.APPLICATION_JSON);
        try {
            String domainName = loginService.getDomainName();
            if (customerToken != null && !customerToken.isEmpty()) {
                token = customerToken;
                url = scheme + "://" + domainName + config.customer_createOrder_string();
                HttpPost httpost = new HttpPost(url);
                httpost.setHeader("Authorization", "Bearer " + token);
                httpost.setEntity(input);

                httpResponse = httpClient.execute(httpost);
                LOG.debug("httpResponse is {}", httpResponse);
                statusCode = httpResponse.getStatusLine().getStatusCode();

            } else {
                String createOrderPath = getCreateOrderPath();
                createOrderPath = createOrderPath.replace("{cartId}", cartId);
                url = scheme + "://" + domainName + createOrderPath;
                HttpPut httput = new HttpPut(url);
                httput.setEntity(input);
                httpResponse = httpClient.execute(httput);
                LOG.debug("httpResponse is {}", httpResponse);
                statusCode = httpResponse.getStatusLine().getStatusCode();
            }
            LOG.debug("orderInfo url ={}", url);

            if (org.eclipse.jetty.http.HttpStatus.OK_200 == statusCode) {
                BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
                String order = StringUtils.EMPTY;
                String output;

                while ((output = br.readLine()) != null) {
                    order += output;
                }
                JSONObject orderId = new JSONObject();
                order = order.replace("\"", "");
                LOG.debug("Get Order Detail for order Id: {}", order);

                String authToken = loginService.getToken();
                url = scheme + "://" + domainName + "/us/V1/orders/" + order;
                LOG.info("createOrderInfo url ={}", url);
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + authToken);
                CloseableHttpResponse Httpresponse = httpClient.execute(httpGet);
                statusCode = Httpresponse.getStatusLine().getStatusCode();
                if (org.eclipse.jetty.http.HttpStatus.OK_200 == statusCode) {
                    String orderRes = EntityUtils.toString(Httpresponse.getEntity());

                    JSONObject jsonRes = new JSONObject(orderRes);
                    JSONObject billingAddress = jsonRes.getJSONObject("billing_address");
                    String smail = jsonRes.getString("customer_email");
                    if (jsonRes.length() != 0 && billingAddress.getString("firstname").equals("NA")) {

                        //send Email for store pick up
                        customEmailService.sendEmail(EmailTemplateConstants.STORE_PICKUP_EMAIL_TEMPLATE, getEmailParameters(jsonRes, order, deliverCharges, couponDiscount, storeAddress), smail);
                    }
                    else if (jsonRes.length() != 0) {

                        //send Email for shipping to address
                        customEmailService.sendEmail(EmailTemplateConstants.SHIPPING_ADDRESS_EMAIL_TEMPLATE, getEmailParameters(jsonRes, order, deliverCharges, couponDiscount, storeAddress), smail);
                    }
                } else if (org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400 == statusCode) {
                    LOG.error("Error while  getting customer Orders. status code:{} and message={}", statusCode, Httpresponse.getEntity().getContent());
                } else {
                    LOG.error("Error while getting customer orders. status code:{}", statusCode);
                }
                orderId.put("orderId", order);
                createOrderItemRes.put("statusCode", statusCode);
                createOrderItemRes.put("message", orderId);
            } else if (HttpStatus.SC_BAD_REQUEST == statusCode) {
                createOrderItemRes.put("statusCode", statusCode);
                createOrderItemRes.put("message", httpResponse.getEntity().getContent().toString());
                LOG.error("Error while create Order Info . status code:{} and message={}", statusCode, httpResponse.getEntity().getContent());
            } else {
                LOG.error("Error while create Order Info. status code:{}", statusCode);
            }
        } catch (Exception e) {
            LOG.error("createOrderCart method caught an exception {0}", e);
        }
        LOG.debug("createOrderCart method {}: ", createOrderItemRes);
        return createOrderItemRes;
    }

    /*
    This method takes json array of street address and returns as a String.
     */
    private String getAddressDetails(JSONArray streetAddress) throws JSONException {
        String[] streetAddressArray = new String[streetAddress.length()];
        for (int i = 0; i < streetAddressArray.length; i++) {
            streetAddressArray[i] = streetAddress.getString(i);
        }
        return String.join(",", streetAddressArray);
    }

    /*
    This method takes the jsonObject, order, deliveryCharges, couponDiscount and returns a map of containing email parameters.
     */
    private Map<String, String> getEmailParameters(JSONObject jsonRes, String order, String deliverCharges, String couponDiscount, JSONObject storeAddress) throws JSONException {
        Map<String, String> emailParams = new HashMap<>();
        JSONObject billingAddress = jsonRes.getJSONObject("billing_address");
        JSONArray streetAddress = billingAddress.getJSONArray("street");
        String street = getAddressDetails(streetAddress);
        String city = billingAddress.get("city").toString();
        String region = billingAddress.get("region").toString();
        String country = billingAddress.get("country_id").toString();
        String postCode = billingAddress.get("postcode").toString();

        int subTotal = jsonRes.getInt("subtotal");
        float grandTotal = Float.parseFloat(String.valueOf(subTotal)) + Float.parseFloat(deliverCharges) - Float.parseFloat(couponDiscount);
        DecimalFormat format = new DecimalFormat("0.00");
        String subTotalDecimal = format.format(grandTotal);
        String smail = jsonRes.getString("customer_email");
        String firstname = jsonRes.getString("customer_firstname");
        String lastname = jsonRes.getString("customer_lastname");
        emailParams.put("receiveremail", smail);
        emailParams.put("firstname", firstname);
        emailParams.put("lastname", lastname);

        emailParams.put("orderId", order);
        emailParams.put("grandTotal", subTotalDecimal);
        emailParams.put("address", street + ", " + city + ", " + region + ", " + country + ", " + postCode);

        // store pick up address
        if (storeAddress != null) {
            String contactName = storeAddress.get("contact_name").toString();
            String pickUpCity = storeAddress.get("city").toString();
            String phone = storeAddress.get("phone").toString();
            String pickUpStreet = storeAddress.get("street").toString();
            String pickupLocationCode = storeAddress.get("pickup_location_code").toString();
            String name = storeAddress.get("name").toString();
            String postCodes = storeAddress.get("postcode").toString();
            String regions = storeAddress.get("region").toString();
            String email = storeAddress.get("email").toString();
            String storePickUpAddress = contactName + "," + pickUpCity + "," + phone + "," + pickUpStreet + "," + pickupLocationCode + "," + name + "," + postCodes + "," + regions + "," + email;
            emailParams.put("storeAddress", storePickUpAddress);
        }
        return emailParams;
    }

}

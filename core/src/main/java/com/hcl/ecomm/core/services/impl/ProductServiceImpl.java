package com.hcl.ecomm.core.services.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.LoginService;
import com.hcl.ecomm.core.services.ProductService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, enabled = true, service = ProductService.class)
@Designate(ocd = MagentoServiceConfig.class)
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Reference
    LoginService loginService;

    String scheme = "http";
    String responseStream = null;
    JSONArray productJsonArray = null;
    JSONObject productJsonObject = null;
    String searchCriteriaFilterField_1 = "searchCriteria[filterGroups][0][filters][0][field]";
    String searchCriteriaFilterValue_1 = "searchCriteria[filterGroups][0][filters][0][value]";
    String searchCriteriaFilterField_2 = "searchCriteria[filterGroups][0][filters][1][field]";
    String searchCriteriaFilterValue_2 = "searchCriteria[filterGroups][0][filters][1][value]";

    @Activate
    private MagentoServiceConfig config;

    @Override
    public String getServicePath() {
        return config.productService_servicePath();
    }

    @Override
    public String getSearchCriteriaField() {
        return config.productService_searchCriteriaField();
    }

    @Override
    public String getSearchCriteriaValue() {
        return config.productService_searchCriteriaValue();
    }



    @Override
    public JSONArray getAllProductDetails() throws JSONException {


        String token = loginService.getToken();
        String domainName = loginService.getDomainName();
        String servicePath = getServicePath();
        String searchCriteriaField = getSearchCriteriaField();
        String searchCriteriaValue = getSearchCriteriaValue();


        String productUrl = scheme + "://" + domainName + servicePath
                + "?" + searchCriteriaFilterField_1 + "=" + searchCriteriaField
                + "&" + searchCriteriaFilterValue_1 + "=" + searchCriteriaValue;


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(productUrl);

        httpGet.setHeader("Content-Type", "application/json");
        String bearerToken = "Bearer " + token;
        String finalToken = bearerToken.replaceAll("\"", "");
        LOG.info("Final Token Value is : " + finalToken);
        httpGet.setHeader("Authorization", finalToken);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
                LOG.info("Product Response Json : " + responseStream);

            } else {
                responseStream = "Failed to fetch products from the store";
                LOG.error("Failed to fetch products from the store");
            }
        } catch (Exception e) {
            LOG.error(" getAllProductDetails method caught an exception" + e.getMessage());
        }
        productJsonObject = new JSONObject(responseStream);
        JSONArray productJsonArray = productJsonObject.getJSONArray("items");
        LOG.info("Json Array formed : " + productJsonArray);

        return productJsonArray;
    }

    @Override
    public List<String> getAllProductSkus(JsonArray productJson) {

        ArrayList<String> productSkuList = new ArrayList<>();

        for (JsonElement jel : productJson) {
            String sku = jel.getAsJsonObject().get("sku").getAsString();
            productSkuList.add(sku);
        }
        return productSkuList;
    }

    @Override
    public JSONObject getProductDetail(String sku) {

        String token = loginService.getToken();
        String domainName = loginService.getDomainName();
        String servicePath = getServicePath();

        String productUrl = scheme + "://" + domainName + servicePath
                + "/" + sku;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(productUrl);
        String websiteId = getSearchCriteriaValue();
        httpGet.setHeader("Content-Type", "application/json");
        String bearerToken = "Bearer " + token;
        String finalToken = bearerToken.replaceAll("\"", "");
        LOG.info("Final Token Value is : " + finalToken);
        httpGet.setHeader("Authorization", finalToken);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
                LOG.info("Product Details Response Json : " + responseStream);
            } else {
                responseStream = "Failed to fetch product details from the store";
                LOG.error("Failed to fetch products details from the store");
            }
            productJsonObject = new JSONObject(responseStream);
            if (productJsonObject.getJSONObject("extension_attributes").has("website_ids")) {
                JSONArray array = productJsonObject.getJSONObject("extension_attributes").getJSONArray("website_ids");

                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i).toString().equals(websiteId)) {
                        return productJsonObject;
                    }
                }
            }

            LOG.info("product details Response in Json object : " + productJsonObject);
        } catch (Exception e) {
            LOG.error(" getProductDetail method caught an exception" + e.getMessage());
        }

        return new JSONObject();
    }


}

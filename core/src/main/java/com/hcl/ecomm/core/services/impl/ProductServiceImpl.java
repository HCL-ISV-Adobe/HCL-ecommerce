package com.hcl.ecomm.core.services.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hcl.ecomm.core.config.ProductServiceConfig;
import com.hcl.ecomm.core.services.LoginService;
import com.hcl.ecomm.core.services.ProductService;
import com.hcl.ecomm.core.utility.ProductUtility;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component(immediate = true, enabled = true, service = ProductService.class)
@Designate(ocd = ProductServiceConfig.class)
public class ProductServiceImpl implements ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Reference
	LoginService loginService;

	String scheme = "http";
	String responseStream = null;
	JsonArray productJsonArray = null;

	@Activate
	private ProductServiceConfig config;

	
	@Override
	public String getDomainName() {
		return config.productService_domainName();
	}

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
	public JsonArray getAllProductDetails() {

		LOG.info("inside getAllProductDetails method");
		String token = loginService.getToken();
		String domainName = getDomainName();
		String servicePath = getServicePath();
		String searchCriteriaField = getSearchCriteriaField();
		String searchCriteriaValue = getSearchCriteriaValue();
		
		String productUrl = scheme + "://" + domainName + servicePath
				+ "?searchCriteria[filterGroups][0][filters][0][field]=" + searchCriteriaField
				+ "&searchCriteria[filterGroups][0][filters][0][value]=" + searchCriteriaValue;
				

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
		productJsonArray = ProductUtility.fromStringToJsonArray(responseStream);
		LOG.info("Json Array formed : " + productJsonArray);

		return productJsonArray;
	}

	@Override
	public List<String> getAllProductSkus(JsonArray productJson) {

		ArrayList<String> productSkuList = new ArrayList<String>();

		for (JsonElement jel : productJson) {
			String sku = jel.getAsJsonObject().get("sku").getAsString();
			productSkuList.add(sku);
		}
		return productSkuList;
	}

	@Override
	public JsonArray getProductDetail(String sku) {

		LOG.info("inside getProductDetail method");
		String token = loginService.getToken();
		String domainName = getDomainName();
		String servicePath = getServicePath();
		String searchFirstCriteriaField = getSearchCriteriaField();
		String searchFirstCriteriaValue = getSearchCriteriaValue();
		String searchSecondCriteriaField = "SKU";
		String searchSecondCriteriaValue = sku;

		String productUrl = scheme + "://" + domainName + servicePath
				+ "?searchCriteria[filterGroups][0][filters][0][field]=" + searchFirstCriteriaField
				+ "&searchCriteria[filterGroups][0][filters][0][value]=" + searchFirstCriteriaValue
				+ "&searchCriteria[filterGroups][0][filters][1][field]=" + searchSecondCriteriaField
				+ "&searchCriteria[filterGroups][0][filters][1][value]=" + searchSecondCriteriaValue;

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
			LOG.error(" getProductDetail method caught an exception" + e.getMessage());
		}
		productJsonArray = ProductUtility.fromStringToJsonArray(responseStream);
		LOG.info("Json Array formed : " + productJsonArray);

		return productJsonArray;
	}


}

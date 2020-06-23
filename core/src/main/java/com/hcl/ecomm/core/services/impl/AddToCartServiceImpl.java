package com.hcl.ecomm.core.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.LoginService;

@Component(
		immediate = true,
		enabled = true, 
		service = AddToCartService.class)
public class AddToCartServiceImpl implements AddToCartService{

	private static final Logger LOG = LoggerFactory.getLogger(AddToCartServiceImpl.class);

	@Reference
	LoginService loginService;
	
	@Activate
	private MagentoServiceConfig config;

	@Override
	public String getDomainName() {
		return loginService.getDomainName();
	}


	@Override
	public String getAddToCartPath() {
		return config.magentoService_addToCartPath();
	}
	
	@Override
	public String updateCartItemPath() {
		return config.magentoService_updateCartItemPath();
	}
	
	@Override
	public JSONObject addToCart(JSONObject product, String custToken) {
		LOG.debug("addToCart method start  product={}: " + product);
		String scheme = "http";
		String addToCartPath = "";
		String authToken = "";
		JSONObject addToCartResponse = new JSONObject();

		try {
			if(custToken != null && !custToken.isEmpty()) {
				authToken = custToken;
				addToCartPath = config.customer_addToCart_string();
			}
			else {
				authToken = loginService.getToken();
				addToCartPath = getAddToCartPath();
			}

			String domainName = getDomainName();
			String cartid = product.getJSONObject("cartItem").getString("quote_id");
			addToCartPath = addToCartPath.replace("{cartId}", cartid);
			String url = scheme + "://" + domainName + addToCartPath;
			LOG.info("addToCartPath  : " + url);
			
			Integer statusCode;
			JSONObject response = new JSONObject();
			StringEntity input = new StringEntity(product.toString(),ContentType.APPLICATION_JSON);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization", "Bearer " +authToken);
			httppost.setEntity(input);
			CloseableHttpResponse httpResponse = httpClient.execute(httppost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			
			LOG.info("add to cart: magento statusCode ={}",statusCode);
			
			if(HttpStatus.SC_OK == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String output;
				while ((output = br.readLine()) != null) {
					response = new JSONObject(output);
				}
				addToCartResponse.put("statusCode", statusCode);
				addToCartResponse.put("message", response);
			}else if(HttpStatus.SC_BAD_REQUEST == statusCode){
				addToCartResponse.put("statusCode", statusCode);
				addToCartResponse.put("message", httpResponse.getEntity().getContent().toString());
				LOG.error("Error while add to cart. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while add to cart. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("addToCart method caught an exception " + e.getMessage());
		}
		LOG.debug("addToCart method end  product={}: " + product);
		return addToCartResponse;
	}


	@Override
	public JSONObject updateCartItem(JSONObject item, String itemId, String custToken) {
		LOG.debug("updateCartItem() method start.  product={}: " + item);
		String scheme = "http";
		JSONObject updatedItem = new JSONObject();

		try {
			String authToken = loginService.getToken();
			String domainName = getDomainName();
			String updateCartItemPath = updateCartItemPath();
			String cartId = item.getJSONObject("cartItem").getString("quote_id");
			updateCartItemPath = updateCartItemPath.replace("{cartId}", cartId).replace("{ItemId}", itemId);
			String url = scheme + "://" + domainName + updateCartItemPath;
			LOG.info("updateCartItem url ={}",url);
			Integer statusCode;
			JSONObject response = new JSONObject();
			StringEntity input = new StringEntity(item.toString(),ContentType.APPLICATION_JSON);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPut httput = new HttpPut(url);
			httput.setHeader("Content-Type", "application/json");
			httput.setHeader("Authorization", "Bearer " +authToken);
			httput.setEntity(input);
			CloseableHttpResponse httpResponse = httpClient.execute(httput);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			
			LOG.info("update cart item: magento statusCode ={}",statusCode);
			
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
				LOG.error("Error while update cart item. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while update cart item. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("error while executing updateCartItem() method. Error={}" + e);
		}
		LOG.debug("updateCartItem() method end  updatedItem={}: " + updatedItem);
		return updatedItem;
	}




	





}

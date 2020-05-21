package com.hcl.ecomm.core.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import com.hcl.ecomm.core.config.CartServiceConfig;
import com.hcl.ecomm.core.config.LoginServiceConfig;
import com.hcl.ecomm.core.config.ProductServiceConfig;
import com.hcl.ecomm.core.services.AddToCartService;
import com.hcl.ecomm.core.services.ProductService;

@Component(
		immediate = true,
		enabled = true, 
		service = AddToCartService.class)
@Designate(ocd = CartServiceConfig.class)
public class AddToCartServiceImpl implements AddToCartService{

	private static final Logger LOG = LoggerFactory.getLogger(AddToCartServiceImpl.class);

	@Activate
	private CartServiceConfig config;

	@Override
	public String getDomainName() {
		return config.cartService_domainName();
	}

	@Override
	public String getEmptyCartPath() {
		return config.cartService_emptyCartPath();
	}

	@Override
	public String getAddToCartPath() {
		return config.cartService_addToCartPath();
	}
	
	
	@Override
	public JSONObject addToCart(JSONObject product) {

		String scheme = "http";
		String token = null;
		JSONObject addToCartResponse = new JSONObject();

		try {
			String domainName = getDomainName();
			String addToCartPath = getAddToCartPath();
			String cartid = product.getJSONObject("cartItem").getString("quote_id");
			addToCartPath = addToCartPath.replace("{cartId}", cartid);
			String url = scheme + "://" + domainName + addToCartPath;
			LOG.debug("addToCartPath  : " + url);
			
			Integer statusCode;
			StringEntity input = new StringEntity(product.toString(),ContentType.APPLICATION_JSON);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(input);
			CloseableHttpResponse httpResponse = httpClient.execute(httppost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			LOG.info("add to cart: magento statusCode ={}",statusCode);
			if(200 == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String output;
				while ((output = br.readLine()) != null) {
					addToCartResponse = new JSONObject(output);
				}
			}else if((401 == statusCode || 400 == statusCode)){
				addToCartResponse.put("statusCode", statusCode);
				addToCartResponse.put("message", httpResponse.getEntity().getContent().toString());
				LOG.error("Error while add to cart. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while add to cart. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("getToken method caught an exception " + e.getMessage());
		}
		return addToCartResponse;
	}

	@Override
	public String createGuestCart(JSONObject product) {
		// TODO Auto-generated method stub
		return null;
	}





}

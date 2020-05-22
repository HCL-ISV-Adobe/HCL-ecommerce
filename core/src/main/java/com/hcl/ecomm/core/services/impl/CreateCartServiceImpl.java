package com.hcl.ecomm.core.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.ecomm.core.config.CreateCartServiceConfig;
import com.hcl.ecomm.core.services.CreateCartService;

@Component(
		immediate = true,
		enabled = true, 
		service = CreateCartService.class)
@Designate(ocd = CreateCartServiceConfig.class)
public class CreateCartServiceImpl implements CreateCartService{

	private static final Logger LOG = LoggerFactory.getLogger(CreateCartServiceImpl.class);

	@Activate
	private CreateCartServiceConfig config;

	@Override
	public String getDomainName() {
		return config.cartService_domainName();
	}

	@Override
	public String getEmptyCartPath() {
		return config.cartService_emptyCartPath();
	}
	

	@Override
	public JSONObject createGuestCart() {
		LOG.debug("createGuestCart method start.");
		String scheme = "http";
		JSONObject createGuestCartRes = new JSONObject();
		

		try {
			String domainName = getDomainName();
			String createGuestCartPath = getEmptyCartPath();
			String url = scheme + "://" + domainName + createGuestCartPath;
			LOG.info("createGuestCartPath  : " + url);
			
			Integer statusCode;
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost(url);
			CloseableHttpResponse httpResponse = httpClient.execute(httppost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			
			LOG.info("Create cart: magento statusCode ={}",statusCode);
			
			if(200 == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String str = "";
				String output;
				while ((output = br.readLine()) != null) {
					str += output;
				}
				JSONObject cartid = new JSONObject();
				cartid.put("cartid", str);
				createGuestCartRes.put("statusCode", statusCode);
				createGuestCartRes.put("message", cartid);
			}else if(400 == statusCode){
				createGuestCartRes.put("statusCode", statusCode);
				createGuestCartRes.put("message", httpResponse.getEntity().getContent().toString());
				LOG.error("Error while  create cart. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while ceate cart. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("createGuestCart method caught an exception " + e);
		}
		LOG.debug("createGuestCart method end  createGuestCartRes={}: " + createGuestCartRes);
		return createGuestCartRes;
	}





}

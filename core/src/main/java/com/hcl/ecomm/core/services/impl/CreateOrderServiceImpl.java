package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.CreateOrderService;

import com.hcl.ecomm.core.services.LoginService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;

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
		service = CreateOrderService.class)

public class CreateOrderServiceImpl implements CreateOrderService{

	private static final Logger LOG = LoggerFactory.getLogger(CreateOrderServiceImpl.class);

	@Activate
	private MagentoServiceConfig config;

	@Reference
	LoginService loginService;


	@Override
	public String getCreateOrderPath() {
		return config.createOrder_servicePath_string();
	}

	@Override
	public JSONObject createOrderItem(JSONObject orderItem, String cartId) {

		LOG.debug("createOrderItem method={}",orderItem,cartId);
		String scheme = "http";
		JSONObject createOrderItemRes = new JSONObject();
		try {

			String domainName = loginService.getDomainName();
			String createOrderPath = getCreateOrderPath();

			createOrderPath = createOrderPath.replace("{cartId}", cartId);
			String url = scheme + "://" + domainName + createOrderPath;
			LOG.info("createOrderInfo url ={}",url);
			Integer statusCode;

			StringEntity input = new StringEntity(orderItem.toString(),ContentType.APPLICATION_JSON);
			LOG.info("input is {}",input);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPut httput = new HttpPut(url);
			httput.setEntity(input);
			CloseableHttpResponse httpResponse = httpClient.execute(httput);
			LOG.info("httpResponse is {}",httpResponse);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			LOG.info("createOrder Info : magento statusCode ={}",statusCode);

			if(org.eclipse.jetty.http.HttpStatus.OK_200 == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String order="";
				String output;

				while ((output = br.readLine()) != null) {

					order +=output;
				}
				JSONObject orderId = new JSONObject();
				orderId.put("orderId",order);
				createOrderItemRes.put("statusCode", statusCode);
				createOrderItemRes.put("message", orderId);
			}else if(HttpStatus.SC_BAD_REQUEST == statusCode){
				createOrderItemRes.put("statusCode", statusCode);
				createOrderItemRes.put("message", httpResponse.getEntity().getContent().toString());
				LOG.error("Error while create Order Info . status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while create Order Info. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("createOrderCart method caught an exception " + e);
		}
		LOG.debug("createOrderCart method {}: " + createOrderItemRes);
		return createOrderItemRes;
	}



}

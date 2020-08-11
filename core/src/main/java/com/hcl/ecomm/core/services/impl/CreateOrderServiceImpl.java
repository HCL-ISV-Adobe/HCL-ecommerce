package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.CreateOrderService;

import com.hcl.ecomm.core.services.CustomEmailService;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;
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
import java.util.HashMap;
import java.util.Map;

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

	@Reference
	CustomEmailService customEmailService;


	@Override
	public String getCreateOrderPath() {
		return config.createOrder_servicePath_string();
	}

	@Override
	public JSONObject createOrderItem(JSONObject orderItem, String cartId, String customerToken) {

		LOG.debug("createOrderItem method={}",orderItem,cartId);
		String scheme = "http";
		String token="";
		String url = "";
		Integer statusCode=0;
		JSONObject createOrderItemRes = new JSONObject();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse httpResponse = null;
		StringEntity input = new StringEntity(orderItem.toString(),ContentType.APPLICATION_JSON);
		try {
			String domainName = loginService.getDomainName();
			if(customerToken != null && !customerToken.isEmpty()) {
				token = customerToken;
				url = scheme + "://" + domainName + config.customer_createOrder_string() ;
				HttpPost httpost = new HttpPost(url);
				httpost.setHeader("Authorization", "Bearer " +token);
				httpost.setEntity(input);

				httpResponse = httpClient.execute(httpost);
				LOG.debug("httpResponse is {}",httpResponse);
				statusCode = httpResponse.getStatusLine().getStatusCode();

			}
			else
			{
				String createOrderPath = getCreateOrderPath();
				createOrderPath = createOrderPath.replace("{cartId}", cartId);
				url = scheme + "://" + domainName + createOrderPath;
				HttpPut httput = new HttpPut(url);
				httput.setEntity(input);
				httpResponse = httpClient.execute(httput);
				LOG.debug("httpResponse is {}",httpResponse);
				statusCode = httpResponse.getStatusLine().getStatusCode();
			}
			LOG.debug("createOrderInfo url ={}",url);

			if(org.eclipse.jetty.http.HttpStatus.OK_200 == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String order="";
				String output;

				while ((output = br.readLine()) != null) {
					order +=output;
				}
				JSONObject orderId = new JSONObject();

				String order_Id=order.replaceAll("\\s", "").replaceAll("\"", "");
				LOG.debug("order_Id: {}",order_Id);

				String authToken = loginService.getToken();
				url = scheme + "://" + domainName + "/us/V1/orders/"+order_Id;
				LOG.info("createOrderInfo url ={}",url);
				HttpGet httpGet = new HttpGet(url);
				httpGet.setHeader("Content-Type", "application/json");
				httpGet.setHeader("Authorization", "Bearer " + authToken);
				CloseableHttpResponse Httpresponse = httpClient.execute(httpGet);
				statusCode = Httpresponse.getStatusLine().getStatusCode();
				if(org.eclipse.jetty.http.HttpStatus.OK_200 == statusCode){
					String orderRes = EntityUtils.toString(Httpresponse.getEntity());
					JSONObject jsonRes = new JSONObject();
					Map emailParams = new HashMap<>();
					jsonRes = new JSONObject(orderRes);
					if(jsonRes.length()!=0 && customerToken != null && !customerToken.isEmpty()) {
						emailParams.put("orderId", order_Id);
						emailParams.put("grandTotal", jsonRes.get("base_grand_total"));
						emailParams.put("address",jsonRes.getJSONObject("billing_address").get("street"));

						//send Email
						String templatePath="/etc/notification/email/hclecomm/order-confirmation-email-template.html";
						String smail=jsonRes.getString("customer_email");
						String firstname=jsonRes.getString("customer_firstname");
						emailParams.put("receiveremail",smail);
						emailParams.put("firstname",firstname);
						customEmailService.sendEmail(templatePath,emailParams,smail);
					}
				}else if(org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400 == statusCode){
					LOG.error("Error while  getting customer Orders. status code:{} and message={}",statusCode,Httpresponse.getEntity().getContent().toString());
				}else{
					LOG.error("Error while getting customer orders. status code:{}",statusCode);
				}
				orderId.put("orderId",order_Id);
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

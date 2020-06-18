package com.hcl.ecomm.core.services.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hcl.ecomm.core.services.DeleteCartItemService;

@Component(
		immediate = true,
		enabled = true,
		service = DeleteCartItemService.class)

public class DeleteCartItemServiceImpl implements DeleteCartItemService{

	private static final Logger LOG = LoggerFactory.getLogger(DeleteCartItemServiceImpl.class);

	@Activate
	private MagentoServiceConfig config;

	@Reference
	LoginService loginService;

	@Override
	public String getGuestCartItemDeletePath() {
		return config.deleteCartService_guestCartItemDeletePath();
	}

	@Override
	public JSONObject deleteCartItem(String cartId, String itemId, String customerToken) {
		LOG.debug("deleteCartItem method start.deleteCartItem={} and itemId={}",cartId,itemId);
		String scheme = "http";
		String url = "";
		String token = "";
		JSONObject deleteCartItemRes = new JSONObject();

		try {
			String domainName = loginService.getDomainName();
			String itemDeletePath = "";

			if(customerToken != null && !customerToken.isEmpty()) {
				token = customerToken;
				//itemDeletePath = config.customer_deleteCart_string();
				itemDeletePath= config.customer_deleteCart_string().replace("{item-id}", itemId);
				url = scheme + "://" + domainName + itemDeletePath ;

			}
			else
			{
				token = loginService.getToken();
				itemDeletePath = getGuestCartItemDeletePath();
				itemDeletePath= itemDeletePath.replace("{cartId}", cartId).replace("{itemId}", itemId);
				url = scheme + "://" + domainName + itemDeletePath;
			}

			LOG.info("itemDeletePath  : " + url);
			Integer statusCode;
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpDelete httppost = new HttpDelete(url);
			httppost.setHeader("Authorization", "Bearer " +token);
			CloseableHttpResponse httpResponse = httpClient.execute(httppost);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			LOG.info("Delete guest cart item: magento statusCode ={}",statusCode);

			if(HttpStatus.OK_200 == statusCode){
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				String str = "";
				String output;
				while ((output = br.readLine()) != null) {
					str += output;
				}
				JSONObject cartid = new JSONObject();
				cartid.put("isDeleted", str);
				deleteCartItemRes.put("statusCode", statusCode);
				deleteCartItemRes.put("message", cartid);
			}else if(HttpStatus.BAD_REQUEST_400 == statusCode){
				deleteCartItemRes.put("statusCode", statusCode);
				deleteCartItemRes.put("message", httpResponse.getEntity().getContent().toString());
				LOG.error("Error while  create cart. status code:{} and message={}",statusCode,httpResponse.getEntity().getContent().toString());
			}else{
				LOG.error("Error while ceate cart. status code:{}",statusCode);
			}
		} catch (Exception e) {
			LOG.error("createGuestCart method caught an exception " + e);
		}
		LOG.debug("createGuestCart method end  createGuestCartRes={}: " + deleteCartItemRes);
		return deleteCartItemRes;
	}

}

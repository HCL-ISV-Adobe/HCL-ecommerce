package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.LoginServiceConfig;
import com.hcl.ecomm.core.services.LoginService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = LoginService.class, immediate = true)
@Designate(ocd = LoginServiceConfig.class)

@ServiceDescription("Login service")
@ServiceRanking(1001)
@ServiceVendor("HCL")

public class LoginServiceImpl implements LoginService {

	private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Activate
	private LoginServiceConfig config;

	@Override
	public String getDomainName() {
		return config.loginservice_domainName_string();
	}

	@Override
	public String getServicePath() {
		return config.loginservice_servicePath_string();
	}

	@Override
	public String getUsername() {
		return config.loginservice_username_string();
	}

	@Override
	public String getPassword() {
		return config.loginservice_password_string();
	}

	@Override
	public String getToken() {

		String scheme = "http";
		String token = null;

		String domainName = getDomainName();
		String servicePath = getServicePath();
		String username = getUsername();
		String password = getPassword();

		String url = scheme + "://" + domainName + servicePath + "?username=" + username + "&password=" + password;
		LOG.info("URL formed : " + url);

		try {

			
			CloseableHttpClient httpClient = HttpClients.createDefault();

			HttpPost httppost = new HttpPost(url);
			CloseableHttpResponse httpResponse = httpClient.execute(httppost);
			token = EntityUtils.toString(httpResponse.getEntity());
			LOG.info("Response Token : " + token);

		} catch (Exception e) {
			LOG.error("getToken method caught an exception " + e.getMessage());

		}

		return token;
	}
}

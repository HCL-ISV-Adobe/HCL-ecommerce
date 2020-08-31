package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MailChimpConfig;
import com.hcl.ecomm.core.services.MailChimpService;
import com.hcl.ecomm.core.utility.ProductUtility;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = MailChimpService.class, immediate = true)
@Designate(ocd = MailChimpConfig.class)
public class MailChimpServiceImpl implements MailChimpService {

    private static final Logger LOG = LoggerFactory.getLogger(MailChimpServiceImpl.class);

    String username = null;
    String password = null;
    String serverPrefix = null;
    String domain = null;

    @Activate
    public void activate(MailChimpConfig config) {
        username=config.mailChimp_Username();
        password = config.mailChimp_Password();
        serverPrefix = config.mailchimp_ServerPrefix();
        domain= config.mailChimp_Domain();
    }
    @Override
    public String getMailChimpUsername() {
        return username;
    }

    @Override
    public String getMailChimpPassword() {
        return password;
    }

    @Override
    public String getMailChimpServerPrefix() {
        return serverPrefix;
    }

    @Override
    public String getMailChimpDomain() {
        return domain;
    }

    JSONObject customerSignupResponse = new JSONObject();

    @Override
    public JSONObject mailChimpCustomerSignup(JSONObject customerDetails) {
        String url = "https://" + serverPrefix + "." + domain + "lists/6bcf7b7a39/members/";

        String authString = username + ":" + password;
        String authStringEnc = ProductUtility.toBase64(authString);
        String authorToken = "Basic " + authStringEnc;
        String finalToken = authorToken.replaceAll("\"", "");
        StringEntity customerInput = new StringEntity(customerDetails.toString(), ContentType.APPLICATION_JSON);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", finalToken);
        httpPost.setEntity(customerInput);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                customerSignupResponse.put("statusCode", statusCode);
                customerSignupResponse.put("message", httpResponse.getEntity().getContent().toString());
            }
            else {
                CloseableHttpResponse httpResponse1 = httpClient.execute(httpPost);
                Integer statusCode1 = httpResponse1.getStatusLine().getStatusCode();
                if (HttpStatus.SC_OK == statusCode1) {
                    customerSignupResponse.put("statusCode", statusCode1);
                    customerSignupResponse.put("message", httpResponse1.getEntity().getContent().toString());
                } else if(HttpStatus.SC_BAD_REQUEST == statusCode1){
                    customerSignupResponse.put("statusCode", statusCode1);
                    customerSignupResponse.put("message", httpResponse1.getEntity().getContent().toString());
                    LOG.error("Error while MailChimp customer Signup. status code:{} and message={}",statusCode1,httpResponse1.getEntity().getContent().toString());
                } else {
                    LOG.error("Error while MailChimp customer Signup. status code:{}",statusCode1);
                }
            }
        } catch (Exception e) {
            LOG.error("Error while executing mailChimpCustomerSignup() method. Error={} ",e);
        }
        LOG.debug("MailChimpSignupObject method end  customerSignupResponse={}: ", customerSignupResponse);
        return customerSignupResponse;
    }
}


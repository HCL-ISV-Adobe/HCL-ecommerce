package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.MailChimpConfig;
import com.hcl.ecomm.core.services.MailChimpService;
import com.hcl.ecomm.core.utility.ProductUtility;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

@Component(service = MailChimpService.class, immediate = true)
@Designate(ocd = MailChimpConfig.class)
public class MailChimpServiceImpl implements MailChimpService {

    private static final Logger LOG = LoggerFactory.getLogger(MailChimpServiceImpl.class);

    /*@Activate
    private MailChimpConfig config;*/

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


    String responseStream = null;

    @Override
    public String mailChimpCustomerSignup(JSONObject customerDetails) {
        String url = "https://" + serverPrefix + "." + domain + "lists/6bcf7b7a39/members/";
        LOG.info("URL for Mailchimp :" + url);

        String authString = username + ":" + password;

        String authStringEnc = ProductUtility.toBase64(authString);

        String authorToken = "Basic " + authStringEnc;
        String finalToken = authorToken.replaceAll("\"", "");
        LOG.info("Encoded authentication : "+authStringEnc);
        StringEntity customerInput = new StringEntity(customerDetails.toString(), ContentType.APPLICATION_JSON);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", finalToken);
        httpPost.setEntity(customerInput);

        try {

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            LOG.info("HTTP Response : "+httpResponse);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                responseStream = EntityUtils.toString(httpResponse.getEntity());
                LOG.info("Response : "+responseStream);
            }
            else {
                CloseableHttpResponse httpResponse1 = httpClient.execute(httpPost);
                LOG.info("HTTP Response11111 : "+httpResponse1);
                if (httpResponse1.getStatusLine().getStatusCode() == 200) {
                    responseStream = EntityUtils.toString(httpResponse1.getEntity());
                    LOG.info("Response1111 : "+responseStream);
                }
                else {
                    responseStream = "Error in signing up new customer in mailchimp";
                    LOG.info("Error Response111111 : "+responseStream);
                }
            }

        } catch (Exception e) {
            LOG.error("Exception : "+e.getMessage());
            e.printStackTrace();
        }

        return responseStream;
    }


}


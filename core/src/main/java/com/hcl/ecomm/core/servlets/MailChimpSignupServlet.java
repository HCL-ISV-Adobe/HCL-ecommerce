package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.MailChimpService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component(service = Servlet.class,
        property = {
                "sling.servlet.paths=/bin/hclecomm/mailChimpCustomerSignup",
                "sling.servlet.method=" + HttpConstants.METHOD_POST,
                "sling.servlet.extensions=json"
        }
)
public class MailChimpSignupServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(MailChimpSignupServlet.class);

    @Reference
    private MailChimpService mailChimpService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        LOG.debug("MailChimpSignupServlet doPost()  method start.");
        JSONObject responseObject = new JSONObject();

        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();

            if (StringUtils.isNotEmpty(payload)) {
                JSONObject jsonPayload = new JSONObject(payload);
                if (isValidPayload(jsonPayload)) {
                    JSONObject customerValidDetails = customerDetailsAligned(jsonPayload);
                    JSONObject mailChimpCustomerSignupResponse = getMailChimpSignup(customerValidDetails);
                    if (mailChimpCustomerSignupResponse.has("statusCode") && mailChimpCustomerSignupResponse.getInt("statusCode") == HttpStatus.OK_200) {
                        responseObject.put("message", mailChimpCustomerSignupResponse.getJSONObject("message"));
                        responseObject.put("status", Boolean.TRUE);
                    } else {
                        responseObject.put("message", "something went wrong while MailChimp Customer Signup.");
                        LOG.error("Error while MailChimp customer Signup. response:{}",mailChimpCustomerSignupResponse.toString());
                    }
                }
            }
            response.getWriter().print(responseObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LOG.debug("MailChimpSignupServlet doPost()  method end.");
    }

    private JSONObject getMailChimpSignup(JSONObject customerDetails) throws UnsupportedEncodingException {
        return mailChimpService.mailChimpCustomerSignup(customerDetails);
    }

    private boolean isValidPayload(JSONObject jsonPayload) {
        boolean isValidData=Boolean.TRUE;
        if(!jsonPayload.has("email") && !jsonPayload.has("firstname") && !jsonPayload.has("lastname")){
            isValidData = Boolean.FALSE;
        }
        return isValidData;
    }

    private JSONObject customerDetailsAligned(JSONObject customerData){
        JSONObject customerSignup = new JSONObject();
        JSONObject customer = new JSONObject();
        try {
            customer.put("FNAME", customerData.getString("firstname"));
            customer.put("LNAME", customerData.getString("lastname"));
            customer.put("PHONE", customerData.getString("phone"));
            customerSignup.put("merge_fields", customer);
            customerSignup.put("email_address",customerData.getString("email") );
            customerSignup.put("status","subscribed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customerSignup;
    }
}

package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Customer Address Servlet",
                "sling.servlet.paths=/bin/hclecomm/addAddress",
                "sling.servlet.method=" + HttpConstants.METHOD_POST,
                "sling.servlet.extensions=json"})
public class MyAccountAddAddress extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 2731083214874663626L;
    private static final Logger LOG = LoggerFactory.getLogger(MyAccountAddAddress.class);

    @Reference
    private CustomerService customerService;

    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {
            LOG.debug("Inside doPut Method in MyAccountAddress Servlet");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject responseObject = new JSONObject();
            responseObject.put("message", "Request failed");
            responseObject.put("status", Boolean.FALSE);

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String payload = buffer.toString();
            String customerToken = request.getHeader("CustomerToken");

            if (StringUtils.isNotEmpty(payload)) {
                JSONObject jsonPayload = new JSONObject(payload);
                LOG.info("Shipping Address inside If", jsonPayload);
                if (isValidPayload(jsonPayload, customerToken)) {
                    JSONObject customerAddress = jsonItemObj( jsonPayload);
                    LOG.debug("customerAddress:: "+customerAddress);

                    JSONObject customerProfileAddress = customerService.customerProfileAddDetails(customerToken,customerAddress);
                    LOG.debug("customerProfileAddress after servlet"+customerProfileAddress.toString());
                    if (customerProfileAddress.has("statusCode") && customerProfileAddress.getInt("statusCode") == HttpStatus.OK_200) {
                        responseObject.put("message", customerProfileAddress.getJSONObject("message"));
                        responseObject.put("status", Boolean.TRUE);
                    } else {
                        responseObject.put("message", "something went wrong while customerProfileAddDetails");
                    }

                }
            } else {
                responseObject.put("message", "Missing Parameter in payload.");
                responseObject.put("status", Boolean.FALSE);
            }
            response.getWriter().print(responseObject);

        } catch (Exception e) {
            LOG.debug("Exception while Putting Customer Address details: " + e.getMessage());
        }

    }

    private boolean isValidPayload(JSONObject jsonPayload, String customerToken) {
        boolean isValidData = Boolean.TRUE;
        if (!jsonPayload.has("first_name") && (null == customerToken || customerToken == "")) {
            isValidData = Boolean.FALSE;
        }
        return isValidData;
    }

    private JSONObject jsonItemObj(JSONObject shipData) throws JSONException {
        LOG.debug("Inside jsonItemObj response"+shipData);
        JSONObject customer= new JSONObject();
        JSONObject innerResponse=new JSONObject();
        JSONArray addresses=new JSONArray();
        JSONObject address = new JSONObject();
        try {

            address.put("city", shipData.getString("city"));
            address.put("country_id", shipData.getString("country_id"));
            address.put("firstname", shipData.getString("firstname"));
            address.put("lastname", shipData.getString("lastname"));
            address.put("postcode", shipData.getString("postcode"));
            address.put("region", shipData.getString("region"));
            address.put("region_id", 0);
            address.put("street", shipData.get("street"));
            address.put("telephone", shipData.getString("telephone"));
            address.put("default_billing", true);
            address.put("default_shipping", true);
            LOG.debug("address:: "+address.toString());
            addresses.put(address);
            LOG.debug("address:: "+addresses.toString());

            innerResponse.put("email",shipData.getString("email"));
            innerResponse.put("firstname", shipData.getString("first_name"));
            innerResponse.put("lastname", shipData.getString("last_name"));
            innerResponse.put("websiteId", 0);
            innerResponse.put("addresses", addresses);

            LOG.debug("innerResponse "+innerResponse.toString());
            customer.put("customer",innerResponse);
            LOG.debug("customer ::"+customer.toString());

        } catch (JSONException e) {
            LOG.error("Error while executing. Error={}", e);
        }
        return customer;
    }
}

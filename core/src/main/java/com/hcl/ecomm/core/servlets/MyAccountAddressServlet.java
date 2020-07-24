package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.CustomerService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(
        service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Customer Address Servlet",
                "sling.servlet.paths=/bin/hclecomm/customerAddress",
                "sling.servlet.method=" + HttpConstants.METHOD_GET,
                "sling.servlet.extensions=json"})
public class MyAccountAddressServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 2731083214874663626L;
    private static final Logger LOG = LoggerFactory.getLogger(MyAccountAddressServlet.class);

    @Reference
    private CustomerService customerService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        String addressDetails = "";
        String customerToken = request.getHeader("CustomerToken");
        try {
            LOG.debug("customerToken value : " + customerToken);
            if (customerToken != null) {
                JSONObject customerProfileAddress = customerService.customerProfile(customerToken);
                JSONObject message = customerProfileAddress.getJSONObject("message");
                LOG.debug("message : " + message);
                addressDetails = message.get("addresses").toString();
            } else {
                addressDetails = "Customer Token is null";
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF8");
            response.getWriter().write(addressDetails);
        } catch (Exception e) {
            LOG.debug("Exception while fetching Customer Address details: " + e.getMessage());
        }
    }
}

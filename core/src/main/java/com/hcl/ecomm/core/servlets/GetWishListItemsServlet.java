package com.hcl.ecomm.core.servlets;

import com.hcl.ecomm.core.services.AddToWishListService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class, property = { "sling.servlet.paths=/bin/hclecomm/getWishList",
        "sling.servlet.method=" + HttpConstants.METHOD_GET, "sling.servlet.extensions=json" })
public class GetWishListItemsServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger(GetWishListItemsServlet.class);

    @Reference
    private AddToWishListService WishListService;

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {
        try {
            String customerToken = request.getHeader("CustomerToken");
          //  String WishListItems = null;
            JSONObject responseObject = new JSONObject();
            JSONObject responseStream = WishListService.getWishListItems(customerToken);
            LOG.info("responseStream is {}", responseStream.toString());

            response.setContentType("application/json");
            responseObject.put("message",responseStream.get("message"));
            responseObject.put("status", responseStream.get("statusCode"));
            response.getWriter().print(responseObject);

        }
        catch (Exception e){
            LOG.error("error in product servlet {} ",e.getMessage());
           // response.setStatus(500);
        }

    }
}

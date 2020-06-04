package com.hcl.ecomm.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class ,
            property = {"sling.servlet.paths=/bin/hclecomm/applyCoupon",
                    "sling.servlet.method=" + HttpConstants.METHOD_GET,
                    "sling.servlet.extensions=json"})

public class CouponServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 4016057296495129474L;
    private static final Logger LOG = LoggerFactory.getLogger("CouponServlet.class");

    String COUPON_LIST_PATH = "/etc/acs-commons/lists/coupon-list";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String couponApplied = request.getParameter("coupon");
        String responseString = "";
        ResourceResolver resourceResolver = request.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page listPage = pageManager.getPage(COUPON_LIST_PATH);
        Resource resource = listPage.adaptTo(Resource.class);
        Resource childResources = resource.getChild("jcr:content").getChild("list");

        Iterator<Resource> children = childResources.listChildren();
        while (children.hasNext()) {
            Resource child = children.next();
            ValueMap childNodeProperty = child.getValueMap();
            String couponCode = (String)childNodeProperty.get("jcr:title");
            if(couponApplied.equals(couponCode))
            {
                responseString = (String)childNodeProperty.get("value");
                break;
            }
        }
        response.getWriter().write(responseString);
    }
}


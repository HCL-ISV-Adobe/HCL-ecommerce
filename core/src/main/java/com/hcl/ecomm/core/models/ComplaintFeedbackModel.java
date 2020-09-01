package com.hcl.ecomm.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcl.ecomm.core.services.MySqlService;

@Model(adaptables = SlingHttpServletRequest.class)
public class ComplaintFeedbackModel {

	private static final Logger LOG = LoggerFactory.getLogger(ComplaintFeedbackModel.class);

 	@Inject
 	MySqlService mySqlService;

 	@Inject
 	private SlingHttpServletRequest slingHttpServletRequest;
 	
 	String complaintId;
 	
 	String satisfiedValue;

    @PostConstruct
    protected void init() {
    	LOG.debug("ComplaintFeedbackModel init method start");
    	String status= "Closed";
    	 complaintId = slingHttpServletRequest.getParameter("complaintId");
    	 satisfiedValue  = slingHttpServletRequest.getParameter("satisfied");
    	LOG.debug("ComplaintFeedbackModel complaintId ={} and satisfied={}",complaintId,satisfiedValue);
    	if (StringUtils.isNoneEmpty(complaintId) && StringUtils.isNoneEmpty(satisfiedValue)) {
    		if ("no".equalsIgnoreCase(satisfiedValue)) {
    			status = "Reopen";
			}
    		mySqlService.userComplaintFeedback(status, complaintId);
			
		} else {
			LOG.error("Either complaintId or satisfied value is null");
		}
    	LOG.debug("ComplaintFeedbackModel init method end");

    }

	public String getComplaintId() {
		return complaintId;
	}

	public String getSatisfiedValue() {
		return satisfiedValue;
	}
    
    
	
}

package com.hcl.ecomm.core.models;

import com.hcl.ecomm.core.services.CommonService;
import com.hcl.ecomm.core.services.CustomerService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class RecaptchaSiteKeyModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    CommonService commonService;

    private String siteKey;

    @PostConstruct
    protected void init(){

        logger.debug("In init method of RecaptchaSiteKey model.");

        siteKey = commonService.getRecaptchaSiteKey();

    }

    public String getSiteKey() {
        return siteKey;
    }
}

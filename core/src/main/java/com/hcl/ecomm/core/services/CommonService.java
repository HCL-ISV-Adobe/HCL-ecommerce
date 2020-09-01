package com.hcl.ecomm.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface CommonService {

    public String getRecaptchaSiteKey();
    
    public ResourceResolver getHclecommResourceResolver();

}

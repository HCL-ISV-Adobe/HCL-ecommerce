package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.CommonServiceConfig;
import com.hcl.ecomm.core.services.CommonService;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,enabled = true,service = CommonService.class)
public class CommonServiceImpl implements CommonService {

    private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Activate
    private CommonServiceConfig config;
    
    @Reference
	ResourceResolverFactory resourceResolverFactory;
	

    @Override
    public String getRecaptchaSiteKey() { return  config.getRecaptchaSiteKey_string();}
    
    @Override
	public ResourceResolver getHclecommResourceResolver() {
		ResourceResolver resourceResolver = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, "userName");
		try {
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
		} catch (Exception e) {
			LOG.error("Error while getting ResourceResolver Error-{}", e);
		}
		return resourceResolver;
	}

}

package com.hcl.ecomm.core.services.impl;

import com.hcl.ecomm.core.config.CommonServiceConfig;
import com.hcl.ecomm.core.services.CommonService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true,enabled = true,service = CommonService.class)
public class CommonServiceImpl implements CommonService {

    private static final Logger LOG = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Activate
    private CommonServiceConfig config;

    @Override
    public String getRecaptchaSiteKey() { return  config.getRecaptchaSiteKey_string();}
}

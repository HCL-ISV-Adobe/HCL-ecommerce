package com.hcl.ecomm.core.services.impl;

import com.day.cq.dam.api.Asset;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.ecomm.core.config.MagentoServiceConfig;
import com.hcl.ecomm.core.services.PincodeService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, enabled = true,service = PincodeService.class)

public class PincodeServiceImpl implements PincodeService {

    private static final Logger LOG = LoggerFactory.getLogger(PincodeServiceImpl.class);
    @Activate
    private MagentoServiceConfig config;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Override
    public JSONArray getPincodeLocation() {
        JSONArray pincodelist=new JSONArray();
        Resource original;
        try{
            Map<String,Object>param=new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE,"userName");
            ResourceResolver resourceResolver=resourceResolverFactory.getServiceResourceResolver(param);
            Resource resource=resourceResolver.getResource(config.getPincodeLocationPath_string());
            Asset asset=resource.adaptTo(Asset.class);
            original = asset.getOriginal();
            InputStream content = original.adaptTo(InputStream.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readValue(content,JsonNode.class);
            pincodelist=new JSONArray(jsonNode.toString());
            }
        catch (Exception e) {
            LOG.error("error while executing getPincodeLocation() method in PincodeServiceImpl. Error={}" + e);
        }
      return pincodelist;
    }
}

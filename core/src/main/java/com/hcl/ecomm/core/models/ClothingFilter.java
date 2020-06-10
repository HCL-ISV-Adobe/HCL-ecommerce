package com.hcl.ecomm.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
@Model(adaptables = Resource.class)
public class ClothingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private String clothes;

    public String getClothes() {
        return clothes;
    }

    public void setClothes(String clothes) {
        this.clothes = clothes;
    }
    @PostConstruct
    protected void init() {
        logger.debug("In init of ClothingFilter Model");
    }
}

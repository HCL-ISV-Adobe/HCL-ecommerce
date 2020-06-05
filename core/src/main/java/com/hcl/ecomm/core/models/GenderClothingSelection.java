package com.hcl.ecomm.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
@Model(adaptables = Resource.class)
public class GenderClothingSelection {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private List<Resource> clothingFilter;

    @Inject @Optional
    private String selectClothing;

    public String getSelectClothing() {
        return selectClothing;
    }

    public void setSelectClothing(String selectClothing) {
        this.selectClothing = selectClothing;
    }

    private List<ClothingFilter> clothingList = new ArrayList<>();

    public List<ClothingFilter> getClothingList() {
        return clothingList;
    }

    public void setClothingList(List<ClothingFilter> clothingList) {
        this.clothingList = clothingList;
    }
    @PostConstruct
    protected void init() {
        logger.debug("In init method of GenderClothingSelection model.");
        if(!clothingFilter.isEmpty()) {
            for (Resource resource : clothingFilter) {
                ClothingFilter clothes = resource.adaptTo(ClothingFilter.class);
                clothingList.add(clothes);
            }
        }
    }

}

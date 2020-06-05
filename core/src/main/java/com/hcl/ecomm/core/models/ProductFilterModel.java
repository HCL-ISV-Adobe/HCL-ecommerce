package com.hcl.ecomm.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class ProductFilterModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private String filter;


    @Inject @Optional
    private List<Resource> selectGenderClothing;

    public String getFilter() {
        return filter;
    }

    private List<GenderClothingSelection> genderclothingList = new ArrayList<>();


    public List<GenderClothingSelection> getGenderclothingList() {
        return genderclothingList;
    }

    public void setGenderclothingList(List<GenderClothingSelection> genderclothingList) {
        this.genderclothingList = genderclothingList;
    }


    @PostConstruct
    protected void init() {
        logger.debug("In init of ProductFilterModel");
        if (!selectGenderClothing.isEmpty()) {
            for (Resource resource : selectGenderClothing) {
                GenderClothingSelection genderClothing = resource.adaptTo(GenderClothingSelection.class);
                genderclothingList.add(genderClothing );
            }
        }

    }
}

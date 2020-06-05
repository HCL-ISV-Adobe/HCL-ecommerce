package com.hcl.ecomm.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class ProductFilterModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private String filter;
    @Inject @Optional
    private String categories;

    @Inject @Optional
    private String clothing;

    @Inject @Optional
    private String brand;

    @Inject @Optional
    private List<Resource> selectBrand ;

    @Inject @Optional
    private List<Resource> selectGenderClothing;

    public String getFilter() {
        return filter;
    }

    public String getCategories() {
        return categories;
    }

    public String getClothing() {
        return clothing;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    private List<GenderClothingSelection> genderclothingList = new ArrayList<>();

    private List<BrandFilter> brandList = new ArrayList<>();

    public List<GenderClothingSelection> getGenderclothingList() {
        return genderclothingList;
    }

    public void setGenderclothingList(List<GenderClothingSelection> genderclothingList) {
        this.genderclothingList = genderclothingList;
    }

    public List<BrandFilter> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<BrandFilter> brandList) {
        this.brandList = brandList;
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
        if (!selectBrand.isEmpty()) {
            for (Resource resource : selectBrand) {
                BrandFilter brand = resource.adaptTo(BrandFilter.class);
                brandList.add(brand);
            }
        }

    }
}

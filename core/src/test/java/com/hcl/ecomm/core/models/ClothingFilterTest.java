package com.hcl.ecomm.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(AemContextExtension.class)
class ClothingFilterTest {

    private ClothingFilter clothingFilter;
    private Resource resource;

    @BeforeEach
    public void setup(AemContext context) throws Exception {
        context.addModelsForClasses(ClothingFilter.class);
        context.load().json("/ClothingFilter.json", "/content");
        context.currentPage("/content/page-1");
        resource = context.currentResource("/content/page-1/jcr:content/root/responsivegrid/productfilter/selectGenderClothing/item0/clothingFilter/item0");
        clothingFilter = resource.adaptTo(ClothingFilter.class);
    }
    @Test
    void getClothes() {
        String expected = "Apparels";
        String actual = clothingFilter.getClothes();
        assertEquals(expected, actual);
    }

}
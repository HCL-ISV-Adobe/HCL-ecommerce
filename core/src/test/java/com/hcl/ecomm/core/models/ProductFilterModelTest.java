package com.hcl.ecomm.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(AemContextExtension.class)
class ProductFilterModelTest {

    private ProductFilterModel productFilterModel;

    private Resource resource;

    private final AemContext context = new AemContext();

    @Test
    void getFilter() {
        context.addModelsForClasses(ProductFilterModel.class);
        context.load().json("/ProductFilterModel.json", "/content");
        context.currentPage("/content/page-1");
        resource = context.currentResource("/content/page-1/jcr:content/root/responsivegrid/productfilter");
        productFilterModel = resource.adaptTo(ProductFilterModel.class);
        String expected = "Filter";
        String actual = productFilterModel.getFilter();
        assertEquals(expected, actual);
    }

    @Test
    void getGenderClothingList() {
        context.addModelsForClasses(ProductFilterModel.class);
        context.load().json("/ProductFilterModel.json", "/content");
        context.currentPage("/content/page-1");
        resource = context.currentResource("/content/page-1/jcr:content/root/responsivegrid/productfilter");
        productFilterModel = resource.adaptTo(ProductFilterModel.class);
        List<GenderClothingSelection> actual = productFilterModel.getGenderclothingList();
        assertFalse(actual.isEmpty());
    }
}
package com.hcl.ecomm.core.models;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
   //Junit for GenderClothingSelectionModel
@ExtendWith(AemContextExtension.class)
class GenderClothingSelectionTest {

    private GenderClothingSelection genderClothingSelection;
    private ClothingFilter clothingFilter;
    private Resource resource;
    private final AemContext context=new AemContext();
    List<ClothingFilter>clothingFilterList=new ArrayList<>();


    @Test
    public void getSelectClothing()
    {
        context.addModelsForClasses(GenderClothingSelection.class);
        context.load().json("/GenderClothingSelection.json","/content");
        context.currentPage("/content/GenderClothing");
        resource= context.currentResource("/content/GenderClothing/jcr:content/root/responsivegrid/productfilter/selectGenderClothing/item0");
        if (resource == null) throw new AssertionError();
        genderClothingSelection=resource.adaptTo(GenderClothingSelection.class);
        String expected="Men";
        String actual=genderClothingSelection.getSelectClothing();
        assertEquals(expected,actual);
    }



    @Test
    void getClothingList() {
        context.addModelsForClasses(GenderClothingSelection.class);
        context.load().json("/GenderClothingSelection.json","/content");
        context.currentPage("/content/GenderClothing");
        resource=context.currentResource("/content/GenderClothing/jcr:content/root/responsivegrid/productfilter/selectGenderClothing/item0/clothingFilter/item0");
        if (resource == null) throw new AssertionError();
        genderClothingSelection=resource.adaptTo(GenderClothingSelection.class);
        clothingFilterList.add(clothingFilter);
        genderClothingSelection.setClothingList(clothingFilterList);
        List<ClothingFilter>actual=genderClothingSelection.getClothingList();
        assertFalse(actual.isEmpty());

    }
}
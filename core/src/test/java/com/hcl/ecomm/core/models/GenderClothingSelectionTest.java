package com.hcl.ecomm.core.models;
import junitx.util.PrivateAccessor;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



class GenderClothingSelectionTest {

    GenderClothingSelection gc=new GenderClothingSelection();
    ClothingFilter clfilter=new ClothingFilter();
    List<ClothingFilter>clf=new ArrayList<>();
    Resource resource;


    List<Resource> clothingFilter=new ArrayList<>();
    String selectClothing="shirt";


    @Test
    public void init() throws Exception{
        resource=mock(Resource.class);
        clothingFilter.add(resource);
        PrivateAccessor.setField(gc,"clothingFilter",clothingFilter);
        when(resource.adaptTo(ClothingFilter.class)).thenReturn(clfilter);

        PrivateAccessor.setField(gc,"selectClothing",selectClothing);
        gc.setSelectClothing(selectClothing);
        gc.getSelectClothing();

       clf.add(clfilter);
        gc.setClothingList(clf);
        gc.getClothingList();
    }
}
package com.hcl.ecomm.core.models;

import com.hcl.ecomm.core.pojo.ListPojo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class StaticListModelTest {

    private Resource resource;
    private StaticListModel staticListModel;
    private final AemContext context = new AemContext();

    @Test
    void getListItems() {
        context.addModelsForClasses(StaticListModel.class);
        context.load().json("/StaticListModel.json", "/content");
        context.currentPage("/content/ListPage");
        resource = context.currentResource("/content/ListPage/jcr:content/root/responsivegrid/list");
        if (resource != null)
            staticListModel = resource.adaptTo(StaticListModel.class);
        assert staticListModel != null;
        List<ListPojo> actual = staticListModel.getListItems();
        assertFalse(actual.isEmpty());
    }
}
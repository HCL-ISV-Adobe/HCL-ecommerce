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
public class StaticListModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private List<Resource> staticList;
    private List<Listmodel> listItems = new ArrayList<>();

    public List<Listmodel> getListItems() {
        return listItems;
    }

    public void setListItems(List<Listmodel> listItems) {
        this.listItems = listItems;
    }

    @PostConstruct
    protected void init() {
        logger.debug("In init of StaticListModel");
        try {
            if (!staticList.isEmpty()) {
                for (Resource resource : staticList) {
                    Listmodel staticlist = resource.adaptTo(Listmodel.class);
                    listItems.add(staticlist);
                }
            }
        }catch (Exception e){
            logger.error("Exception encountered",e.getMessage());
        }


    }
}


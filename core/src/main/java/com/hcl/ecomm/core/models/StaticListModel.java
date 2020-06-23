package com.hcl.ecomm.core.models;

import com.hcl.ecomm.core.pojo.ListPojo;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@Model(adaptables = Resource.class)
public class StaticListModel {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject @Optional
    private  Resource staticList;

    private List<ListPojo> listItems;



    @PostConstruct
    protected void init() {
        logger.debug("In init of StaticListFilterModel");
        try {
            if(null!= staticList && staticList.hasChildren() )
            {
                listItems = new ArrayList<>();
                Iterator<Resource> listChildren= staticList.listChildren();
                while(listChildren.hasNext())
                {
                    ListPojo bean=new ListPojo();
                    Resource resource=listChildren.next();
                    ValueMap valueMap= resource.getValueMap();
                    bean.setListitem(valueMap.get("itemList", String.class));
                    listItems.add(bean);
                }
            }

        }catch (Exception e){
            logger.error("Exception encountered",e.getMessage());
        }

    }

    public List<ListPojo> getListItems() {
        return listItems;
    }

}


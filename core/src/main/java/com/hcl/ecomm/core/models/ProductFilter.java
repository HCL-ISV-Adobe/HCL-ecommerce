package com.hcl.ecomm.core.models;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Iterator;
        import java.util.List;
        import javax.annotation.PostConstruct;
        import javax.inject.Inject;
        import org.apache.sling.api.resource.Resource;
        import org.apache.sling.api.resource.ValueMap;
        import org.apache.sling.models.annotations.Model;
        import org.apache.sling.models.annotations.Optional;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

@Model(adaptables=Resource.class)
public class ProductFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ProductFilter.class);

    @Inject @Optional
    private String filter;
    @Inject @Optional
    private String categories;
    @Inject @Optional
    private String clothing;
    @Inject @Optional
    private String mensClothing;
    @Inject @Optional
    private  Resource mensClothingFilter;


    private List<String> filters;

    private  HashMap<String, String> clothesMap;

    public String getFilter() {
        return filter;
    }
    public String getCategories() {

        return categories;
    }
    public String getClothing() {
        return clothing;
    }

    public String getMensClothing() {
        return mensClothing;
    }


    public HashMap<String, String> getClothesMap() {
        return clothesMap;
    }


    @PostConstruct
    private void init() {
        if(null!= mensClothingFilter && mensClothingFilter.hasChildren() )
        {
            filters =new ArrayList<>();
            Iterator<Resource>listChildren=mensClothingFilter.listChildren();
            while(listChildren.hasNext())
            {
                FilterBean bean=new FilterBean();
                Resource resource=listChildren.next();
                ValueMap valueMap= resource.getValueMap();
                bean.setSelectclothes(valueMap.get("clothes", String.class));
                filters.add(bean.toString());

            }
        }

        clothesMap = new HashMap<String, String>();
        clothesMap.put(filters.get(0),"t-shirt");
        clothesMap.put(filters.get(1),"shirts");
        clothesMap.put(filters.get(2),"jeans");
        clothesMap.put(filters.get(3),"sport-wear");
        clothesMap.put(filters.get(4),"trouser");

        LOG.info(" mensClothingResponse is {}", clothesMap);

    }
    public class FilterBean{

        private String Selectclothes;
        public String getSelectclothes() {
            return Selectclothes;
        }

        public void setSelectclothes(String selectclothes) {
            Selectclothes = selectclothes;
        }


        @Override
        public String toString() {
            return Selectclothes;
        }
    }
}


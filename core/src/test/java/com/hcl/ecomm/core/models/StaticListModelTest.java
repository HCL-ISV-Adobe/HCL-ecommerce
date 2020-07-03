package com.hcl.ecomm.core.models;
import com.hcl.ecomm.core.pojo.ListPojo;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;


class StaticListModelTest {

 ListPojo lp=new ListPojo();
 List<ListPojo>listPojos=new ArrayList<>();
 StaticListModel sm=new StaticListModel();


    @Test
    public void getListItems(){
        lp.setListitem("page1");
        listPojos.add(lp);
        sm.getListItems();
    }
}
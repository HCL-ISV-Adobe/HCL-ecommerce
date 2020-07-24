package com.hcl.ecomm.core.services;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ShippingInfoService {

    public String getDomainName();

    public String getShippingInfoPath();

    public JSONObject createShipInfo(JSONObject shipItem,String cartId, String customerToken);

    public JSONArray getStateCountryList();

    public JSONObject getPickupStoreList();




}

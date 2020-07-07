package com.hcl.ecomm.core.services;


import com.google.gson.JsonObject;
import org.json.JSONObject;

public interface WishListService {
    public String getDomainName();

    public String getAddToWishListPath();

    public JSONObject addToWishList(String sku, String custToken);

    public String getWishListItemsPath();

    public JsonObject getWishListItems(String custToken);

}

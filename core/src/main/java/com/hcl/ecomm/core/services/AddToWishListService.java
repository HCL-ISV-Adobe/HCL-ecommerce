package com.hcl.ecomm.core.services;

import com.google.gson.JsonObject;
import org.json.JSONObject;

public interface AddToWishListService {
    public String getDomainName();

    public String getAddToWishListPath();

    public JSONObject addToWishList(JSONObject sku, String custToken);

    public String getWishListItemsPath();

    public JSONObject getWishListItems(String custToken);
}

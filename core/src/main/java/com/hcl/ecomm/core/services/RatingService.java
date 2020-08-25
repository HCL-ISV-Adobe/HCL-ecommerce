package com.hcl.ecomm.core.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface RatingService {

    public List getRatingDataSQL(String sku);
    public JSONObject saveRating(JSONObject ratItem,String email,String sku);


}

package com.hcl.ecomm.core.services;

import org.json.JSONObject;
import com.hcl.ecomm.core.pojo.Ratings;

import java.util.List;

public interface RatingService {

    public List<Ratings> getRatingDataSQL(String sku);
    public JSONObject saveRating(JSONObject ratItem,String email,String sku);


}

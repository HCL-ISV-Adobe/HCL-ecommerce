package com.hcl.ecomm.core.services;

import com.hcl.ecomm.core.pojo.Ratings;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public interface RatingService {

    public List<Ratings> getRatingDataSQL(String sku);
    public JSONObject saveRating(JSONObject  ratItem);

}

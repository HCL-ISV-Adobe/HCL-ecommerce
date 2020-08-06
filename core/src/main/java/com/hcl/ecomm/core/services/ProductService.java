package com.hcl.ecomm.core.services;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface ProductService {

	public String getServicePath();

	public String getSearchCriteriaField();

	public String getSearchCriteriaValue();

	public JSONArray getAllProductDetails() throws JSONException;

	public List<String> getAllProductSkus(JsonArray productJson);

	public JSONObject getProductDetail(String sku);


}

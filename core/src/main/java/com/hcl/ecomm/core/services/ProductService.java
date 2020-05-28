package com.hcl.ecomm.core.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface ProductService {

	public String getServicePath();

	public String getSearchCriteriaField();

	public String getSearchCriteriaValue();

	public JsonArray getAllProductDetails();

	public List<String> getAllProductSkus(JsonArray productJson);

	public JsonObject getProductDetail(String sku);


}

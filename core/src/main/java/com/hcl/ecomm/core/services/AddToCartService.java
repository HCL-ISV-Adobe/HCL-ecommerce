package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface AddToCartService {
	
	public String getDomainName();
	
	public String getEmptyCartPath();
	
	public String getAddToCartPath();
	
	public JSONObject addToCart(JSONObject product);
	
	public String createGuestCart(JSONObject product);

}

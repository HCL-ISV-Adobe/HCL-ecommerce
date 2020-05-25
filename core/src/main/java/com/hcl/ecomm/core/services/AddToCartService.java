package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface AddToCartService {
	
	public String getDomainName();
	
	public String getAddToCartPath();
	
	public String updateItemQtytPath();
	
	public JSONObject addToCart(JSONObject product);
	
	public JSONObject updateCartItem(JSONObject item, String itemId);
	

}

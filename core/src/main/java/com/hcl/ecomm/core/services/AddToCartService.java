package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface AddToCartService {
	
	public String getDomainName();
	
	public String getAddToCartPath();
	
	public String updateCartItemPath();
	
	public JSONObject addToCart(JSONObject product, String custToken);
	
	public JSONObject updateCartItem(JSONObject item, String itemId, String custToken);
	

}

package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface DeleteCartItemService {
	
	public String getDomainName();
	
	public String getGuestCartItemDeletePath();
	
	public JSONObject deleteCartItem(String cartId, String itemId);

}

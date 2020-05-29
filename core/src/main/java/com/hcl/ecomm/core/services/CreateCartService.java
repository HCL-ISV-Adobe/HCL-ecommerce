package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface CreateCartService {
	
	public String getDomainName();
	
	public String getEmptyCartPath();
	
	public JSONObject createGuestCart();

}

package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface CreateOrderService {
	
	public String getCreateOrderPath();
	
	public JSONObject createOrderItem(JSONObject orderItem,String cartId);

}

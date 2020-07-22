package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface OrderService {
    public JSONObject getOrders(String customerEmail);
}

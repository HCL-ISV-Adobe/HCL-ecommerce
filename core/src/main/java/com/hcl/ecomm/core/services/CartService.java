package com.hcl.ecomm.core.services;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

public interface CartService {

    String getServicePath();

    public int getCartItemCount(String cartId, String custToken);
    public JSONArray getCartItemsDetails(String cartId, String custToken);
    public String updateCartDetails(String payload, String custToken);
    public String applyCoupon(String coupon);
    public JSONObject getCustomerCart(String custToken);
}

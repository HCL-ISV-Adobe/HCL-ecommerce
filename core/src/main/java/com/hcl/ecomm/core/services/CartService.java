package com.hcl.ecomm.core.services;

import com.google.gson.JsonArray;

public interface CartService {

    String getServicePath();

    public int getCartItemCount(String cartId, String custToken);
    public JsonArray getCartItemsDetails(String cartId, String custToken);
    public String updateCartDetails(String payload, String custToken);
    public String applyCoupon(String coupon);
    public JsonArray getCustomerCart(String custToken);
}

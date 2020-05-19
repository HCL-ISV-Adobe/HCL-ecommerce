package com.hcl.ecomm.core.services;

import com.google.gson.JsonArray;

public interface CartService {

    String getDomainName();

    String getServicePath();

    public String getCartDetails(String cartId);
}

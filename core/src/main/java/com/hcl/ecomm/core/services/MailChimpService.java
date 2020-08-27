package com.hcl.ecomm.core.services;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public interface MailChimpService {

    public String getMailChimpUsername();

    public String getMailChimpPassword();

    public String getMailChimpServerPrefix();

    public String getMailChimpDomain();

    public String mailChimpCustomerSignup(JSONObject customerDetails) throws UnsupportedEncodingException;
}

package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface CustomerService {

	public String getDomainName();
	
	public String customerSignupServicePath();
	
	public String customerSigninServicePath();

	public JSONObject customerSignup(JSONObject signupObject);
	
	public JSONObject customerSignin(JSONObject signObject);	
}

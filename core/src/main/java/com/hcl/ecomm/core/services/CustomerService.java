package com.hcl.ecomm.core.services;

import org.json.JSONObject;

public interface CustomerService {

	public String getDomainName();
	
	public String customerSignupServicePath();
	
	public String customerSigninServicePath();

	public String customerProfileServicePath();

	public String getRecaptchaSiteKey();

	public JSONObject customerSignup(JSONObject signupObject);
	
	public JSONObject customerSignin(JSONObject signObject);	
	
	public JSONObject customerProfile(String customerToken);

	public JSONObject customerProfileAddDetails(String customerToken,JSONObject payload);
}

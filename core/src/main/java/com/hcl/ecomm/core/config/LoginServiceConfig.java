package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Login Service", description = "Component Usage Report Configurations")
public @interface LoginServiceConfig {

	String servicePathDefaultValue = "/us/V1/integration/admin/token";
	String domainNameDefaultValue = "localhost/magento2/rest";
	String userNameDefaultValue = "admin";
	String passwordDefaultValue = "Admin123";

	@AttributeDefinition(name = "DOMAIN_NAME", description = "This is domain name", defaultValue = {domainNameDefaultValue} )
	String loginservice_domainName_string() default domainNameDefaultValue;
	
	@AttributeDefinition(name = "SERVICE_PATH", description = "This is the API path", defaultValue = {servicePathDefaultValue} )
	String loginservice_servicePath_string() default servicePathDefaultValue;

	@AttributeDefinition(name = "USERNAME", description = "USERNAME", defaultValue = {userNameDefaultValue} )
	String loginservice_username_string() default userNameDefaultValue;

	@AttributeDefinition(name = "PASSWORD", description = "PASSWORD", defaultValue ={ passwordDefaultValue} )
	String loginservice_password_string() default passwordDefaultValue;

}
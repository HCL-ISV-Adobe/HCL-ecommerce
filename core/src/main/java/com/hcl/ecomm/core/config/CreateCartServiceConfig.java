package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Magento Create Cart Service Configuration")
public @interface CreateCartServiceConfig {

	String domainNameDefaultValue = "127.0.0.1/magento2";
	String emptyCartPah = "/rest/us/V1/guest-carts";
	

	@AttributeDefinition(name = "DOMAIN NAME", description = "This is domain name", defaultValue = domainNameDefaultValue, type = AttributeType.STRING)
	String cartService_domainName() default domainNameDefaultValue;

	@AttributeDefinition(name = "SERVICE_PATH", description = "This is create empty cart API path", defaultValue = emptyCartPah, type = AttributeType.STRING)
	String cartService_emptyCartPath() default emptyCartPah;

}

package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Magento Cart Service Configuration")
public @interface AddToCartServiceConfig {

	String domainNameDefaultValue = "127.0.0.1/magento2";
	String addToCartPath = "/rest/us/V1/guest-carts/{cartId}/items";
	

	@AttributeDefinition(name = "DOMAIN NAME", description = "This is domain name", defaultValue = domainNameDefaultValue, type = AttributeType.STRING)
	String cartService_domainName() default domainNameDefaultValue;

	@AttributeDefinition(name = "SERVICE_PATH", description = "This is add to cart API path", defaultValue = addToCartPath, type = AttributeType.STRING)
	String cartService_addToCartPath() default addToCartPath;




}

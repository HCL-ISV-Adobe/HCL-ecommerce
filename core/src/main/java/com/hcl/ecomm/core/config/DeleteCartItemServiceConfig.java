package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Magento Cart item delete"
		+ ""
		+ " Service Configuration")
public @interface DeleteCartItemServiceConfig {

	String domainNameDefaultValue = "127.0.0.1/magento2";
	String guestCartItemDeletePah = "/rest/us/V1/guest-carts/{cartId}/items/{itemId}";
	

	@AttributeDefinition(name = "DOMAIN NAME", description = "This is domain name", defaultValue = domainNameDefaultValue, type = AttributeType.STRING)
	String deleteCartService_domainName() default domainNameDefaultValue;

	@AttributeDefinition(name = "GUEST_CART_ITEM_DELETE_SERVICE_PATH", description = "This is guest cart item delete API path", defaultValue = guestCartItemDeletePah, type = AttributeType.STRING)
	String deleteCartService_guestCartItemDeletePath() default guestCartItemDeletePah;
}

package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Cart Service Configuration", description = "cart fetch service configuration")
public @interface CartServiceConfig {

    String servicePathCartFetchItems = "us/V1/guest-carts/";

    @AttributeDefinition(name = "SERVICE_PATH", description = "This is the API path", defaultValue = {servicePathCartFetchItems} )
    String servicePath_string() default servicePathCartFetchItems;
}

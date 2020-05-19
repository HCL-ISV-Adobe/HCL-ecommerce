package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Cart Service Configuration", description = "cart fetch service configuration")
public @interface CartServiceConfig {

    String servicePathDefaultValue = "/V1/guest-carts/";
    String domainNameDefaultValue = "localhost:8081/magento/rest";

    @AttributeDefinition(name = "DOMAIN_NAME", description = "This is domain name", defaultValue = {domainNameDefaultValue} )
    String domainName_string() default domainNameDefaultValue;

    @AttributeDefinition(name = "SERVICE_PATH", description = "This is the API path", defaultValue = {servicePathDefaultValue} )
    String servicePath_string() default servicePathDefaultValue;
}

package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Magento Graphql API Service Configuration")
public @interface MagentoServiceConfig {
    String domainNameDefaultValue = "localhost/magento2/rest";
    String servicePathDefaultValue = "/us/V1/products";
    String searchFieldDefaultValue = "store_id";
    String searchFieldValueDefaultValue = "2";
    String servicePathCartFetchItems = "/us/V1/guest-carts/";

    @AttributeDefinition(name = "DOMAIN NAME", description = "This is domain name", defaultValue = domainNameDefaultValue, type = AttributeType.STRING)
    String productService_domainName() default domainNameDefaultValue;

    @AttributeDefinition(name = "SERVICE_PATH", description = "This is the API path", defaultValue = servicePathDefaultValue, type = AttributeType.STRING)
    String productService_servicePath() default servicePathDefaultValue;

    @AttributeDefinition(name = "Search Criteria Field", description = "This is the field for search criteria", defaultValue = searchFieldDefaultValue, type = AttributeType.STRING)
    String productService_searchCriteriaField() default searchFieldDefaultValue;

    @AttributeDefinition(name = "Search Criteria Value", description = "This is the value for search criteria", defaultValue = searchFieldValueDefaultValue, type = AttributeType.STRING)
    String productService_searchCriteriaValue() default searchFieldValueDefaultValue;

    @AttributeDefinition(name = "SERVICE_PATH", description = "This is the API path", defaultValue = {servicePathCartFetchItems} )
    String cartFetch_servicePath_string() default servicePathCartFetchItems;

}


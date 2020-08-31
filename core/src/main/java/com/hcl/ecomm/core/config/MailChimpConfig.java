package com.hcl.ecomm.core.config;


import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "MailChimp Configuration Service")
public @interface MailChimpConfig {

    String MAILCHIMP_USERNAME = "anystring";
    String MAILCHIMP_PASSWORD = "2142c8fcc43cee451e4f5b1be8899a78-us17";
    String MAILCHIMP_SERVER_PREFIX = "us17";
    String MAILCHIMP_DOMAIN = "api.mailchimp.com/3.0/";

    @AttributeDefinition(name = "Username", type = AttributeType.STRING, defaultValue = MAILCHIMP_USERNAME )
    String mailChimp_Username() default MAILCHIMP_USERNAME;

    @AttributeDefinition(name = "Password", type = AttributeType.STRING, defaultValue = MAILCHIMP_PASSWORD)
    String mailChimp_Password() default MAILCHIMP_PASSWORD;

    @AttributeDefinition(name = "Domain", type = AttributeType.STRING, defaultValue = MAILCHIMP_DOMAIN)
    String mailChimp_Domain() default MAILCHIMP_DOMAIN;

    @AttributeDefinition(name = "Server Prefix", type = AttributeType.STRING, defaultValue = MAILCHIMP_SERVER_PREFIX)
    String mailchimp_ServerPrefix() default MAILCHIMP_SERVER_PREFIX;
}
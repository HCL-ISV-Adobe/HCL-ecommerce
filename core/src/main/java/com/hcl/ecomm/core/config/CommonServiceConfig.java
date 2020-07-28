package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
public @interface CommonServiceConfig {

    String recaptchaSiteKey = "6LfrFKQUAAAAAMzFobDZ7ZWy982lDxeps8cd1I2i";

    @AttributeDefinition(name = "Recaptcha Site Key", description = "This is for getting the Recaptcha Site Key", defaultValue = recaptchaSiteKey,type = AttributeType.STRING)
    String getRecaptchaSiteKey_string() default recaptchaSiteKey;
}

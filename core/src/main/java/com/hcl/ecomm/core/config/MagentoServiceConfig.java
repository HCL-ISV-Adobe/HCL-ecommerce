package com.hcl.ecomm.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Magento API's Service Configuration")
public @interface MagentoServiceConfig {

    String servicePathDefaultValue = "/us/V1/products";
    String searchFieldDefaultValue = "store_id";
    String searchFieldValueDefaultValue = "2";
    String servicePathCartFetchItems = "/us/V1/guest-carts/";

	String addToCartPath = "/us/V1/guest-carts/{cartId}/items";
	String updateCartItemPath = "/us/V1/guest-carts/{cartId}/items/{ItemId}";
	String emptyCartPah = "/us/V1/guest-carts";
	String guestCartItemDeletePah = "/us/V1/guest-carts/{cartId}/items/{itemId}";
    String servicePathCartUpdateItems = "/V1/guest-carts/";


    @AttributeDefinition(name = "All Products Service Path", description = "This is the API path for getting all products for a store", defaultValue = servicePathDefaultValue, type = AttributeType.STRING)
    String productService_servicePath() default servicePathDefaultValue;

    @AttributeDefinition(name = "Product Product Search Criteria Field", description = "This is the field for search criteria", defaultValue = searchFieldDefaultValue, type = AttributeType.STRING)
    String productService_searchCriteriaField() default searchFieldDefaultValue;

    @AttributeDefinition(name = "Search Criteria Value", description = "This is the value for search criteria", defaultValue = searchFieldValueDefaultValue, type = AttributeType.STRING)
    String productService_searchCriteriaValue() default searchFieldValueDefaultValue;

    @AttributeDefinition(name = "Get Cart Items Service Path", description = "This is the API path for getting cart item details", defaultValue = servicePathCartFetchItems, type = AttributeType.STRING )
    String cartFetch_servicePath_string() default servicePathCartFetchItems;
    
	@AttributeDefinition(name = "ADD_TO_CART_SERVICE_PATH", description = "This is add to cart API path", defaultValue = addToCartPath, type = AttributeType.STRING)
	String magentoService_addToCartPath() default addToCartPath;
	
	@AttributeDefinition(name = "UPDATE_ITEM_QUANTITY_SERVICE_PATH", description = "This is update cart item API path", defaultValue = updateCartItemPath, type = AttributeType.STRING)
	String magentoService_updateCartItemPath() default updateCartItemPath;

	@AttributeDefinition(name = "SERVICE_PATH", description = "This is create empty cart API path", defaultValue = emptyCartPah, type = AttributeType.STRING)
	String magentoService_emptyCartPath() default emptyCartPah;

    @AttributeDefinition(name = "GUEST CART ITEM DELETE SERVICE PATH", description = "This is guest cart item delete API path", defaultValue = guestCartItemDeletePah, type = AttributeType.STRING)
    String deleteCartService_guestCartItemDeletePath() default guestCartItemDeletePah;

    @AttributeDefinition(name = "Update Cart Items Service Path", description = "This is the API path for getting cart item details", defaultValue = servicePathCartUpdateItems, type = AttributeType.STRING )
    String cartUpdate_servicePath_string() default servicePathCartUpdateItems;


}


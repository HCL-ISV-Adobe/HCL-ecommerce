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
  String couponListPath = "/etc/acs-commons/lists/coupon-list";
  String servicePathCartUpdateItems = "/V1/guest-carts/";
  String guestCartShippingInformationPath = "/us/V1/guest-carts/{cartId}/shipping-information";
  String guestCartCreateOrderPath = "/us/V1/guest-carts/{cartId}/order";
    //Customer API
  String customerSignupPath = "/V1/customers";
  String customerSigninPath = "/V1/integration/customer/token";
  String customerProfilePath = "/V1/customers/me";
    String customerCreateCartPath = "/V1/carts/mine";
    String customerAddToCartPath = "/V1/carts/mine/items";
    String customerUpdateCartPath = "/V1/carts/mine/items";
    String customerDeleteCartPath = "/V1/carts/mine/items/{item-id}";
    String customerGetCartPath = "/V1/carts/mine/items";
    String customerShippingInfoPath = "/V1/carts/mine/shipping-information";
    String customerCreateOrderPath = "/V1/carts/mine/payment-information";

  String customerAddToWishListPath = "/us/V1/wishlist/sku";
  String customerGetWishListPath = "/us/V1/wishlist";



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
    
    @AttributeDefinition(name = "Customer Signup Service Path", description = "This is the API path for new customer signup", defaultValue = customerSignupPath, type = AttributeType.STRING )
    String customerService_signupPath() default customerSignupPath;
    
    @AttributeDefinition(name = "Customer Signin Service Path", description = "This is the API path for signin", defaultValue = customerSigninPath, type = AttributeType.STRING )
    String customerService_signinPath() default customerSigninPath;
    
    @AttributeDefinition(name = "Customer Profile Service Path", description = "This is the API path for customer profile", defaultValue = customerProfilePath, type = AttributeType.STRING )
    String customerService_profilePath() default customerProfilePath;

    @AttributeDefinition(name = "Shipping Information Service Path", description = "This is the API path for getting shipping information details", defaultValue = guestCartShippingInformationPath, type = AttributeType.STRING )
    String shippingInfo_servicePath_string() default guestCartShippingInformationPath;

    @AttributeDefinition(name = "Create Order Service Path", description = "This is the API path for Create Order", defaultValue = guestCartCreateOrderPath, type = AttributeType.STRING )
    String createOrder_servicePath_string() default guestCartCreateOrderPath;

    @AttributeDefinition(name = "COUPON_LIST", description = "This is path of Coupon list under ACS-Commons list", defaultValue = {couponListPath} )
    String getCouponlistPath_string() default couponListPath;

    @AttributeDefinition(name = "Customer Create Cart Service Path", description = "This is the API path for creating customer's cart", defaultValue = customerProfilePath, type = AttributeType.STRING )
    String customer_createCart_string() default customerCreateCartPath;

    @AttributeDefinition(name = "Customer Add To Cart Service Path", description = "This is the API path for adding product into customer's cart", defaultValue = customerProfilePath, type = AttributeType.STRING )
    String customer_addToCart_string() default customerAddToCartPath;

    @AttributeDefinition(name = "Customer Update Cart Item Service Path", description = "This is the API path for updating item of customer'cart", defaultValue = customerProfilePath, type = AttributeType.STRING )
    String customer_updateCart_string() default customerUpdateCartPath;

    @AttributeDefinition(name = "Customer Delete Cart Item Service Path", description = "This is the API path for deleting item from customer's cart", defaultValue = customerProfilePath, type = AttributeType.STRING )
    String customer_deleteCart_string() default customerDeleteCartPath;

    @AttributeDefinition(name = "Customer Get Cart Service Path", description = "This is the API path for getting customer cart", defaultValue = customerProfilePath, type = AttributeType.STRING )
   String customer_getCart_string() default customerGetCartPath;

  @AttributeDefinition(name = "Customer Shipping Info Service Path", description = "This is the API path for customer shipping info", defaultValue = customerProfilePath, type = AttributeType.STRING )
  String customer_shippingInfo_string() default customerShippingInfoPath;

  @AttributeDefinition(name = "Customer Create Order Service Path", description = "This is the API path for creating order for customer", defaultValue = customerProfilePath, type = AttributeType.STRING )
  String customer_createOrder_string() default customerCreateOrderPath;

  @AttributeDefinition(name = "Customer Add To WishList Service Path", description = "This is the API path for adding product into customer's wishlist", defaultValue = customerAddToWishListPath, type = AttributeType.STRING )
  String customer_addToWishList_string() default customerAddToWishListPath;

  @AttributeDefinition(name = "Customer Get wishlist Service Path", description = "This is the API path for getting customer wishlist", defaultValue = customerGetWishListPath, type = AttributeType.STRING )
  String customer_getWishList_string() default customerGetWishListPath;


}


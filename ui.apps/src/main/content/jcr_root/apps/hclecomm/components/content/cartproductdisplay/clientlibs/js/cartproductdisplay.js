let totalBagPrice = Number($('.total-bag-count').text());
let bagDiscount = Number($('.bag-discount-amount').text());;
let deliveryCharges = Number($('.delivery-charges').text());
let placeOrderRedirection = null;
let discount = 0;
let discountedPercentage = discount/100;

let allowCouponOnce = true;

function onApplyCoupon(){

    const getApppliedCoupon = $('.cart-detail-container__apply-coupon').val();
	if(getApppliedCoupon === ''){
        $('.apply-coupon-validation').text('Please enter a coupon').css("color", "red");
        if(!allowCouponOnce)
       		removeCoupon();
			return;
    }
            const xhttp = new XMLHttpRequest();
    		discount = "";
            xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                if(this.responseText)
                     discount = this.responseText;
                	 removeCoupon();
                     if(discount)
                    {
                            discountedPercentage = discount/100;
                            const calculatedPrice = (totalBagPrice * (1-discountedPercentage)) - bagDiscount;
                            const discountedPrice = (totalBagPrice * discountedPercentage );
                            $('.coupon-discount').css("display","flex");
                            $('.coupon-discount-amount').text(discountedPrice.toFixed(2));
                            $('.order-price').text(calculatedPrice.toFixed(2));
                            $('.total-price').text((calculatedPrice + deliveryCharges).toFixed(2));
                            $('.apply-coupon-validation').text('Coupon Applied Sucessfully').css("color", "green");
                            allowCouponOnce = false;
                    }
                    else{
                        $('.apply-coupon-validation').text('Your Coupon is not valid').css("color", "red");
                         if(!allowCouponOnce)
                            removeCoupon();
                    }
    }
  };
 	xhttp.open("GET", "/bin/hclecomm/applyCoupon?coupon=" +  getApppliedCoupon, true);
    xhttp.send();
}


$(document).ready(function (){
   	 $('.order-price').text(totalBagPrice - bagDiscount);
    if(Number($('.order-price').text())<=0)
    {
		$('.delivery-charges').text(0);
    }
	//deliveryCharges = Number($('.delivery-charges').text());
     $('.total-price').text(totalBagPrice - bagDiscount + Number($('.order-price').text()));
	 placeOrderRedirection = $('.place-order-button').children().children().attr('href');
	 $('.place-order-button').children().children().removeAttr("href");

	if(Number($('.order-price').text())<=0)
    {
		$('.delivery-charges').text(0);
    }

    if(Number($('.bag-discount-amount').text())>0)
		$('.bag-discount').css("display","flex");
    else
		$('.bag-discount').css("display","none");

    if(Number($('.coupon-discount-amount').text())>0)
		$('.coupon-discount').css("display","flex");
    else
		$('.coupon-discount').css("display","none");
});

function onClickUpdateItem() {
	const cartItem = []

	cartId = getCartIdCookie();

    const productData= $('.cmp-cart-items').get();
	productData.forEach((item) =>{
		const itemObj = {
			"quote_id" : cartId,
        	"item_id" : $(item).attr('id').substring(3),
        	"sku" :  $(item).find('.cmp-cart-item-code').text().substring(7),
        	"qty" : $(item).find('.cmp-cart-qty-input')[0].value
		}
    	cartItem.push({cartItem : itemObj})
	})

	const xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        placeOrderRedirection ? window.location.href = placeOrderRedirection : null;
    }
  };
 	xhttp.open("GET", "/bin/hclecomm/updateCartItems?payload=" +  JSON.stringify(cartItem) , true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send();

}

function removeCoupon()
{
			const calculatedPrice = totalBagPrice - bagDiscount;
            $('.coupon-discount').css("display","none");
            $('.order-price').text(calculatedPrice);
            $('.total-price').text(calculatedPrice + deliveryCharges);
			allowCouponOnce = true;
}

function getCartIdCookie()
{
    let cartId = '';
    const getCookies = document.cookie;
	if (getCookies.indexOf('cartId') > -1) {
        const cookiesCartID = getCookies.split(';');
        const currentCookiesIndex = getCookies.indexOf('cartId');
        if (cookiesCartID && cookiesCartID.length > 0) {
            const cartIdArr = cookiesCartID[currentCookiesIndex].split('=')
            cartId = cartIdArr[1];
        }

    }
    return cartId;
}

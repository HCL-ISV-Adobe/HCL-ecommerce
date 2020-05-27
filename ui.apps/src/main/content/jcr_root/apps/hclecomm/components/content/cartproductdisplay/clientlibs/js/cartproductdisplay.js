const totalBagPrice = Number($('.total-bag-count').text());
const bagDiscount = 15;
const deliveryCharges = Number($('.delivery-charges').text());

let allowCouponOnce = true;

function onApplyCoupon(){

    $(document).on('newMessage', function(e, eventInfo) {
  		console.log(eventInfo);
        console.log(e);
	});

    const getApppliedCoupon = $('.cart-detail-container__apply-coupon').val();

    if(getApppliedCoupon === 'HCL2020')
    {
        if(allowCouponOnce) {
            const calculatedPrice = (totalBagPrice * .8 ) - bagDiscount;
            const discountedPrice = (totalBagPrice * .2 );
            $('.coupon-discount').text(discountedPrice);
            $('.order-price').text(calculatedPrice);
            $('.total-price').text(calculatedPrice + deliveryCharges);
            $('.apply-coupon-validation').text('Coupon Applied Sucessfully').css("color", "green");
            allowCouponOnce = false;
        }
        else
        {
			 $('.apply-coupon-validation').text('Coupon already Applied.')
        }
    }
    else if(getApppliedCoupon === ''){
		$('.apply-coupon-validation').text('Please enter a coupon').css("color", "red");
     }
    else{
    	$('.apply-coupon-validation').text('Your Coupon is not valid').css("color", "red");

    }
}


$(document).ready(function (){
   	 $('.order-price').text(totalBagPrice - bagDiscount);
     $('.total-price').text(totalBagPrice - bagDiscount + deliveryCharges);

    $.event.trigger({
    		type: "newMessage",
    		message: "Test"

			});

});

function onClickUpdateItem() {

    console.log("place order");
	const object = { "cartItem": { "quote_id": "4522ebc7d219rre415a8ad1eewr22f73b55", "sku": "Item SKU", "qty": 1 } }
	const xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      /// redirect code here
    }
  };
  xhttp.open("POST", "demo_post.asp", true);
  xhttp.send(object );
}


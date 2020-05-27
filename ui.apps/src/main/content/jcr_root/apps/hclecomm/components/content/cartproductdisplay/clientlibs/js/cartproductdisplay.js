const totalBagPrice = Number($('.total-bag-count').text());
const bagDiscount = 15;
const deliveryCharges = Number($('.delivery-charges').text());
let placeOrderRedirection = null;

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
	 placeOrderRedirection = $('.place-order-button').children().children().attr('href');
	 $('.place-order-button').children().children().removeAttr("href");
    //window.location.replace(placeOrderRedirection);


});

function onClickUpdateItem() {

	const object = {"cartItem":{"quote_id": "AR93aupnz6KYQL786ZEdOAtEtL73lYQq","item_id": 5, "sku": "24-MB01", "qty": 20}}
	const xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        placeOrderRedirection ? window.location.href = placeOrderRedirection : null;
    }
  };
  xhttp.open("GET", "/bin/hclecomm/updateCartItems?payload=" +JSON.stringify(object) , true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	//xhttp.send(JSON.stringify(object));
  xhttp.send();
}


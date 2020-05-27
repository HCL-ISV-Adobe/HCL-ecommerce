const totalBagPrice = Number($('.total-bag-count').text());
const bagDiscount = Number($('.bag-discount-amount').text());;
const deliveryCharges = Number($('.delivery-charges').text());
let placeOrderRedirection = null;
const discount = 10;

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
            const discountedPercentage = discount/100;
            const calculatedPrice = (totalBagPrice * (1-discountedPercentage)) - bagDiscount;
            const discountedPrice = (totalBagPrice * discountedPercentage );
            $('.coupon-discount').css("display","flex");
            $('.coupon-discount-amount').text(discountedPrice);
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
        if(!allowCouponOnce)
       		 removeCoupon();
     }
    else{
    	$('.apply-coupon-validation').text('Your Coupon is not valid').css("color", "red");
		 if(!allowCouponOnce)
        	removeCoupon();
    }
}


$(document).ready(function (){
   	 $('.order-price').text(totalBagPrice - bagDiscount);
     $('.total-price').text(totalBagPrice - bagDiscount + deliveryCharges);
	 placeOrderRedirection = $('.place-order-button').children().children().attr('href');
	 $('.place-order-button').children().children().removeAttr("href");
    //window.location.replace(placeOrderRedirection);

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

	const object = {"cartItem":{"quote_id": "AR93aupnz6KYQL786ZEdOAtEtL73lYQq","item_id": 5, "sku": "24-MB01", "qty": 20}}
	/*const xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        placeOrderRedirection ? window.location.href = placeOrderRedirection : null;
    }
  };
 	xhttp.open("GET", "/bin/hclecomm/updateCartItems?payload=" +  JSON.stringify(object) , true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhttp.send();

    //xhttp.open("POST", "/bin/hclecomm/updateCartItems"  , true);
    //xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    //xhttp.send( JSON.stringify(object));
    */

    jQuery.ajax({
		url: '/bin/hclecomm/updateCartItems',
		type: 'POST',
		data: JSON.stringify(object),
		contentType: 'application/json',
		success: function (respHTML) {


			console.log("responseHTml" +respHTML );
		}

	});

}

function removeCoupon()
{
			const calculatedPrice = totalBagPrice - bagDiscount;
            $('.coupon-discount').css("display","none");
            $('.order-price').text(calculatedPrice);
            $('.total-price').text(calculatedPrice + deliveryCharges);
			allowCouponOnce = true;
}

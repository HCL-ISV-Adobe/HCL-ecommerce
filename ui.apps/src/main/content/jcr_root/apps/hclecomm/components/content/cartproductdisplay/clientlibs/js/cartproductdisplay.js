const totalBagPrice = Number($('.total-bag-count').text());
const bagDiscount = Number($('.bag-discount-amount').text());;
const deliveryCharges = Number($('.delivery-charges').text());
let placeOrderRedirection = null;
let discount = 0;

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
     $('.total-price').text(totalBagPrice - bagDiscount + deliveryCharges);
	 placeOrderRedirection = $('.place-order-button').children().children().attr('href');
	 $('.place-order-button').children().children().removeAttr("href");

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
	const xhttp = new XMLHttpRequest();
  	xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        placeOrderRedirection ? window.location.href = placeOrderRedirection : null;
    }
  };
 	xhttp.open("GET", "/bin/hclecomm/updateCartItems?payload=" +  JSON.stringify(object) , true);
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

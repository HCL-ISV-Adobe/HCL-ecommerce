let productResponse = [];
$(document).ready(function () {

    let userData = getUserCookie("hcluser"); 
    if(userData != "") {
         custToken = JSON.parse(userData).customerToken;
    }

    var value = $('#mydiv').data('custom-property');


    let path= $('#redirect').data('custom-property');

    var msg = $('#msg').data('custom-property');

    var checkmode = $('.cart-detail-container').attr('checkmode');

   if(checkmode == 'edit'){
    	 $('.cartproductdisplay').css('display', 'block');
    }
    else{
		$('.cartproductdisplay').css('display', 'none');
    }
       let cartId = '';

                const getCountEle = document.getElementById("total-item-count");

     const getCookies = document.cookie;
     if (getCookies.indexOf('cartId') > -1) {
        const cookiesCartID = getCookies.split(';');
        cookiesCartID  && cookiesCartID.length >0 ?
        Object.keys(cookiesCartID).forEach((cookiesCartIDitem) =>{
            const splitCookies  = cookiesCartID[cookiesCartIDitem].split('=')
                if(splitCookies[0] === 'cartId' || splitCookies[0] === ' cartId')  {
                cartId= splitCookies[1];
            }
    }):null
        }

        else {

                    			//$(".product").text("shopping cart is empty");
                                //document.cookie = "cartId = j7KaMe1zWFfopDFOVTdFZV0rokpjwzam";
                                //cartId = 'j7KaMe1zWFfopDFOVTdFZV0rokpjwzam';

                }
				let target = document.querySelector(".product");

                if (cartId) {

                                function getLoad(url, callBack) {
                                                var xmlhttp = new XMLHttpRequest();
                                                xmlhttp.onreadystatechange = function () {
                                                                if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                                                                                //console.log('response ', xmlhttp.responseText);
                                                                                try {
                                                                                                var data = JSON.parse(xmlhttp.responseText);
                                                                                } catch (err) {
                                                                                                console.log("Error ", err.message);
                                                                                                return;
                                                                                }
                                                                                callBack(data);
                                                                }
                                                };
                                                xmlhttp.open("GET", url, true);
                                    			      xmlhttp.setRequestHeader("CustomerToken", custToken);
                                                xmlhttp.send();
                                }




                getLoad("/bin/hclecomm/cartproducts?cartId=" +cartId, function (data) {

                                //let target = document.querySelector(".product");
								productResponse = data;

                                let result = data.map((item) => {
                                                return (
                                                                `<div class="cmp-cart-items"  id=PID${item.item_id}>
                                                                <div class="cmp-cart-row">
                                                                 <div class="cmp-cart-item">

                                            <div class="cmp-item-left">
                                                <img class="cmp-cart-item-image" src=${item.image_url}>
                                            </div>
                                            <div class="cmp-item-right">
                                                                  <div class="cmp-item-productInfo">
                                                <span class="cmp-cart-item-title">${item.name}</span>
                                                <span class="cmp-cart-item-code">Code : ${item.sku}</span>
                                                                </div>
                                          <div class="cmp-cart-quantity">
                                            <span class="fa fa-minus"></span>
                                       <input class="cmp-cart-qty-input" type="number" value=${item.qty} id="number" min="1"/>
                                             <span class="fa fa-plus"></span>
                                        </div>
											<div class="cmp-cart-price" style="display:none">${item.price}</div>
                                       <div class="cmp-cart-total"><i class="fa fa-usd" style="margin-right:5px"></i><span>${(item.price*item.qty).toFixed(2)}</span></div>
                                        <div class="cmp-cart-button"> <i class="fa fa-trash" onclick="deleteproduct(event,'${item.item_id}','${item.quote_id}');" ></i> </div>
                                            </div>

                                        </div>
                                        </div>

                                                                                                                                </div>`
                                                );

                                }).join('');
                    			if(target)
                                target.innerHTML = result;
								OnLoadCartCalculation();



                                data.map((it, key) => {
                                         if(document.querySelectorAll(".cmp-cart-items input")[key]){
                                                document.querySelectorAll(".cmp-cart-items input")[key].addEventListener('keyup', qty);
                                }

                                });
                                var mybagcount = bagCount(data);

    							if(mybagcount == 0){



								if(checkmode == 'edit'){
                                    $('.bag-discount-amount').text(0);
									$('.delivery-charges').text(0);
                                     $('.cartproductdisplay').css('display', 'block');
                                    $('.total-price').text(0);
                                    $('.order-price').text(0);
                                }
                                else{
                                        $('.bag-discount-amount').text(10);
										$('.delivery-charges').text(160);
                                        $('.cartproductdisplay').css('display', 'none');
                                        }

                                    $(".product").prepend(("<h4><span >" + msg + "</span></h4>")),emptyRedirect();
									//code for adding custom msg and redirect path

                                    function emptyRedirect(){


                                    setTimeout(function(){
                                                         var url = window.location.href;
                                                         url = url.substring(0, url.indexOf('/content/'));
                                                         url+=path;
                                                        if(url){
                                                              window.location=url+'.html';
                                                                }
                                                         },7000);
                                    }




                                }
    							else{
                                    $('.cartproductdisplay').css('display', 'block');
									 $(".product").prepend(("<h4><span >" + value + "</span>(" + mybagcount + " items)</h4>"));
                                }

                                for (var k = 0; document.querySelectorAll(".cmp-cart-items span.fa").length > k; k++) {
                                                document.querySelectorAll(".cmp-cart-items span.fa")[k].addEventListener('click', qty);
                                                console.log("k", k)


                                }


                });
           }
           else{					
               if(target){
									$('.total-bag-count').text(0);
									$('.bag-discount-amount').text(0);
									$('.delivery-charges').text(0);
                                     //$('.cartproductdisplay').css('display', 'block');
                                    $('.total-price').text(0);
                                    $('.order-price').text(0);

                   target.innerHTML='<p> There is no product in the cart </p>';}
                         }

                function qty(evt) {
                                console.log("inp", this.parentNode.parentNode.parentNode.parentNode.parentNode.id)
                                var tgt = this.getAttribute("class"),
                                                inp = document.querySelector("#" + this.parentNode.parentNode.parentNode.parentNode.parentNode.id + " input"),
                                                inpV;

                                if (tgt == "fa fa-minus") {
                                                var inpValue = parseInt(inp.value);
                                                inpValue--;
                                                (inpValue == 0) ? inpValue = 1: inp.value = inpValue;
                                                inpV = inpValue;
                                } else if (tgt == "fa fa-plus") {
                                                var inpValue = parseInt(inp.value);
                                                console.log("val ", inpValue);
                                                inpValue++;
                                                (inpValue > 9999) ? inpValue = 9999: inp.value = inpValue;
                                                inp.value = inpValue;
                                                inpV = inpValue;
                                } else {
                                                var inVa = inp.value;
                                                if (inVa == 0) {
                                                                inVa = 1;
                                                }
                                                if (inVa > 9999) {
                                                                inVa = 9999;
                                                }

                                                document.querySelector("#" + this.parentNode.parentNode.parentNode.parentNode.parentNode.id + " input").value = inVa;
                                                inpV = inVa;
                                }
                                pTotal(inpV, document.querySelector("#" + this.parentNode.parentNode.parentNode.parentNode.parentNode.id + " .cmp-cart-price").innerHTML, this.parentNode.parentNode.parentNode.parentNode.parentNode.id);
                }


                function pTotal(inpV, price, id) {
				console.log("inpv ",inpV," proce: ",price, " id ",id);
                                document.querySelector("#" + id + " .cmp-cart-total span").innerHTML = (inpV * price).toFixed(2);
                    updateCartTotal();
                }


function updateCartTotal() {

		let getSumOfAllItem = 0
   		const getFinalCount =  $('.cmp-cart-total').children();
   		getFinalCount && getFinalCount.length > 0 ?
   		Object.keys(getFinalCount).forEach((individualItemCount) =>{
        getFinalCount[individualItemCount].textContent ?
        getSumOfAllItem = getSumOfAllItem  + Number(getFinalCount[individualItemCount].textContent) : null;
   }): null;

      totalBagPrice = getSumOfAllItem;

			bagDiscount = Number($('.bag-discount-amount').text());;

			$('.total-bag-count').text(getSumOfAllItem.toFixed(2));
			 $('.delivery-charges').text(deliveryCharges.toFixed(2));
			if(allowCouponOnce)
            {
				$('.order-price').text((getSumOfAllItem - bagDiscount).toFixed(2));
                $('.total-price').text((getSumOfAllItem - bagDiscount + deliveryCharges).toFixed(2));
            }
			else
            {

                const calculatedPrice = (getSumOfAllItem * (1-discountedPercentage)) - bagDiscount;
                const discountedPrice = (getSumOfAllItem * discountedPercentage );

                $('.coupon-discount-amount').text(discountedPrice.toFixed(2));
                $('.order-price').text(calculatedPrice.toFixed(2));
                $('.total-price').text((calculatedPrice + deliveryCharges).toFixed(2));
            }

		}


});

function bagCount(data) {


                console.log("111", data.length);
                var totalitems = data.length;
                $.event.trigger({
                                type: "newMessage",
                                message: totalitems

                });

                return totalitems;


}


function deleteproduct(event, itemId, cartId) {
                $('#PID' + itemId).remove();

                var qs = {
                                "cartId": cartId,
                                "itemId": itemId
                };
                $.ajaxSetup({
    		          beforeSend: function(xhr) {
        		      xhr.setRequestHeader('CustomerToken', custToken);
    			        }
			          })
                jQuery.ajax({
                                url: '/bin/hclecomm/deleteCartItem',
                                type: 'PUT',
                                data: JSON.stringify(qs),
                                contentType: 'application/json',
                                success: function (respHTML) {


                                                window.location.reload(true);
                                }

                });


}

function OnLoadCartCalculation() {
	let total = 0;

    productResponse.forEach((item) =>{
			total += (item.price)*(item.qty);
	})
      totalBagPrice = total;

			bagDiscount = Number($('.bag-discount-amount').text());;

			$('.total-bag-count').text(total.toFixed(2));
			  $('.delivery-charges').text(deliveryCharges.toFixed(2));
			if(allowCouponOnce)
            {
				$('.order-price').text((total - bagDiscount).toFixed(2));
                $('.total-price').text((total - bagDiscount + deliveryCharges).toFixed(2));
            }
			else
            {

                const calculatedPrice = (total * (1-discountedPercentage)) - bagDiscount;
                const discountedPrice = (total * discountedPercentage );

                $('.coupon-discount-amount').text(discountedPrice.toFixed(2));
                $('.order-price').text(calculatedPrice.toFixed(2));
                $('.total-price').text((calculatedPrice + deliveryCharges).toFixed(2));
            }

		}

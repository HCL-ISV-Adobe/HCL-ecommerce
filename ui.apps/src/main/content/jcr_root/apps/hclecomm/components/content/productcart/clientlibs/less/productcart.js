$(document).ready(function () {
                var value = $('#mydiv').data('custom-property');
                let cartId = '';

                //const getCookies = document.cookie;
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
                                                xmlhttp.send();
                                }




                getLoad("/bin/hclecomm/cartproducts?cartId=" +cartId, function (data) {

                                //let target = document.querySelector(".product");


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
                                       <input class="cmp-cart-qty-input" type="number" value=${item.qty} id="number" />                                    
                                             <span class="fa fa-plus"></span>   
                                        </div>
											<div class="cmp-cart-price" style="display:none">${item.price}</div>
                                       <div class="cmp-cart-total"><i class="fa fa-usd" style="margin-right:5px"></i><span>${(item.price*item.qty).toFixed(2)}</span></div>
                                        <div class="cmp-cart-button"> <button class="cmp-btn-danger" type="button" onclick="deleteproduct(event,'${item.item_id}','${item.quote_id}');"><i class="fa fa-trash" ></i></button> </div>
                                            </div>

                                        </div>
                                        </div>

                                                                                                                                </div>`
                                                );

                                }).join('');
                                target.innerHTML = result;



                                data.map((it, key) => {
                                                document.querySelectorAll(".cmp-cart-items input")[key].addEventListener('keyup', qty);

                                });
                                var mybagcount = bagCount(data);
                                console.log(mybagcount);

                                $(".productcart").prepend(("<h4><span >" + value + "</span>(" + mybagcount + " items)</h4>"));

                                for (var k = 0; document.querySelectorAll(".cmp-cart-items span.fa").length > k; k++) {
                                                document.querySelectorAll(".cmp-cart-items span.fa")[k].addEventListener('click', qty);
                                                console.log("k", k)


                                }


                });
           }
           else{

                                    target.innerHTML='<p> There is no product in the cart </p>';
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
                }


                function updateCartTotal() {

                                var cartItemContainer = document.getElementsByClassName('cmp-cart-items')[0];
                                var cartrows = cartItemContainer.getElementsByClassName('cmp-cart-row');

                                for (var i = 0; i < cartrows.length; i++) {
                                                var cartInvididualRow = cartrows[i];
                                                var priceElement = cartInvididualRow.getElementsByClassName('cmp-cart-price')[0];

                                                var qtyElement = cartInvididualRow.getElementsByClassName('cmp-cart-qty-input')[0];
                                                console.log(priceElement, qtyElement);


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

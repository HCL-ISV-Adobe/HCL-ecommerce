  $(document).ready(function() {

	/*var productData = [
                    {
                        "item_id": 5,
                        "sku": "24-WG085",
                        "qty": 1,
                        "name": "Sprite Yoga Strap 6 foot",
                        "price": 14,
                        "product_type": "simple",
                        "quote_id": "j7KaMe1zWFfopDFOVTdFZV0rokpjwzam",
                        "extension_attributes": {
                            "image_url": "http://127.0.0.1/magento2/pub/static/version1589967585/webapi_rest/_view/en_US/Magento_Catalog/images/product/placeholder/.jpg"
                        }
                    },
                    {
                        "item_id": 7,
                        "sku": "mehiwiext",
                        "qty": 1,
                        "name": "Expedition Tech Long-Sleeved Shirt",
                        "price": 54,
                        "product_type": "simple",
                        "quote_id": "j7KaMe1zWFfopDFOVTdFZV0rokpjwzam",
                        "extension_attributes": {
                            "image_url": "http://127.0.0.1/magento2/pub/media/catalog/product\\cache\\84e3ec616dfeead44f09ae682858fa68\\//t/h/thumnail-swiss-polo-tshirt.jpg"
                        }
                    },
        {
                        "item_id": 5,
                        "sku": "24-WG085",
                        "qty": 1,
                        "name": "Sprite Yoga Strap 6 foot",
                        "price": 14,
                        "product_type": "simple",
                        "quote_id": "j7KaMe1zWFfopDFOVTdFZV0rokpjwzam",
                        "extension_attributes": {
                            "image_url": "http://127.0.0.1/magento2/pub/static/version1589967585/webapi_rest/_view/en_US/Magento_Catalog/images/product/placeholder/.jpg"
                        }
                    },
                    {
                        "item_id": 7,
                        "sku": "mehiwiext",
                        "qty": 1,
                        "name": "Expedition Tech Long-Sleeved Shirt",
                        "price": 54,
                        "product_type": "simple",
                        "quote_id": "j7KaMe1zWFfopDFOVTdFZV0rokpjwzam",
                        "extension_attributes": {
                            "image_url": "http://127.0.0.1/magento2/pub/media/catalog/product\\cache\\84e3ec616dfeead44f09ae682858fa68\\//t/h/thumnail-swiss-polo-tshirt.jpg"
                        }
                    }
               ];
*/



      			let cartId = '';

                const getCookies = document.cookie;

                if (getCookies.indexOf('cartId') > -1) {

                    const cookiesCartID = getCookies.split(';');
                    if (cookiesCartID && cookiesCartID.length > 0) {

                        cookiesCartID.forEach(function (cookiesCartIDItem) {
                            if ((cookiesCartIDItem).indexOf(cartId) > -1) {
                                const cartIdArr = cookiesCartIDItem.split('=')
                                cartId = cartIdArr[1];

                            }
                        })
                    }

                 } else {

                    document.cookie = "cartId = j7KaMe1zWFfopDFOVTdFZV0rokpjwzam";
                    cartId = 'j7KaMe1zWFfopDFOVTdFZV0rokpjwzam';
                }



       			if (cartId) {

                    	var xhttp = new XMLHttpRequest();
						xhttp.open("GET", "/bin/hclecomm/cartproducts?cartId=" + cartId, true);
      					xhttp.onreadystatechange = getResponse();
        				xhttp.send();
    				}




                function getResponse(){


                        if (this.readyState == 4 || this.status == 200) 
                        {
                               // document.getElementById('productDataLoad').innerHTML = xhttp.responseText;
                                var response = 	this.responseText;


                                if(response) {

									var productData =  response;
									alert(productData);

                                    var productHtml = productData.map(function(p){


							return `<div id="productDataLoad" class="cmp-cart-items">
                            			<div class="cmp-cart-row">
                                			<div class="cmp-cart-item">

                                            <div class="cmp-item-left">
                                                <img class="cmp-cart-item-image" src=${p.image_url}>
                                            </div>
                                            <div class="cmp-item-right">
                                                <span class="cmp-cart-item-title">${p.name}</span>
                                                <span class="cmp-cart-item-size">Size : ${p.product_type}</span>
                                                <span class="cmp-cart-item-code">Code : ${p.sku}</span>
                                            </div>

                                        </div>

                                        <div class="cmp-cart-quantity">
                                            <span class="fa fa-minus"  onclick="decrementValue()"></span>	
                                                <input class="cmp-cart-qty-input" type="number" value="1" id="number" />                                    
                                             <span class="fa fa-plus"  onclick="incrementValue()"></span>   
                                        </div>
                                        <div class="cmp-cart-price">${p.price}</div>
                                        <div class="cmp-cart-button"> <button class="cmp-btn-danger" type="button"><i class="fa fa-trash"></i></button> </div>
                                    </div>
                        		</div>`; 

      				});
                                    $('#productDataLoad').append(productHtml);


                                }

                        }




                }




      				var removeCartItemsButtons =  document.getElementsByClassName('cmp-btn-danger');

      				//console.log("333",removeCartItemsButtons);

      				for(var i=0;i<removeCartItemsButtons.length;i++) {

						var delButton = removeCartItemsButtons[i];
                        delButton.addEventListener('click',function(event){
							var delButtonclicked = event.target;
                            	delButtonclicked.parentElement.parentElement.remove();
                        });

                        updateCartTotal();

                	}





                });


                    function incrementValue()
                    {
                        var value = parseInt(document.getElementById('number').value, 99);
                        value = isNaN(value) ? 0 : value;
                        if(value<99){
                            value++;
                                document.getElementById('number').value = value;
                        }
                    }


                    function decrementValue()
                    {

                        var value = parseInt(document.getElementById('number').value, 99);
                        value = isNaN(value) ? 0 : value;
                        if(value>1){
                            value--;
                                document.getElementById('number').value = value;
                        }

                    }


						function updateCartTotal() {

						var cartItemContainer = document.getElementsByClassName('cmp-cart-items')[0];
                        var cartrows = cartItemContainer.getElementsByClassName('cmp-cart-row');

                        for(var i=0;i<cartrows.length;i++) {
							var cartInvididualRow = cartrows[i];
                            var priceElement = cartInvididualRow.getElementsByClassName('cmp-cart-price')[0];

                            var qtyElement   = cartInvididualRow.getElementsByClassName('cmp-cart-qty-input')[0];
                            console.log(priceElement,qtyElement);


                        }
      				}







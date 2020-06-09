$(document).ready(function () {

    const getPriceLabel = $('.product-price').text();
let target = document.querySelector(".product-detail");

    /*const getPorductDetails = {cartdetails : [{cartItem:{image: "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png",
														price: "54", qty: "3",title: "Expedition Tech Long-Sleeved Shirt"
                                                        }}, {cartItem:{image: "https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png",
														price: "54", qty: "3",title: "Expedition Tech Long-Sleeved Shirt"
                                                        }}], coupondiscount: "$0", delivercharges: "16",fprice: "192.00"}*/
    if(getPorductDetails){
   		$('.total-price').text(getPorductDetails['fprice']);
        $('.delivery-charges').text(getPorductDetails['delivercharges']) 
		 $('.discount-price').text(getPorductDetails['coupondiscount'])
		let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                                              			<div>
                                                             			<img class ='check-out-total-product-img' src= ${item['cartItem'].image}/>
                                                                         </div>
                                                             <div>
                                                                         <div>
                                                          					<span>${getPriceLabel}</span>
                                                                             <span>${item['cartItem'].title}</span>
                                                                         </div>
                                                                          <div>
                                                                             <span>${item['cartItem'].qty} * <span>$ ${item['cartItem'].price}</span></span>
                                                                         </div>

                                                             			<div><span>Price</span>
                                                                             <span>${item['cartItem'].qty *  item['cartItem'].price}</span>
                                                                         </div>
																		<hr>

                                                                </div>
                                                             </div>`
            );

 

        }).join('');


   if (prodcutDeatilsEle && target) {
            target.innerHTML = prodcutDeatilsEle;

        }
}

});
$(document).ready(function () {

    const getPriceLabel = $('.product-price').text();
let target = document.querySelector(".product-detail");
const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));

let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                                              			<div>
                                                             			<img src= ${item['cartItem'].image}/>
                                                                         </div>
                                                             <div>
                                                                         <div>
                                                          					<span>${getPriceLabel}</span>
                                                                             <span>${item['cartItem'].title}</span>
                                                                         </div>
                                                                          <div>
                                                                             <span>${item['cartItem'].qty}</span>
                                                                         </div>
                                                                        <div>
                                                                             <span>${item['cartItem'].price}</span>
                                                                         </div>

 

                                                                </div>
                                                             </div>`
            );

 

        }).join('');


   if (prodcutDeatilsEle && target) {
            target.innerHTML = prodcutDeatilsEle;

        }


});
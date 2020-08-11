$(document).ready(function() {

    const getPriceLabel = $('.product-price').text();
    let target = document.querySelector(".product-detail");
    const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));

    if (getPorductDetails) {
        $('.total-price-checkout').text(getPorductDetails['fprice']);
        $('.delivery-charges-total').text(getPorductDetails['delivercharges'])
        $('.discount-price').text(getPorductDetails['coupondiscount'])
        let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                                              			<div>
                                                             			<img class ='check-out-total-product-img' src= ${item['cartItem'].image}></img>
                                                                         </div>
                                                             <div>
                                                                         <div>
                                                          					<span>${getPriceLabel}</span>
                                                                             <span>${item['cartItem'].title}</span>
                                                                         </div>
                                                                          <div>
                                                                             <span>${item['cartItem'].qty} * <span>$${Number(item['cartItem'].price).toFixed(2)}</span></span>
                                                                         </div>
                                                             			<div><span>Price: $</span>
                                                                              <span>${(item['cartItem'].qty *  item['cartItem'].price).toFixed(2)}</span>
                                                                         </div>

                                                                </div>
                                                             </div>`
            );



        }).join('');


        if (prodcutDeatilsEle && target) {
            target.innerHTML = prodcutDeatilsEle;

        }
    }

});
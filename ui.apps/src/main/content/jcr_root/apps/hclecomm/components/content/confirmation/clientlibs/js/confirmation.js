
$(document).ready(function () {

    const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));
	const checkOutDeatils = JSON.parse(localStorage.getItem('checkOutDetails'));

    if(checkOutDeatils && checkOutDeatils['orderId'])
    {
		$(".confirmation-failure").addClass("toggle-order");

    let target = document.querySelector(".cmp-confirmation-productInfo");
    let subTotal = document.querySelector(".cmp-confirmation-total");
    let orderNumber = document.querySelector(".cmp-confirmation-orderNo");
    let customerName = document.querySelector(".cmp-confirmation-section-customer-name");
    let customerADD = document.querySelector(".cmp-confirmation-section-customer-address");
    let customerCradDeatils = document.querySelector(".cmp-confirmation-subhead-card-details");
    if (getPorductDetails && checkOutDeatils) {
        let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                                              			<div>
                                                             			<img class ='check-out-total-product-img' src= ${item['cartItem'].image}/>
                                                                         </div>
                                                             <div>
                                                                         <div>
                                                          					<span>Product:}</span>
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

 

        const fiNalAmount = `<div class="product-item-container">
                                                                         <div>
                                                                           Coupon Discount
                                                                         </div>
                                                                          <div>
                                                                             <span>${getPorductDetails['coupondiscount']}</span>
                                                                         </div>

 


                                                                </div>`



        const getOrderId = `<span>We have received your order</span> <span>Order No : ${checkOutDeatils['orderId']}</span>`
        const getCustomerName = `<div class ='cmp-confirmation-subHead'>Delivery For </div> <div class ='cmp-confirmation-text'> ${checkOutDeatils['firstname']}  ${checkOutDeatils['lastname']}</div>`
        const getCustomerAdd = `<div class ='cmp-confirmation-subHead'>Address </div> <div class ='cmp-confirmation-text'> ${checkOutDeatils['street']}  ${checkOutDeatils['city']} ${checkOutDeatils['postcode']}</div>`
        const getCradDeatils = `
                        <div>Card Number: ${checkOutDeatils['cardNumber']}</div>
                            <div>Expiry Date: ${checkOutDeatils['cardExpDate']}</div>
                            <div>Amount: ${getPorductDetails['fprice']}</div>`
        if (prodcutDeatilsEle && fiNalAmount && getOrderId && target) {
            target.innerHTML = prodcutDeatilsEle;
            subTotal.innerHTML = fiNalAmount;
            orderNumber.innerHTML = getOrderId;
            customerName.innerHTML = getCustomerName;
            customerADD.innerHTML = getCustomerAdd;
            customerCradDeatils.innerHTML = getCradDeatils;
        }

 

    }
}
 else
 {
	$(".cmp-confirmation").addClass("toggle-order");
	$(".confirmation-failure").removeClass("toggle-order");

 }
});
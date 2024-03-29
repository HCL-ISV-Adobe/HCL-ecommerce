$(document).ready(function () {



    const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));
    const checkOutDeatils = JSON.parse(localStorage.getItem('checkOutDetails')); 
    const storeAddressDeatils = JSON.parse(localStorage.getItem('storeAddress'));

	if(storeAddressDeatils){
        $('#cmp-delivery-section').addClass('hide-delivery-section');
		const pickupAddressContainer = $('.cmp-storepickup-section');
		 const getCradDeatils = `
                        <div>Contact Person Name: <span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['contact_name']}</span></div>
                            <div>Store Name: <span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['name']}<span></div>
                            <div>Phone No:<span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['phone']}</span></div>
                                <div>Address:<span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['street']},${storeAddressDeatils['city']}</span></div>
                                    <div>Region:<span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['region']}</span></div>
                                        <div>Postcode:<span class ='cmp-confirmation-cardInfo--mrgn-left'>${storeAddressDeatils['postcode']}</span></div>`
                                        if(pickupAddressContainer[0]){
                                         pickupAddressContainer[0].innerHTML = getCradDeatils
                                        }

    	}
    else{
		 $('#store-pickup-point').addClass('hide-delivery-section');
    }

    if(checkOutDeatils && checkOutDeatils['orderId'])
    {
        document.cookie = "cartId" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        $(".confirmation-failure").addClass("toggle-order");

    let target = document.querySelector(".cmp-confirmation-productInfo");
    let subTotal = document.querySelector(".cmp-confirmation-total");
    let orderNumber = document.querySelector(".order-id");
    let customerName = document.querySelector(".cmp-confirmation-section-customer-name");
    let customerADD = document.querySelector(".cmp-confirmation-section-customer-address");
    let customerCradDeatils = document.querySelector(".cmp-confirmation-subhead-card-details");
    let customerCodDetails = document.querySelector(".cmp-confirmation-subhead-cod-details");
    if (getPorductDetails && checkOutDeatils) {
        let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                <div>
                                    <img class ='check-out-total-product-img' src= ${item['cartItem'].image}></img>
                                </div>
                                <div>
                                    <div>
                                        <span>Product:</span>
                                            <span>${item['cartItem'].title}</span>
                                    </div>
                                    <div>
                                        <span>Quantity:</span>
                                        <span>${item['cartItem'].qty}</span></span>
                                    </div>
                                    <div><span>Price</span>
                                        <span>$${(item['cartItem'].qty *  item['cartItem'].price).toFixed(2)}</span>
                                    </div>
                                </div>
                   </div>`
            );

        }).join('');

        const fiNalAmount = `<div class="product-item-container product-item-price-details">

                                    <div>
                                        Coupon Discount
                                            <span>-$${getPorductDetails['coupondiscount']}</span>
                                    </div>
                                    <div>
                                        Delivery Charges
                                            <span>$${getPorductDetails['delivercharges']}</span>
                                    </div>
                                    <div>
                                        Total Price
                                        <span>$${getPorductDetails['fprice']}</span>
                                    </div>
                            </div>`

        const getOrderId = `<span> </span> <span>Order No : ${checkOutDeatils['orderId']}</span>`
        const getCustomerName = `<div class ='cmp-confirmation-subHead'>Delivery For </div> <div class ='cmp-confirmation-text'> ${checkOutDeatils['firstname']}  ${checkOutDeatils['lastname']}</div>`
        const getCustomerAdd = `<div class ='cmp-confirmation-subHead'>Address </div> <div class ='cmp-confirmation-text'> ${checkOutDeatils['street']}  ${checkOutDeatils['city']} ${checkOutDeatils['postcode']}</div><div> ${checkOutDeatils['selectedState']} ${checkOutDeatils['selectedCountry']}</div></div>`
        const cartNumber = checkOutDeatils['cardNumber'];
            let hiddenCardNumber = null;

            {checkOutDeatils['cardNumber'] ?  hiddenCardNumber = cartNumber.slice((cartNumber.length)-5) : null;} 
        const paymentMode = checkOutDeatils['PaymentMode'];
        if(paymentMode == 'Card') {
            document.getElementById('COD').style.display = "none";
            } else if(paymentMode == 'COD') {
            document.getElementById('Card').style.display = "none";
        }
        const getCodDetails = `<div>Amount:<span class ='cmp-confirmation-codInfo--mrgn-left'>$${getPorductDetails['fprice']}</span></div>`
        const getCradDeatils = `
                        <div>Card Number: <span class ='cmp-confirmation-cardInfo--mrgn-left'>***********${hiddenCardNumber}</span></div>
                            <div>Expiry Date: <span class ='cmp-confirmation-cardInfo--mrgn-left'>${checkOutDeatils['cardExpDate']}<span></div>
                            <div>Amount:<span class ='cmp-confirmation-cardInfo--mrgn-left'>$${getPorductDetails['fprice']}</span></div>`;
        if (prodcutDeatilsEle && fiNalAmount && getOrderId && target) {
            target.innerHTML = prodcutDeatilsEle;
            subTotal.innerHTML = fiNalAmount;
            orderNumber.innerHTML = getOrderId;
            if(checkOutDeatils['firstname']!=='NA' && checkOutDeatils['lastname']!=='NA' && checkOutDeatils['street']!=='NA'  && checkOutDeatils['city']!=='NA' && checkOutDeatils['postcode']!=='NA'){
 				customerName.innerHTML = getCustomerName;
           		customerADD.innerHTML = getCustomerAdd;
            }
            customerCradDeatils.innerHTML = getCradDeatils;
            customerCodDetails.innerHTML = getCodDetails;
        }
    }
}
 else
 {
    $(".cmp-confirmation").addClass("toggle-order");
    $(".confirmation-failure").removeClass("toggle-order");

 }
});
$(document).ready(function () {



    const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));
    const checkOutDeatils = JSON.parse(localStorage.getItem('checkOutDetails')); 




    // const getPorductDetails = {"cartdetails":[{"cartItem":{"image":"https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png","title":"Joust Duffle Bag","qty":"3","price":"34"}},{"cartItem":{"image":"https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png","title":"Strive Shoulder Pack","qty":"5","price":"32"}}],"fprice":"359.60","coupondiscount":"52.40","delivercharges":"150"};
    //const checkOutDeatils = {"email":"deepali@hcl.com","firstname":"Deepali","lastname":"Agarwal","postcode":"12450","city":"Noida","street":["Mall Road, Shimla"],"telephone":"8376852110","cartId":"wqDaUgxbQwgrDyi4E2qOefedf1BX9Cj9","region":"MH","region_id":0,"country_id":"IN","region_code":"MH","shipping_method_code":"flatrate","shipping_carrier_code":"flatrate","orderId":"\"9\"","cardNumber":"1234567890123456","cardExpDate":"2020-05-11"}; 

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
    if (getPorductDetails && checkOutDeatils) {
        let prodcutDeatilsEle = getPorductDetails['cartdetails'].map((item) => {
            return (
                `<div class="product-item-container">
                                <div>
                                    <img class ='check-out-total-product-img' src= ${item['cartItem'].image}/>
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
                                        <span>$${item['cartItem'].qty *  item['cartItem'].price}</span>
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
        const getCustomerAdd = `<div class ='cmp-confirmation-subHead'>Address </div> <div class ='cmp-confirmation-text'> ${checkOutDeatils['street']}  ${checkOutDeatils['city']} ${checkOutDeatils['postcode']}</div>`
        const cartNumber = checkOutDeatils['cardNumber'];
        let hiddenCardNumber = null;

        {checkOutDeatils['cardNumber'] ?  hiddenCardNumber = cartNumber.slice((cartNumber.length)-5) : null;} 
        const getCradDeatils = `
                        <div>Card Number: <span class ='cmp-confirmation-cardInfo--mrgn-left'>***********${hiddenCardNumber}</span></div>
                            <div>Expiry Date: <span class ='cmp-confirmation-cardInfo--mrgn-left'>${checkOutDeatils['cardExpDate']}<span></div>
                            <div>Amount:<span class ='cmp-confirmation-cardInfo--mrgn-left'>$${getPorductDetails['fprice']}</span></div>`
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
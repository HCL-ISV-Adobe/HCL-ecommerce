$(document).ready(function () {
    let cartId = '';
    const getCountEle = document.getElementById("total-item-count");
    getCountEle.style.display = 'none';
    const getCookies = document.cookie;
    if (getCookies.indexOf('cartId') > -1) {
        const cookiesCartID = getCookies.split(';');
        const currentCookiesIndex = getCookies.indexOf('cartId');
        if (cookiesCartID && cookiesCartID.length > 0) {
            const cartIdArr = cookiesCartID[currentCookiesIndex].split('=')
            cartId = cartIdArr[1];
        }

    } else {
        /// standard  cart id if cookies are not avliable
        document.cookie = "cartId = RntVBlSqoTTYDj3BepTMQybULNvY6Wbl";
        cartId = 'RntVBlSqoTTYDj3BepTMQybULNvY6Wbl';
    }
    if (cartId) {
        const xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                if (this.responseText > 0) {
                    getCountEle.style.display = 'inline-flex';
                    getCountEle.innerHTML =
                        this.responseText;
                } else {
                    $("#cart-item-count").removeAttr("href");;
                }
            }
        };
        xhttp.open("GET", "/bin/hclecomm/cartItemsCount?cartId=" + cartId, true);
        xhttp.send();
    }


});
// method to show notification on empty cart 
const showEmptyCartNotification = () => {
    const getItemCount = $('#total-item-count').text();
    if (getItemCount === '0') {
        $("#notification").empty();
        $("#notification").css({
            display: 'inline-block'
        }).append('Cart is empty');
        $("#notification").delay(5000).fadeOut("slow");
    }


}
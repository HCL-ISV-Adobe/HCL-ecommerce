let allowToShowNotification = false;

$(document).ready(function () {
    let userData = getUserCookie("hcluser"); 
    if(userData != "") {
         custToken = JSON.parse(userData).customerToken;
    }
    let cartId = '';
    const getCountEle = document.getElementById("total-item-count");
    const href = $('#cart-item-count').attr('href');
    $("#cart-item-count").removeAttr("href");

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
    if (cartId) {
        const xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                allowToShowNotification = true;
                if (this.responseText > 0) {
                    //$("#cart-item-count").addAttr("href");
                    $("#cart-item-count").attr('href', href);
                    if(getCountEle)
                    {
                    	getCountEle.style.display = 'inline-flex';
                    	getCountEle.innerHTML = this.responseText;
                    }

                }
            }
        };
        xhttp.open("GET", "/bin/hclecomm/cartItemsCount?cartId=" + cartId, true);
        xhttp.setRequestHeader("CustomerToken", custToken);
        xhttp.send();
    }
    else
    {
        allowToShowNotification=true;
    }


});
// method to show notification on empty cart
const showEmptyCartNotification = () => {
    const getItemCount = $('#total-item-count').text();
    if ((getItemCount === '0' || getItemCount==="") && allowToShowNotification) {
        $("#notification").empty();
        $("#notification").css({
            display: 'inline-block' ,fontFamily: 'sans-serif'
        }).append('Cart is empty');
        $("#notification").delay(5000).fadeOut("slow");
    }


}

$(document).ready(function() {
    let cartId = '';
    const getCountEle = document.getElementById("total-item-count");
    getCountEle.style.display = 'none';
    document.cookie = "cartId = RntVBlSqoTTYDj3BepTMQybULNvY6Wbl";
    const cookiesCartID = ['cartId=RntVBlSqoTTYDj3BepTMQybULNvY6Wbl']
    if (cookiesCartID && cookiesCartID.length > 0) {

        cookiesCartID.forEach(function(cookiesCartIDItem) {
            if ((cookiesCartIDItem).indexOf(cartId) > -1) {
                const cartIdArr = cookiesCartIDItem.split('=')
                cartId = cartIdArr[1];
            }

        })

        if (cartId) {
            const xhttp = new XMLHttpRequest();
            xhttp.onreadystatechange = function() {
                if (this.readyState == 4 && this.status == 200) {
                    if (this.responseText > 0) {
                        getCountEle.style.display = 'inline-block';
                        getCountEle.innerHTML =
                            this.responseText;
                    }
                    else {
            $("#cart-item-count").removeAttr("href");;
                    }
                }
            };
            xhttp.open("GET", "/bin/hclecomm/cartsItems?cartId=" + cartId, true);
            xhttp.send();
        }

    }
});


// method to show notification on empty cart 

const redirectToCart = () => {
    const getItemCount = $('.min-cart-text-count').text();
    if (getItemCount === '0') {
    $("#notification").empty();
        $("#notification").css({display: 'inline-block'}).append('Cart is empty');
        $("#notification").delay(5000).fadeOut("slow");
    } 

}
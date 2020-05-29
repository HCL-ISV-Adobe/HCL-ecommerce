//var sku = "24-MB01";

var getUrl=window.location.href;
var url = new URL(getUrl);
var prodSku = url.searchParams.get("prodSku");
console.log(prodSku);

$(document).ready(function () {
	$('.quantity').val("1");
	$('.product-details-cmp__prduct-price-sign').css("display", "none");
	$('.product-details-cmp--no-product').css("display", "none");
	$('.btn-product-card').addClass("btn-product-card-disabled");
	$(".btn-product-card").attr('disabled', 'disabled');
	const firstSizeSelected = $(".product-details-cmp__product-size--item:first-child").text();
	$('.select-size').text(firstSizeSelected);
	$(".product-details-cmp__product-size--item:first-child").addClass("selected-size");
	const xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function () {
		if (this.readyState == 4 && this.status == 200) {
            let productDetail;

			productDetail = JSON.parse(this.responseText)

			$('.product-details-cmp__prduct-price-sign').css("display", "inline-block");
			$(".product-details-cmp__prduct-price").text(productDetail[0].price);
			const isProductAvilable = (productDetail[0].stock === 'true');
			if (!isProductAvilable) {
				$('.btn-product-card').children().children().addClass("btn-product-card-disabled");
				$(".btn-product-card").attr('disabled', 'disabled');
				$('.product-details-cmp--no-product').css("display", "block");
			} else {
				$('.btn-product-card').removeClass("btn-product-card-disabled");
				$(".btn-product-card").removeAttr('disabled');
				$('.product-details-cmp--no-product').css("display", "none");

			}


		}
	};
	xhttp.open("GET", "/bin/hclecomm/productDetails?sku=" + prodSku, true);
	xhttp.send();

});

function onSizeSelection() {
	$(".selected-size").removeClass("selected-size");
	//$(this).addClass("selected-size");


	$target = $(event.target);
	$target.addClass('selected-size');
	$(".select-size").text(event.target.textContent);
}

function onQuantityChnage(para) {
	let getQuant = Number($('.quantity').val());
	if (para === '-') {
		if (getQuant === 1) {
			return
		} else {
			getQuant = getQuant - 1;
		}
		$('.quantity').val(getQuant);
		return
	}


	if (getQuant === 999) {
		return
	} else {
		getQuant = getQuant + 1;
	}
	$('.quantity').val(getQuant);
}


function onQuantityKeyUp(event) {
	const getCount = event.target.value;
	getCount < 1 ? $('.quantity').val(1) : null;
	getCount > 999 ? $('.quantity').val(999) : null;
}


function getCookie(name, callback) {

        var cookieArr = document.cookie.split(";");
    	let cartId = "";
        for(var i = 0; i < cookieArr.length; i++) {
            var cookiePair = cookieArr[i].split("=");
            if(name == cookiePair[0].trim()) {
                cartId = decodeURIComponent(cookiePair[1]);;
                //return cartId;
				callback(cartId);
            }
        }

    	if(!cartId)
        {
			 /// standard  cart id if cookies are not avliable
        	//document.cookie = "cartId = j7KaMe1zWFfopDFOVTdFZV0rokpjwzam";
        	//cartId = 'j7KaMe1zWFfopDFOVTdFZV0rokpjwzam';
            const xhttp = new XMLHttpRequest();
       		 xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
				if(this.responseText)
                {
                    const {message:{cartid}} = JSON.parse(this.responseText);
                           console.log(cartid);
                    document.cookie = "cartId = " + cartid;
                    callback(cartid);

                }
            }
        };
        xhttp.open("GET", "/bin/hclecomm/createCart", true);
        xhttp.send();
        }

       // return cartId;
 }
function callback(cartId) {
    var inpValue=parseInt(document.querySelector(".productdetails .qty input[type='text']").value);
	var data={
  "sku": prodSku,
  "cartid": cartId,
  "qty": inpValue
	}
	console.log("data ",data);

//sending data to server
	var xhr = new XMLHttpRequest();
	var url = "http://localhost:4502/bin/hclecomm/addToCart";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
        console.log("response ",xhr.responseText);
	}
	};
	xhr.send(JSON.stringify(data));
}

function addtoCart(){
	cartID=getCookie('cartId',callback);
}

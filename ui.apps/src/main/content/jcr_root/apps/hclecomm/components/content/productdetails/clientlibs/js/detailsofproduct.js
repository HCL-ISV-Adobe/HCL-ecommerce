let prodSku = "";
let AddtocartRedirection = "";
let custToken = "";

$(document).ready(function() {
 AddtocartRedirection = $('.btn-product-card').children().children().attr('href');
 $('.btn-product-card').children().children().removeAttr("href");
 $('.quantity').val("1");
 $('.product-details-cmp__prduct-price-sign').css("display", "none");
 $('.product-details-cmp--no-product,.cmp--wishlist--product,.cmp--wishlist--outofstock,.cmp--wishlist--product-add').css("display", "none");
 $('.btn-product-card').addClass("btn-product-card-disabled");
 $(".btn-product-card").attr('disabled', 'disabled');
 prodSku = $(".product-sku").text();
 const firstSizeSelected = $(".product-details-cmp__product-size--item:first-child").text();
 $('.select-size').text(firstSizeSelected);
 if (prodSku) {
  $(".product-details-cmp__product-size--item:first-child").addClass("selected-size");

  let userData = getUserCookie("hcluser");
  if (userData != "") {
   custToken = JSON.parse(userData).customerToken;
  }

  const xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
    let productDetail;
    try {
     productDetail = JSON.parse(this.responseText);
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

     if(checkmode !== 'edit') {
      const domProdList = document.querySelectorAll('#cross-sell-products .product-listing-tile');
      let crosssellSkuList = JSON.parse(productDetail[0].related_products_sku);
         if(!crosssellSkuList || crosssellSkuList.length === 0){
            document.querySelector('.cmp-experiencefragment--product-listing').style.display = 'none';
         } else {
              for(const tile of domProdList) {
                  if(!tile.dataset.productsku || !crosssellSkuList.includes(tile.dataset.productsku)){
                      tile.parentElement.style.display = 'none';
                  }
              }
         }
      }
     }
    } catch (e) {
     console.log(e)
    }
   }
  };
  xhttp.open("GET", "/bin/hclecomm/productDetails?sku=" + prodSku, true);
  xhttp.send();
 }
});

function onSizeSelection() {
 $(".selected-size").removeClass("selected-size");
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
 if (!Number(event.target.value)) {
  $('.quantity').val(1);
  return
 }
 const getCount = event.target.value;
 getCount < 1 ? $('.quantity').val(1) : null;
 getCount > 999 ? $('.quantity').val(999) : null;
}

function getCookie(name, callback) {
 var cookieArr = document.cookie.split(";");
 let cartId = "";
 for (var i = 0; i < cookieArr.length; i++) {
  var cookiePair = cookieArr[i].split("=");
  if (name == cookiePair[0].trim()) {
   cartId = decodeURIComponent(cookiePair[1]);;

   callback(cartId);
  }
 }

 if (!cartId) {

  const xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
    if (this.responseText) {
     const {
      message: {
       cartid
      }
     } = JSON.parse(this.responseText);
     document.cookie = "cartId = " + cartid + "; path=/";
     callback(cartid);

    }
   }
  };
  xhttp.open("GET", "/bin/hclecomm/createCart", true);
  xhttp.setRequestHeader("CustomerToken", custToken);
  xhttp.send();
 }
}

function callback(cartId) {
 var inpValue = parseInt(document.querySelector(".productdetails .qty input[type='text']").value);
 var data = {
  "sku": prodSku,
  "cartid": cartId,
  "qty": inpValue
 }

 //sending data to server
 var xhr = new XMLHttpRequest();
 var url = "/bin/hclecomm/addToCart";
 xhr.open("POST", url, true);
 xhr.setRequestHeader("Content-Type", "application/json");
 xhr.setRequestHeader("CustomerToken", custToken);
 xhr.onreadystatechange = function() {
  if (xhr.readyState === 4 && xhr.status === 200) {
   localStorage.removeItem("checkOutDetails");
   AddtocartRedirection ? window.location.href = AddtocartRedirection : null;
  }
 };
 xhr.send(JSON.stringify(data));
}

function addtoCart() {
 cartID = getCookie('cartId', callback);
}

function addtoWishlist() {
 if (custToken && prodSku) {
  const data = {
   sku: prodSku
  }
  var xhr = new XMLHttpRequest();
  var url = "/bin/hclecomm/addToWishlist";
  xhr.open("POST", url, true);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.setRequestHeader("CustomerToken", custToken);
  xhr.onreadystatechange = function() {
   if (xhr.readyState === 4 && xhr.status === 200) {
     const productDetail = JSON.parse(this.responseText);

     if (!productDetail.status) {
      $('.cmp--wishlist--outofstock').fadeIn('slow', function() {
        $(this).delay(5000).fadeOut('slow');
       });



	 }
       else{
           $('.cmp--wishlist--product-add').fadeIn('slow', function() {
        $(this).delay(5000).fadeOut('slow');
       });

       }
   }

   }
  xhr.send(JSON.stringify(data));
 }
  else {
   $('.cmp--wishlist--product').fadeIn('slow', function() {
    $(this).delay(5000).fadeOut('slow');
   });
  }

 }
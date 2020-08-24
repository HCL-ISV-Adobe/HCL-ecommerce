let wishListArr =null;
let wishlistCustToken = null;
let wishlistProdSku =null;
$( document ).ready(function() {
      $('.cmp-wisthlit-item-product-move-to-shoping-bag').css("display", "none");
const wisthListUrl = window.location.href.includes("my-wishlist-bag.html");
     let userData = getUserCookie("hcluser");
  if (userData != "") {
   wishlistCustToken = JSON.parse(userData).customerToken;
  }
     const xhttp = new XMLHttpRequest();
    if( wishlistCustToken && wisthListUrl){
  xhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
    try {
     wishListArr = JSON.parse(this.responseText);
      const wishListContainer = $('.cmp-wish-list-container');
    if( wishListArr && wishListArr.length > 0){
         $('.wishlist-item-no-item-list').text('');
        $('.wishlist-item-no-item-list').addClass('toggle-wishlist-display');
        $('.wishlist-item-count').text(wishListArr.length);

        const wisListItems = wishListArr.map((wishListArrItem) =>{

                                             return (
                                                    `<div class ='wish-list-item'>
                                                        <div class ='wish-list-item--left-wist'>
                                                            <img class='wishlist-cmp--product-image'  src= ${wishListArrItem.image_url}/>
                                                            <div>
                                                                <p><b>${wishListArrItem.name}</b></p>
                                                                <p><span>Code :${wishListArrItem.sku}</span></p>
                                                            </div>

                                                        </div>
                                                        <div class ='wish-list-item--right-container'>
                                                        <button class='wishlis-cmp--move-to-bag' onclick = "onMoveToBag('${wishListArrItem.sku}')"> Add to Bag </button>
                                                            <p><span>$ ${wishListArrItem.price}</span></p>
                                                            <i class=" wishlist-cm-delete-icon fa fa-trash " onclick = "onDeleteItem('${wishListArrItem.item_id}')" aria-hidden="true"></i>
                                                        </div>
                                                    </div>`
                                                )
                                             })

          if( wishListContainer){
            wishListContainer[0].innerHTML = wisListItems;

          }

 

        }
       else{
           $(".cmp-wishlist-item-count").css("display",'none');
       }

     } 
     catch (e) {
     console.log(e)
    }
   }

  };

                     xhttp.open("GET","/bin/hclecomm/getWishList", true);
xhttp.setRequestHeader("CustomerToken",wishlistCustToken);
 xhttp.send();
}
});


function getCookieWishlist(name, callback) {
 var cookieArr = document.cookie.split(";");
 let cartId = "";
 for (var i = 0; i < cookieArr.length; i++) {
  var cookiePair = cookieArr[i].split("=");
  if (name == cookiePair[0].trim()) {
   cartId = decodeURIComponent(cookiePair[1]);;

   callbackwishlist(cartId);
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

function callbackwishlist(cartId) {
 var inpValue =1;
 var data = {
  "sku": wishlistProdSku,
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
       $('.cmp-wisthlit-item-product-move-to-shoping-bag').fadeIn('slow', function() {
           $(this).delay(5000).fadeOut('slow',function(){
             location.reload();
           });


   });

  }
 };
 xhr.send(JSON.stringify(data));
}

function onMoveToBag(prodsku) {
    wishlistProdSku =prodsku;
 cartID = getCookieWishlist('cartId', callbackwishlist);
}
function onDeleteItem(itemId) {
 var data = {
  "itemId":itemId
 }
 
 //sending data to server
 var xhr = new XMLHttpRequest();
 var url = "/bin/hclecomm/deleteWishListItem";
 xhr.open("DELETE", url, true);
 xhr.setRequestHeader("Content-Type", "application/json");
 xhr.setRequestHeader("CustomerToken", custToken);
 xhr.onreadystatechange = function() {
  if (xhr.readyState === 4 && xhr.status === 200) {
       console.log('item deleted');
      const response = JSON.parse(this.responseText);
      if(response['status']){
      location.reload();
 }
   }

  }

  xhr.send(JSON.stringify(data));
 };


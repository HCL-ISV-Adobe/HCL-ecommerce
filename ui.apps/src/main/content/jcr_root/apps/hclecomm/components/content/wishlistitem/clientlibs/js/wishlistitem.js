let wishListArr =null;
let wishlistCustToken = null;
let wishlistProdSku =null;
$( document ).ready(function() {
     let userData = getUserCookie("hcluser");
  if (userData != "") {
   wishlistCustToken = JSON.parse(userData).customerToken;
  }
     const xhttp = new XMLHttpRequest();

  xhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
    try {
     wishListArr = JSON.parse(this.responseText);
      const wishListContainer = $('.cmp-wish-list-container');
    if( wishListArr && wishListArr.length > 0){
        $('.wishlist-item-count').text(wishListArr.length);
        const wisListItems = wishListArr.map((wishListArrItem) =>{

                                             return (
                                                    `<div class ='wish-list-item' id=PID${wishListArrItem.item_id}>
                                                        <div class ='wish-list-item--left-wist'>
                                                            <img class='wishlist-cmp--product-image'  src= ${wishListArrItem.image_url}/>
                                                            <div>
                                                                <p><b>${wishListArrItem.name}</b></p>
                                                                <p><span>Code :${wishListArrItem.sku}</span></p>
                                                            </div>

                                                        </div>
                                                        <div class ='wish-list-item--right-container'>
                                                        <button class='wishlis-cmp--move-to-bag' onclick = "onMoveToBag('${wishListArrItem.sku}')"> Move to Bag </button>
                                                            <p><span>$ ${wishListArrItem.price}</span></p>
                                                            <i class=" wishlist-cm-delete-icon fa fa-trash " aria-hidden="true"></i>
                                                        </div>
                                                    </div>`
                                                )
                                             })

          if( wishListContainer){
            wishListContainer[0].innerHTML = wisListItems;

          }

 

        }
       else{

           const emptyWishlist ='<div class ="wishlist-item-no-item-list">No Product in Wishlist</div>';
            wishListContainer[0].innerHTML = emptyWishlist;
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
     console.log(cartid);
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
 console.log("data ", data);



 //sending data to server
 var xhr = new XMLHttpRequest();
 var url = "/bin/hclecomm/addToCart";
 xhr.open("POST", url, true);
 xhr.setRequestHeader("Content-Type", "application/json");
 xhr.setRequestHeader("CustomerToken", custToken);
 xhr.onreadystatechange = function() {
  if (xhr.readyState === 4 && xhr.status === 200) {
   localStorage.removeItem("checkOutDetails");

  }
 };
 xhr.send(JSON.stringify(data));
}

function onMoveToBag(prodsku) {
    wishlistProdSku =prodsku;
 cartID = getCookieWishlist('cartId', callbackwishlist);
}
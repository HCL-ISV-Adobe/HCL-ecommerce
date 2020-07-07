let wishListArr =null;
$( document ).ready(function() {
     const xhttp = new XMLHttpRequest();

  xhttp.onreadystatechange = function() {
   if (this.readyState == 4 && this.status == 200) {
    try {
     wishListArr = JSON.parse(this.responseText);
      const wishListContainer = $('.cmp-wish-list-container');
    if( wishListArr && wishListArr.length > 0){
        const wisListItems = wishListArr.map((wishListArrItem) =>{
                                             return (
                                                    `<div class ='wish-list-item'>
                                                        <div class ='wish-list-item--left-wist'>
                                                            <img class=''  src= ${wishListArrItem.image_url}/>
                                                            <div>
                                                                <p><b>${wishListArrItem.name}</b></p>
                                                                <p><span>${wishListArrItem.sku}</span></p>
                                                            </div>

                                                        </div>
                                                        <div class ='wish-list-item--right-container'>
                                                            <button> move to bag</button>
                                                            <p><span>${wishListArrItem.price}</span></p>
                                                            <button> move to bag</button>
                                                        </div>
                                                    </div>`
                                                )
                                             })

          if( wishListContainer){
            wishListContainer[0].innerHTML = wisListItems;

          }

 

        }

     } 
     catch (e) {
     console.log(e)
    }
   }
  };
  xhttp.open("GET","/bin/hclecomm/getWishList", true);
xhttp.setRequestHeader("CustomerToken", "3xbz14to2jpi4kqa6h2i7g83x8ih3ci2");
  xhttp.send();


});


$(document).ready(function(){
    let cartId = '';
    document.cookie = "cartId = RntVBlSqoTTYDj3BepTMQybULNvY6Wbl";
   // const cookiesCartID = document.cookie.split(';');
   // console.log(parts);
    const cookiesCartID = ['cartId=RntVBlSqoTTYDj3BepTMQybULNvY6Wbl']
    if(cookiesCartID && cookiesCartID.length > 0){
   
    cookiesCartID.forEach(function(cookiesCartIDItem){
    if((cookiesCartIDItem).indexOf(cartId) > -1){
    const cartIdArr = cookiesCartIDItem.split('=')
    cartId  = cartIdArr[1];
    }
        console.log(cartId);
    })

    if(cartId)
    {
        const xhttp = new XMLHttpRequest();
  		xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
              document.getElementById("total-item-count").innerHTML =
              this.responseText;
            }
  		};
           xhttp.open("GET", "/bin/hclecomm/cartsItems?cartId="+cartId, true);
          xhttp.send();
    }

    }
});
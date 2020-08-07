let avgRating = 0;


const xhttpReq = new XMLHttpRequest();
xhttpReq.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        let ratingResponse;
        try {
            ratingResponse = JSON.parse(this.responseText);

            avgRating = Math.round(ratingResponse[0].rating * 2) / 2;
            document.getElementById("rating" + avgRating * 2).checked = true;
        } catch (e) {
            console.log(e)
        }
    }
};
prodSku = $(".product-sku").text();
xhttpReq.open("GET", "/bin/hclecomm/ratinglist?sku=" + prodSku, true);
xhttpReq.send();


function submitRating(rating) {
    let data = {
        "sku": prodSku,
        "name": prodName,
        "rating": rating
    }
    //sending data to server
    let xhttpPostReq = new XMLHttpRequest();
    let url = "/bin/hclecomm/ratinglist";
    xhttpPostReq.open("POST", url, true);
    xhttpPostReq.setRequestHeader("Content-Type", "application/json");
    xhttpPostReq.setRequestHeader("CustomerToken", custToken);
    xhttpPostReq.send(JSON.stringify(data));
}
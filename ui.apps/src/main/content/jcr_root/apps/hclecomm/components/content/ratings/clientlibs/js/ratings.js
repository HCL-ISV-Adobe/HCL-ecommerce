let avgRating = 0;
$(document).ready(function() {
 $('.cmp-ratingCount').on("click", function() {
        $('#customReviewWrapper').toggle();
    });

const xhttpReq = new XMLHttpRequest();
xhttpReq.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
        let ratingResponse;
        try {
            ratingResponse = JSON.parse(this.responseText);
            Object.values(ratingResponse[0]);
            if ($(".cmp-ratingCount").length > 0) {
            $('.cmp-ratingCount .cmp-text a').text(ratingResponse[1].length +' ratings');
            }
            avgRating = Math.round(Object.values(ratingResponse[0]) * 2) / 2;
            document.getElementById("rating" + avgRating * 2).checked = true;
        } catch (e) {
            console.log(e)
        }
    }
};
prodSku = $(".product-sku").text();
xhttpReq.open("GET", "/bin/hclecomm/ratinglist?sku=" + prodSku, true);
xhttpReq.send();

$('.rate input').on('click',function(){
submitRating(this.value);
});

function submitRating(rating) {
ratingReview=rating;
let userData = getUserCookie("hcluser");
 if (userData == "" || typeof userData == 'undefined') {
		customerEmail ='N/A';
        customerName = 'guest';


    let data = {
        "sku": prodSku,
        "name": prodName,
        "rating": ratingReview,
        "customer": customerName,
        "email": customerEmail,
        "title":'N/A',
        "description": 'N/A'
    }
    //sending data to server
    let xhttpPostReq = new XMLHttpRequest();
    let url = "/bin/hclecomm/ratinglist";
    xhttpPostReq.open("POST", url, true);
    xhttpPostReq.setRequestHeader("Content-Type", "application/json");
	xhttpPostReq.send(JSON.stringify(data));

 }

}
});

$(document).ready(function() {

    let reviewFormLink = $('.review-form-link').attr('href');
    signinLink = $('.review-form-link').attr('loginUrl');
    //alert("reviewFormLink" +reviewFormLink);
    $('#customerReview').hide();
    $('.review-button-link').on("click", function() {
        $('#customerReview').toggle();
    });




    $(".review-form-link").removeAttr("href");
    $('.review-form-link').on("click", function() {

        let userData = getUserCookie("hcluser");
        if (userData != "") {
            window.location.href = reviewFormLink;
        } else {
            window.location.href = `${signinLink}?referer=${reviewFormLink}`;
        }
    });


    function onNavigateToReviewForm() {
        let userData = getUserCookie("hcluser");
        if (userData != "") {
            window.location.href = reviewFormLink;
        } else {
            // window.location.href = `${signinLink}?referer=${reviewFormLink}`;
        }
    }


    var ratingstar0 = `<span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstarhalf = `<span class="fa fa-star-half-full checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstarone = `<span class="fa fa-star checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstaronehalf = ` <span class="fa fa-star checked"></span><span class="fa fa-star-half-full checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstartwo = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstartwohalf = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star-half-full checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstarthree = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star"></span><span class="fa fa-star"></span>`;
    var ratingstarthreehalf = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star-half-full checked"></span><span class="fa fa-star"></span>`;
    var ratingstarfour = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star"></span>`;
    var ratingstarfourhalf = ` <span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star-half-full checked"></span>`;
    var ratingstarfive = `<span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span><span class="fa fa-star checked"></span>`;

    const xhttpcustomerReviewReq = new XMLHttpRequest();
    xhttpcustomerReviewReq.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            let ratingResponseReview;
            try {
                ratingResponseReview = JSON.parse(this.responseText);
                var ratingUser = ratingResponseReview[1];
                var customerReview = "";
                document.getElementById('customerReview').innerHTML = "";
                i = 0;
                for (ratingObj of ratingUser) {

                    switch (ratingObj.rating) {
                        case 0:
                            rateStar = ratingstar0;
                            break;
                        case 0.5:
                            rateStar = ratingstarhalf;
                            break;
                        case 1:
                            rateStar = ratingstarone;
                            break;
                        case 1.5:
                            rateStar = ratingstaronehalf;
                            break;
                        case 2:
                            rateStar = ratingstartwo;
                            break;
                        case 2.5:
                            rateStar = ratingstartwohalf;
                            break;
                        case 3:
                            rateStar = ratingstarthree;
                            break;
                        case 3.5:
                            rateStar = ratingstarthreehalf;
                            break;
                        case 4:
                            rateStar = ratingstarfour;
                            break;
                        case 4.5:
                            rateStar = ratingstarfourhalf;
                            break;
                        case 5:
                            rateStar = ratingstarfive;
                            break;
                    }

                    customerReview += `<div class="review-card">
<div class="review-container">
<div>
${rateStar}

<span class="review-title">
${ratingObj.title}
</span>
</div>
<p>${ratingObj.description}</p>
<div>${ratingObj.customer}</div>
</div>
</div>
`;

                }
                $("#customerReview").append(customerReview);

            } catch (e) {
                console.log(e)
            }
        }
    };
    prodSku = $(".product-sku").text();
    xhttpcustomerReviewReq.open("GET", "/bin/hclecomm/ratinglist?sku=" + prodSku, true);
    xhttpcustomerReviewReq.send();

});
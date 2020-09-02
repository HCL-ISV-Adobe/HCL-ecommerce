$(document).ready(function() {
    ratingReview = '';
    customerEmail = '';
    custToken = '';
    customerName = '';
    let productDescriptionURL = '';
    let userData = getUserCookie("hcluser");
    if (userData != "") {
        custToken = JSON.parse(userData).customerToken;
        customerEmail = JSON.parse(userData).email;
        customerName = JSON.parse(userData).firstname;

    }
	$("#review_name").val(customerName);
	$(".close").on("click", function() {
        if (customerValidateForm()) {

            submitReviewForm();
        }

    });



    function displayerror(msg) {
        $(".error").text(msg);
        $(".error").show();
    }

    function showSucess() {

        $(".review-form-container").show();
        $(".review-form .container").hide();

        setTimeout(function() {
            window.location.href = $(".review-button a").attr("href");
        }, 7000);


    }

    function customerValidateForm() {

        $(".error").hide();
        if (ratingReview == "") {
            displayerror("Please select Rating");
            return false;
        } else if ($("#review_title").val() == "") {
            displayerror("Please Enter the Title.");
            return false;
        } else if ($("#review_name").val() == "") {
            displayerror("Please Enter the Customer Name.");
            return false;
        } else if ($("#review_description").val() == "") {
            displayerror("Please Enter the Customer Description.");
            return false;
        } else {
            return true;
        }
    }




    function submitReviewForm() {

        let data = {
            "sku": localStorage.getItem('prodSku'),
            //"sku":"24-WG085",
            "name": localStorage.getItem('prodName'),
            "rating": ratingReview,
            "customer": customerName,
            "email": customerEmail,
            "title": $("#review_title").val(),
            "description": $("#review_description").val()
        }
        //sending data to server
        let xhttpPostReq = new XMLHttpRequest();
        let url = "/bin/hclecomm/ratinglist";
        xhttpPostReq.open("POST", url, true);
        xhttpPostReq.setRequestHeader("Content-Type", "application/json");
        //xhttpPostReq.setRequestHeader("CustomerToken", custToken);
        xhttpPostReq.send(JSON.stringify(data));

        xhttpPostReq.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                let reviewResponse;
                try {
                    reviewResponse = JSON.parse(this.responseText);
					if (reviewResponse.message == true) {
                        showSucess();

                    } else {
                        displayerror(reviewResponse.message);
                    }
                } catch (e) {
                    console.log(e)
                }
            }
        };

    }



});
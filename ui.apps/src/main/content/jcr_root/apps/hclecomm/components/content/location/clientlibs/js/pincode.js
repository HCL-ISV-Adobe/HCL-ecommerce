let userAddressLoc = null;
let addressCustTokenLoc = null;


const getuserLocation = (userAddress) => {
console.log(userAddress);

let userDataLoc = getUserCookie("hcluser");
    if (userDataLoc != "") {
        addressCustTokenLoc = JSON.parse(userDataLoc).customerToken;
         if (userAddress && userAddress.length > 0) {
                    userAddressLoc = userAddress;
                    const addressPlaceHolder = $('.location-cmp-address-txt');
                    const userAddressDetails = `<span class='location-cmp-address-city'>${userAddress[0].city}</span><span>${userAddress[0].postcode}</span>`;
                    if (addressPlaceHolder && userAddressDetails) {
                        addressPlaceHolder[0].innerHTML = userAddressDetails;

                    }

                }
    }
}

const showUserAddress = () => {
    let userAllSection = null;
    $('.location-cmp-overlay').css('display', 'flex');
	const yourAddressElm = $('#location-cmp-your-location');
    var url = window.location.href;
    if(addressCustTokenLoc){

    if (userAddressLoc && userAddressLoc.length > 0) {
        userAllSection = userAddressLoc.map((userAddressItems, index) => {
            return (
                `<div class ='location-cmp-user-single-address'>
                        	<p>${userAddressItems.firstname} ${userAddressItems.lastname}
                        	${userAddressItems.street.join()}, ${userAddressItems.city}</p>
                        	<p>Post Code-${userAddressItems.postcode}<p>
                        	<p>Mob No - ${userAddressItems.telephone}<p>
						 </div>`)
        })
        const userAddressPlaceHolder = $('.location-cmp-body-upper-container');
        if (userAddressPlaceHolder && userAllSection) {
            userAddressPlaceHolder[0].innerHTML = userAllSection.join(' ');
        }
if (userAddressLoc && userAddressLoc.length > 1){
    $('.location-cmp-user-single-address').css('border-bottom','1px solid gray' )
}
    }
	else{
        const goToAddAddressUrl = (yourAddressElm.attr("data-custom-add-address"));
    url = url.substring(0, url.indexOf('/content/'));
    const goToAddAddressCompleteUrl = url+goToAddAddressUrl+'.html';
        const goAddAddress = `<a class='location-cmp-btn-go-to-sign-in' href = ${goToAddAddressCompleteUrl}> Go to Add Address</a>`;
	const goAddAddressPlaceHolder = $('.location-cmp-body-go-to-sign-in');
        if (goAddAddressPlaceHolder && goAddAddress) {
            goAddAddressPlaceHolder[0].innerHTML = goAddAddress;
        }
	}

}
    if(yourAddressElm) {
    const signInUrl = (yourAddressElm.attr("data-custom-sign-in"));
    url = url.substring(0, url.indexOf('/content/'));
    const signInCompleteUrl = url+signInUrl+'.html';
        $('.location-cmp-btn-sign-in-link')[0] ? $('.location-cmp-btn-sign-in-link')[0].href = signInCompleteUrl : null;
    }
}

const onHideUserAddress = () => {
    $('.location-cmp-overlay').css('display', 'none');
}

const onPincodeApply = () => {
    const enteredPincode = $('.location-cmp-pincode-text').val();
    const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
    if (enteredPincode.match(pinRegexp) && enteredPincode !== '') {


        $.ajax({
            type: 'GET',
            data: {
                Pincode: enteredPincode
            },
            url: '/bin/hclecomm/locationpincode',
            success: function(result) {
                if(result && result.length > 0){
                console.log(result[0].City);
                $('.location-cmp-address-txt').text(result[0].City);
                onHideUserAddress();
                }
                else
                {
					$('.location-cmp-pincode-validation').text("Please Enter Valid Pincode");
                }
            },

            error: function(request, error) {
                alert("error");
            }
        });
    } else {
        if (enteredPincode === '') {

            $('.location-cmp-pincode-validation').text("Please Enter Pincode");
            return;
        }
        $('.location-cmp-pincode-validation').text("Please Enter Valid Pincode");
    }
}

function onPincodeChange() {
    $('.location-cmp-pincode-validation').text(" ");
}
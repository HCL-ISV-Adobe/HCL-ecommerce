const getUserDeatils = {};
//let cartId = 'aUETIXpuqYFyr9EEx1F7XjMXL4RBmBer';
let checkoutcartId = ''
getHrefForCvvButton = '';
getHrefForNewButton = '';
$(document).ready(function () {

    //using cookies

		var checkmode;

       const getCookies = document.cookie;
	   
	   if (getCookies.indexOf('wcmmode') > -1) {
        const cookiesCartID = getCookies.split(';');
        cookiesCartID  && cookiesCartID.length >0 ?
        Object.keys(cookiesCartID).forEach((cookiesCartIDitem) =>{
            const splitCookies  = cookiesCartID[cookiesCartIDitem].split('=')
                if(splitCookies[0] === 'wcmmode' || splitCookies[0] === ' wcmmode')  {

                checkmode= splitCookies[1];
        		console.log("checkmode {}",checkmode);

            }
    }):null
        }
		if(checkmode == 'edit'){

			$(".check-out-section-description").addClass("toggle-checkout-description");

			$(".add-new-address-form").addClass("toggle-checkout-description");
		}
		else{

            $(".check-out-section-description").removeClass("toggle-checkout-description");
            $(".check-out-section-description--first").addClass("toggle-checkout-description");
            $(".add-new-address-form").removeClass("toggle-checkout-description");

		}


       //const getCookies = document.cookie;
     if (getCookies.indexOf('cartId') > -1) {
        const cookiesCartID = getCookies.split(';');
        cookiesCartID  && cookiesCartID.length >0 ?
        Object.keys(cookiesCartID).forEach((cookiesCartIDitem) =>{
            const splitCookies  = cookiesCartID[cookiesCartIDitem].split('=')
                if(splitCookies[0] === 'cartId' || splitCookies[0] === ' cartId')  {
                cartId= splitCookies[1];
        		checkoutcartId=cartId;
            }
    }):null
     }
    console.log(checkoutcartId);
	getHrefForButton = $('.cvv-btn-continue').children('.cvv-continue').children().attr('href');
	$('.cvv-btn-continue').children('.cvv-continue').children().removeAttr("href");
	// using thie cookies value just for testing purpose , remove it after intrgation with other component

	const stateArray = ['Uttar-Pradesh', 'Andhra-Pradesh', 'Maharashtra'];
	let selectHTML = "<select class ='add-addr-feilds' name ='state'>";
	if (stateArray) {
		stateArray.forEach((item) => {
			selectHTML += "<option value='" + item + "'>" + item + "</option>";
		})
		selectHTML += "</select>";
        const getstatecollection= document.getElementById('state-collection');
		if(selectHTML && getstatecollection ){
    			//document.getElementById('state-collection').appendChild(selectHTML);
    			getstatecollection.innerHTML = selectHTML;
    		}
	}


});
let doValidation = false;
let submitForm = false;
let guestEmail = false;
let cvvSubmission = false;
let validatecardNExpDate = false;

function onValidatingGuestMail(event) {
	guestEmail = true;
	const regexEmail = /\S+@\S+\.\S+/;
	const getCustomerMailId = $('.checkout-guest-mail-id')[0].value;
	if (getCustomerMailId === '') {
		$('.customer-validation-message').text('Please Enter Email');
	} else if (!regexEmail.test(getCustomerMailId)) {
		$('.customer-validation-message').text('Please Enter Valid  Email');
	} else {
		getUserDeatils['email'] = getCustomerMailId;
		$('.customer-validation-message').text('');
		onToggleDescription(event);

	}

}


function validateGuestEmailkEUP(event) {
	if (guestEmail) {
		const regexEmail = /\S+@\S+\.\S+/;
		const getCustomerMailId = $('.checkout-guest-mail-id')[0].value;
		if (getCustomerMailId === '') {
			$('.customer-validation-message').text('Please Enter Email');
		} else if (!regexEmail.test(getCustomerMailId)) {
			$('.customer-validation-message').text('Please Enter Valid  Email');
		} else {
			getUserDeatils['email'] = getCustomerMailId;
			$('.customer-validation-message').text('');


		}

	}
}


function onToggleDescription(event) {
	//$(event).parent().parent()[0].classList.remove('toggle-checkout-description');
	const getAllSection = $('.check-out-section-description');
	const getAllEditButton = $('.edit-check-out-section');
	let trackIndex = '';
	if (getAllSection) {
		trackIndex = getAllSection.toArray().findIndex(function (descriptionItem, index) {
			return descriptionItem.classList.contains('toggle-checkout-description');
		})
		if (trackIndex <= getAllSection.toArray().length) {
			getAllSection.toArray()[trackIndex].classList.remove('toggle-checkout-description')
			getAllSection.toArray()[trackIndex + 1].classList.add('toggle-checkout-description')
		}
	}
	if (getAllEditButton) {
		getAllEditButton.toArray()[trackIndex].classList.add('toggle-checkout-description')
	}
}

function onEditCheckOutSection(event) {
	const getAllSection = $('.check-out-section-description');
	if (getAllSection) {
		getAllSection.toArray().forEach(function (descriptionItem, index) {
			descriptionItem.classList.remove('toggle-checkout-description')
		})

	}
	//$('.check-out-section-description').remove('toggle-checkout-description');
	$(event).parent().siblings()[0].classList.add('toggle-checkout-description');
	//$(event).remove('.toggle-checkout-description');

}

function onEnterDeatils(event) {
	const userDetails = event.name;
	const userDetailsValue = event.value;
	//$('.add-new-address-form').toggleClass('toggle-checkout-description')
	if (submitForm) {
		letValidateField(userDetails, userDetailsValue, event)
	}

}


/// method for saving and delivering
function onSaveNDeliver() {
	let validationFeilds = true;
	let validateFormFields = true
	submitForm = true;
	const getAllAddressFields = $('.add-addr-feilds');
	if (getAllAddressFields) {
		getAllAddressFields.toArray().forEach((fieldItem) => {
			const userDetails = fieldItem.name;
			const userDetailsValue = fieldItem.value;
			if (!userDetailsValue) {
				validateFormFields = false;
			}
			if (userDetails === 'Phone Number' && userDetailsValue) {
				const phoneReg = /^\d{10}$/;
				if (!userDetailsValue.match(phoneReg)) {
					validateFormFields = false;
				}
			}

			if (userDetails === 'Pin Number' && userDetailsValue) {
				const pinRegexp = /^[0-9]{5}(?:-[0-9]{4})?$/;
				if (!userDetailsValue.match(pinRegexp)) {

					validateFormFields = false;
				}
			}
			letValidateField(userDetails, userDetailsValue, fieldItem);


		})
	}
	if (!doValidation) {
		//console.log(userDetailsValue);
		getUserDeatils['cartId'] = cartId;
		getUserDeatils['region'] = "MH";
		getUserDeatils['region_id'] = 0;
		getUserDeatils['country_id'] = 'IN';
		getUserDeatils['region_code'] = 'MH';
		getUserDeatils['shipping_method_code'] = "flatrate";
		getUserDeatils['shipping_carrier_code'] = "flatrate";
	}
	if (validationFeilds && validateFormFields) {
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
				if (this.responseText) {
					const jsonObject = JSON.parse(this.responseText)
					getCodeskmu(jsonObject["payment_methods"])
				}
			}
		};
		xhttp.open("POST", "/bin/hclecomm/shipinfo", true);
		xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		xhttp.send(JSON.stringify(getUserDeatils));

	}


	function getCodeskmu(skmuObj) {
		skmuObj['cartId'] = cartId;
		const xhttp = new XMLHttpRequest();
		xhttp.onreadystatechange = function () {
			if (this.readyState == 4 && this.status == 200) {
                const message= JSON.parse(this.responseText)['message']['orderId']
                getUserDeatils['orderId']=message;
				onToggleDescription(event);
			}
		};
		xhttp.open("PUT", "/bin/hclecomm/createOrder", true);
		xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		xhttp.send(JSON.stringify(skmuObj));

	}

}


/* function onSaveNDeliver(){
         onToggleDescription(event);
	  }*/

function onAddCancel() {
	const getAllAddressFields = $('.add-addr-feilds');
	if (getAllAddressFields) {
		getAllAddressFields.toArray().forEach((fieldItem) => {
			if (fieldItem) {
				fieldItem.value = '';
				fieldItem.nextElementSibling.innerText = '';
			}
		})
	}
}


function onToggleNewADD() {
	const getPlusIcon = $('.add-new-plus-icon');
	if (getPlusIcon) {
		getPlusIcon.toArray().forEach((plusIconItem) => {
			plusIconItem.classList.contains('toggle-icon') ? plusIconItem.classList.remove('toggle-icon') :
				plusIconItem.classList.add('toggle-icon')
		})
	}
	$('.add-new-address-form').toggleClass('toggle-checkout-description')


}

function letValidateField(userDetails, userDetailsValue, event) {
	switch (userDetails) {
		case 'First Name':
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}
			getUserDeatils['firstname'] = userDetailsValue;
			break;

		case 'Last Name':
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}
			getUserDeatils['lastname'] = userDetailsValue;
			break;
		case 'Phone Number':
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}
			const phoneReg = /^\d{10}$/;
			if (!userDetailsValue.match(phoneReg)) {
				event.nextElementSibling.innerText = `Please Enter Valid ${userDetails}`;
				doValidation = true;
				return
			}
			getUserDeatils['telephone'] = userDetailsValue;

			break;
		case 'Pin Number':
			const pinRegexp = /^[0-9]{5}(?:-[0-9]{4})?$/;
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}

			if (!userDetailsValue.match(pinRegexp)) {
				event.nextElementSibling.innerText = `Please Enter valid ${userDetails}`;
				doValidation = true;
				return
			}
			getUserDeatils['postcode'] = userDetailsValue

		case 'City':
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}
			getUserDeatils['city'] = userDetailsValue;
			break;

		case 'Street':
			if (!userDetailsValue) {
				event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
				doValidation = true;
				return
			}
			const streetArr = [
				userDetailsValue
			];

			getUserDeatils['street'] = streetArr;
			break;
		case 'state':
			return
			break;

		case 'optional-phone':
			return


			break;
		default:
			break;
	}
	event.nextElementSibling.innerText = '';
	doValidation = false;


}

function onSelecCVV() {
	const getCvvSection = $('.billing-cvv-section');
	if (getCvvSection) {
		getCvvSection.toArray().forEach((cvvSectionItem) => {
			cvvSectionItem.classList.contains('add-new-billing-address') ?
				cvvSectionItem.classList.remove('add-new-billing-address') : cvvSectionItem.classList.add('add-new-billing-address')
		})
	}
}

function onContinueOnlyCvv() {
	cvvSubmission = true;
	//const cvvRegex = /^[0-9]{3,4}$/;
	const getCvvSection = $('.cvv-number')[0].value;
	if (getCvvSection === '') {
		$('.cvv-validation-mssg')[0].innerText = 'Please Enter CVV';
		return
	}
	if (!Number(getCvvSection) || (getCvvSection).length !== 3) {
		$('.cvv-validation-mssg')[0].innerText = 'Please Enter Valid CVV';
		return
	}

	$('.cvv-validation-mssg')[0].innerText = '';
	window.location.href = getHrefForCvvButton;
}

function onCvvKeyUp() {
	if (cvvSubmission) {
		const getCvvSection = $('.cvv-number')[0].value;
		if (getCvvSection === '') {
			$('.cvv-validation-mssg')[0].innerText = 'Please Enter CVV';
			return
		}
		if (!Number(getCvvSection) || (getCvvSection).length !== 3) {
			$('.cvv-validation-mssg')[0].innerText = 'Please Enter Valid CVV';
			return
		}

		$('.cvv-validation-mssg')[0].innerText = '';
	}
}


function onContinueCvv() {
	validatecardNExpDate = true;
	let validateCardNExpiry = true;
	const regexCradNumber = /^\d{16}$/;
	const getNewCardNumber = $('.new-card');
	const getCardNumberValidation = $('.new-card-validation');

	const getNewCardExpiryDate = $('.new-card-expiry-date');
	if (!regexCradNumber.test(getNewCardNumber[0].value)) {
		$('.new-card-validation')[0].innerText = 'Please Enter A valid Card Number';
		validateCardNExpiry = false;
	} else {
		$('.new-card-validation')[0].innerText = ' ';
		validateCardNExpiry = true;
	}
	let todayDate = '';
	let todayMonth = new Date().getMonth() + 1;
	todayMonth > 10 ? todayMonth = todayMonth : todayMonth = `${'0' + todayMonth}`
	new Date().getDate() > 10 ? todayDate = new Date().getDate() : todayDate = `${'0' + new Date().getDate()}`
	const getDateFormat = `${new Date().getFullYear()}-${todayMonth}-${todayDate}`

	if (getDateFormat <= getNewCardExpiryDate[0].value) {
		$('.new-card-expiry-date-validation')[0].innerText = 'Please Enter A Valid Date';
		validateCardNExpiry = false;
		return
	}

	if (getNewCardExpiryDate[0].value == '') {
		$('.new-card-expiry-date-validation')[0].innerText = 'Please Enter Expiry Date';
		validateCardNExpiry = false;
		return
	}
	$('.new-card-expiry-date-validation')[0].innerText = ' ';
	validateCardNExpiry = true;
	console.log('set local storage and rediret confirmation page');
    const checkOutDetails = {...getUserDeatils, cardNumber : getNewCardNumber[0].value, cardExpDate : getNewCardExpiryDate[0].value}
    console.log(checkOutDetails)
    localStorage.setItem('checkOutDetails', JSON.stringify(checkOutDetails));
	window.location.href = getHrefForCvvButton;


}


function onValidateCardNumber() {
	if (validatecardNExpDate) {
		const regexCradNumber = /^\d{16}$/;
		const getNewCardNumber = $('.new-card');
		const getCardNumberValidation = $('new-card-validation');
		if (!regexCradNumber.test(getNewCardNumber[0].value)) {
			$('.new-card-validation')[0].innerText = 'Please Enter A valid Card Number';
			validateCardNExpiry = false;
		} else {
			$('.new-card-validation')[0].innerText = ' ';
			validateCardNExpiry = true;
		}
	}
}


function onValidateCardExpiryDate() {
	const getNewExpDate = $('.new-card-expiry-date');
	if (validatecardNExpDate) {
		const getCardNumberValidation = $('.new-card-validation');
		let todayDate = '';
		let todayMonth = new Date().getMonth() + 1;
		todayMonth > 10 ? todayMonth = todayMonth : todayMonth = `${'0' + todayMonth}`
		new Date().getDate() > 10 ? todayDate = new Date().getDate() : todayDate = `${'0' + new Date().getDate()}`
		const getDateFormat = `${new Date().getFullYear()}-${todayMonth}-${todayDate}`

		if (getDateFormat <= getNewExpDate[0].value) {
			$('.new-card-expiry-date-validation')[0].innerText = 'Please Enter A Valid Date';
			validateCardNExpiry = false;
		} else {
			$('.new-card-expiry-date-validation')[0].innerText = ' ';
			validateCardNExpiry = true;
		}
	}
}
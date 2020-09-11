const getUserDeatils = {};
//let cartId = 'aUETIXpuqYFyr9EEx1F7XjMXL4RBmBer';
let checkoutcartId = '';
getHrefForCvvButton = '';
getHrefForNewButton = '';
let getExpiryMonth = '1';
let getExpiryYear = '';
let currentMonth = '';
let currentYear = '';
let customerEmail = '';
let deleveryOptions = '';
let selectedCountry = '';
let selectedState = '';
let filterStateDetails = null;
let selectHTMLstate = null;

$(document).ready(function () {

    let userData = getUserCookie("hcluser");
    if(userData != "") {
         custToken = JSON.parse(userData).customerToken;
     customerEmail = JSON.parse(userData).email;
    }
    /// creating  drop down for month and year

    currentYear = new Date().getFullYear();
    currentMonth = new Date().getMonth() +1 ;
    getExpiryYear = currentYear
    const getCalenderPlaceHolderForYear = $('.new-card-expiry-calender-year');
    let yearOptions = "";
    if(currentYear && getCalenderPlaceHolderForYear){
        for (let i = currentYear; i <= currentYear+12; i++) {
            yearOptions += '<option value="' + i + '">' + i + '</option>';
            }
        getCalenderPlaceHolderForYear.append( yearOptions);

    }

    const getCalenderPlaceHolderForMonth = $('.new-card-expiry-calender-month');
    let monthOptions = "";
    if( getCalenderPlaceHolderForMonth){
        for (let i = 1; i <= 12; i++) {
            monthOptions += '<option value="' + i + '">' + i + '</option>';
            }
        getCalenderPlaceHolderForMonth.append(monthOptions);

    }

    /// adding event listner to month and year expiry for card
    if( (getCalenderPlaceHolderForYear[0]  && getCalenderPlaceHolderForYear[0].length >0)
       && (getCalenderPlaceHolderForMonth[0]  && getCalenderPlaceHolderForMonth[0].length >0) ){
    getCalenderPlaceHolderForYear[0].addEventListener("change", function(event){
        getExpiryYear = event.target.value;
        if( getExpiryYear === currentYear && currentMonth >   getExpiryMonth && validatecardNExpDate){

                $('.new-card-expiry-date-validation')[0].innerText = 'Expiry Date is not Valid';
        }
        else{
        $('.new-card-expiry-date-validation')[0].innerText = ' ';

        }
    });
    getCalenderPlaceHolderForMonth[0].addEventListener("change", function(event){
        getExpiryMonth = event.target.value;

        if( getExpiryYear === currentYear && (currentMonth >   getExpiryMonth) && validatecardNExpDate){

                $('.new-card-expiry-date-validation')[0].innerText = 'Expiry Date is not Valid';
        }
        else{
        $('.new-card-expiry-date-validation')[0].innerText = ' ';

        }
    });

    }

    //using cookies
    if($('.checkout-guest-mail-id')[0])
            $('.checkout-guest-mail-id')[0].value = customerEmail;

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
    getHrefForCvvButton = $('.cvv-btn-continue').children('.cvv-continue').children().attr('href');
    $('.cvv-btn-continue').children('.cvv-continue').children().removeAttr("href");
    // using thie cookies value just for testing purpose , remove it after intrgation with other component

    if(document.querySelector(".add-new-address-form #country")){
       getCountriesList();
    }
});

const mapUserFields = function (useraddressdetails){
	const getAddressFilelds = $(".add-addr-feilds");
    if(useraddressdetails && getAddressFilelds){
        getAddressFilelds.toArray().forEach((addressfielditems) => {
                                            switch(addressfielditems.name){
            case 'First Name':
        addressfielditems.value = useraddressdetails[0].firstname;
         break;

            case 'Last Name':
        addressfielditems.value = useraddressdetails[0].lastname;
         break;

            case 'Pin Number':
        addressfielditems.value = useraddressdetails[0].postcode;
         break;

            case 'City':
        addressfielditems.value = useraddressdetails[0].city;
         break;

            case 'Street':
        addressfielditems.value = useraddressdetails[0].street.join();
         break;

            case 'Phone Number':
        addressfielditems.value = useraddressdetails[0].telephone;
         break;

         case 'Country':


         let selectedcountryforuser=null;
            let othercountryforuser=null;
            let selectedcountrybyid="<option value= null />  select country </option>";
  		const getCountryListSelectedItem = $(".add-new-address-form #country");
countriesList.forEach(item => {

                if(useraddressdetails[0] && useraddressdetails[0].country_id===item.country_id){
					selectedcountrybyid = "<option value='"+ item.country_id + "' />" + item.country_name + "</option>";
                 getSelectedState(useraddressdetails[0].region.region_code,item)
                    //updateStateList(useraddressdetails[0].country_id , null);
                }
                else{
					 othercountryforuser+="<option value='"+ item.country_id + "' />" + item.country_name + "</option>";

                }

                selectedcountryforuser = selectedcountrybyid + othercountryforuser;




            });
			if(getCountryListSelectedItem && selectedcountryforuser ) {
                getCountryListSelectedItem[0].innerHTML = selectedcountryforuser;

            }
         break;
        }

                                          })}
}
let countriesList = [];
let doValidation = false;
let submitForm = false;
let guestEmail = false;
let cvvSubmission = false;
let validatecardNExpDate = false;

const getCountriesList = function () {
    let url = '/bin/hclecomm/countrystatelist';
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200 ) {
            countriesList = JSON.parse(this.responseText);

            //update the country select box
            let selectHTMLCnty = '<option value="none">Select Country</option>';
            const countryELm = document.getElementById('country');
            countriesList.forEach(item => {
                 selectHTMLCnty += "<option value='"+ item.country_id + "' onclick = 'selectCountry(this)' />" + item.country_name + "</option>";
            });
            if(countryELm && selectHTMLCnty) {
                countryELm.innerHTML = selectHTMLCnty;
            }
            countryELm.addEventListener('change', function(event) {
                updateStateList(this.value,event);
            });
        }
    }
    xhttp.open('GET',url,true);
    xhttp.setRequestHeader('Content-Type','application/json;charset=UTF-8');
    xhttp.send();
}

const selectCountry = function(Element){

   selectedCountry= $( "#country option:selected" ).text();

}

const selectState = function(Element){

   selectedState= $( "#state option:selected" ).text();

}

const updateStateList = function(countryId,event) {
    //this method is also a onchange trigger for country select
    //if (submitForm) {letValidateField(event.name, event.value, event)};
    //console.log(event);
    onEnterDeatils(event);

    const stateELm = document.getElementById('state');
    selectHTMLstate = '<option value="none">Select State</option>';
    stateELm.innerHTML = selectHTMLstate;
    stateELm.disabled = true;
    if(countryId == "none") {
        return ;
    }
    let countryItem = countriesList.filter(item => item.country_id == countryId);
    if(countryItem[0].states && countryItem[0].states.length > 0 ){
        countryItem[0].states.forEach(st => {
           selectHTMLstate += "<option value='"+ st.region_code + "' data-attribute='"+ st.region_id +"'/>" + st.state_name + "</option>";
        })
        stateELm.disabled = false;
        stateELm.innerHTML = selectHTMLstate;
    }
}

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
    selectedState= $( "#state option:selected" ).text();
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
                const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
                if (!userDetailsValue.match(pinRegexp)) {

                    validateFormFields = false;
                }
            }
            if (userDetails === 'Country' && userDetailsValue == 'none') {
                validateFormFields = false;
            }

            if (userDetails === 'State') {
               if($('.add-new-address-form #country').val() == 'none' || $('.add-new-address-form #state option').length <= 1) {
                    return;
                } else if($('.add-new-address-form #state option').length > 1 && userDetailsValue == 'none') {
                    validateFormFields = false;
                }
            }

            letValidateField(userDetails, userDetailsValue, fieldItem);

        })
    }
    if(!checkoutcartId && !custToken){
        $('.empty-cartid').text("The Cart is empty");

        return;
    }
    const getPorductDetails = JSON.parse(localStorage.getItem('productDescription'));
    if(!getPorductDetails){
        $('.checkouttotal-cmp').text("The Cart is empty");

        return;
    }
    if (!doValidation && checkoutcartId) {
        $('.empty-cartid').text("");
        getUserDeatils['cartId'] = checkoutcartId;
        getUserDeatils['region'] = getUserDeatils['region_code']; //MH
        if(!getUserDeatils['region_id']) {
            console.log("no region id");
            getUserDeatils['region_id'] = 0;
        }
        //getUserDeatils['country_id'] = 'IN';
        //getUserDeatils['region_code'] = 'MH';

        getUserDeatils['shipping_method_code'] = "flatrate";
        getUserDeatils['shipping_carrier_code'] = "flatrate";
    }
    if (validationFeilds && validateFormFields) {
        onToggleDescription(event);
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
            const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidation = true;
                return
            }
            if (userDetailsValue.match(pinRegexp) === null) {
                event.nextElementSibling.innerText = `Please Enter valid ${userDetails}`;
                doValidation = true;
                return
            }
            getUserDeatils['postcode'] = userDetailsValue;
            break;

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
        case 'Country':
            if (userDetailsValue == "none") {
               event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidation = true;
                return
            }
            getUserDeatils['country_id'] = userDetailsValue;
            getUserDeatils['country_name'] = $(".add-new-address-form #country option:selected").text();
            break;

        case 'State':
            if (userDetailsValue == "none") {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidation = true;
                return
            }
            getUserDeatils['region_code'] = userDetailsValue;
            let stElm = document.querySelector(".add-new-address-form #state");
            getUserDeatils['region_id'] = "0";
            break;

        case 'optional-phone':
            return
            break;
        default:
            break;
    }
    if(event.type === "change"){
        event.target.nextElementSibling.innerText = '';
    } else {
        event.nextElementSibling.innerText = '';
    }
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
    let paymentMode = document.getElementById("cod").checked ? 'COD' : 'Card'
    let validateCardNExpiry = true;
    let validateCardNo = true;
    const regexCradNumber = /^\d{16}$/;
    const getNewCardNumber = $('.new-card');
    const getCardNumberValidation = $('.new-card-validation');
    if(paymentMode == 'Card') {
        validatecardNExpDate = true;
        if (!regexCradNumber.test(getNewCardNumber[0].value)) {
            $('.new-card-validation')[0].innerText = 'Please Enter A Card Number';
            validateCardNExpiry = false;
            validateCardNo= false;
        } else {
            $('.new-card-validation')[0].innerText = ' ';
            validateCardNo = true;
           validateCardNExpiry = true;
        }
         if( getExpiryYear === currentYear && currentMonth >   getExpiryMonth ){
            $('.new-card-expiry-date-validation')[0].innerText = 'Please Enter A Valid Date';
             return
        }
   }
    if(paymentMode == 'COD' || (validateCardNExpiry && validateCardNo)){
    //localStorage.setItem('checkOutDetails', JSON.stringify(checkOutDetails));
     const xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200 ) {
                if (this.responseText) {
                    const jsonObject = JSON.parse(this.responseText)
                    if(!jsonObject["status"]  ){
                        localStorage.removeItem("checkOutDetails");
                        //document.cookie = "cartId" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                        getHrefForCvvButton ? window.location.href = getHrefForCvvButton : null;
                        return;
                    }
                    else{
                    getCodeskmu(jsonObject["payment_methods"], paymentMode)
                    }
                }
            }
        };
        xhttp.open("POST", "/bin/hclecomm/shipinfo", true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.setRequestHeader("CustomerToken", custToken);
        xhttp.send(JSON.stringify(getUserDeatils));
    }
        function getCodeskmu(skmuObj, pmtMod) {
        if(custToken)
        {
            let paymentMode = skmuObj['code'];//what is this?
            skmuObj = getUserDeatils;
            skmuObj['code'] = paymentMode;
        }
        else {
              skmuObj['cartId'] = checkoutcartId;
        }
                var productDescriptionJSON = JSON.parse(localStorage.getItem("productDescription"));
                    skmuObj['delivercharges'] = productDescriptionJSON["delivercharges"];
                    skmuObj['coupondiscount'] = productDescriptionJSON["coupondiscount"];
                    if (localStorage.getItem("storeAddress") != null) {
                       skmuObj['storeAddress'] = JSON.parse(localStorage.getItem("storeAddress"));
                                     }

        const xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                const message= JSON.parse(this.responseText)['message']['orderId']
                getUserDeatils['orderId']=message;
                getUserDeatils['selectedCountry'] = selectedCountry;
                getUserDeatils['selectedState'] = selectedState;
                const checkOutDetails = {...getUserDeatils,PaymentMode : pmtMod, cardNumber : getNewCardNumber[0].value, cardExpDate : `${getExpiryMonth}-${getExpiryYear}`}
                console.log(checkOutDetails)
                localStorage.setItem('checkOutDetails', JSON.stringify(checkOutDetails));
                document.cookie = "cartId" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                getHrefForCvvButton ? window.location.href = getHrefForCvvButton : null;
            }
        };
        xhttp.open("PUT", "/bin/hclecomm/createOrder", true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.setRequestHeader("CustomerToken", custToken);
        xhttp.send(JSON.stringify(skmuObj));
    }
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

        if (getDateFormat >= getNewExpDate[0].value) {
            $('.new-card-expiry-date-validation')[0].innerText = 'Please Enter A Valid Date';
            validateCardNExpiry = false;
        } else {
            $('.new-card-expiry-date-validation')[0].innerText = ' ';
            validateCardNExpiry = true;
        }
    }
}

function showHideCardDetails() {
document.getElementById('cardDetails').style.display = document.getElementById('card').checked ? 'block' : 'none';
}

const onDeliveryMethodChange = (ele) =>{
    const getPlaceHolderDeliveryOption = $('.store-delivery-options');
    const getAddressSection = document.getElementById("addressSection");
    if(ele.id=== 'storeAdd'){
    getPlaceHolderDeliveryOption.css('display', 'flex');
     $('.confirmation-page-deleivery-option').css('display','block');
     getAddressSection.style.display = "none";
    let url = '/bin/hclecomm/pickupstorelist';
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200 ) {
            deleveryOptions = JSON.parse(this.responseText);

            if(deleveryOptions && deleveryOptions.length> 0){

            storeDeliveryOption = deleveryOptions.map((deleveryOptionsItem, index) =>{

                return (`<div class = "store-address"><div class = "store-radiobutton-contactname"><input type="radio"  name="store"   onchange = "onSelectingStore('${index}')"><div>${deleveryOptionsItem.contact_name}</div></div>
                <div>${deleveryOptionsItem.name}</div>
                <p>${deleveryOptionsItem.street}</p>
                 <p>${deleveryOptionsItem.phone}</p>
                 <p><span>${deleveryOptionsItem.city}</span><span>(${deleveryOptionsItem.postcode})</span></p>
                   <p>${deleveryOptionsItem.region}</p></div>`)
            }).join(' ');
            const storelistcontainer = `<div><div class = "store-header-select"><span>SELECT STORE</span><i class="fa fa-window-close" aria-hidden="true" onclick = "closeStoreOptions()"></i></div>${storeDeliveryOption}</div>`
            if(getPlaceHolderDeliveryOption && storeDeliveryOption){
            getPlaceHolderDeliveryOption.addClass('confirmation-page-deleivery-option-transition');
                getPlaceHolderDeliveryOption[0].innerHTML = storelistcontainer;
            }

        }

        }
    }

    xhttp.open('GET',url,true);
    xhttp.setRequestHeader('Content-Type','application/json;charset=UTF-8');
    xhttp.send();

    }
    else {
        getPlaceHolderDeliveryOption.removeClass('confirmation-page-deleivery-option-transition');
        getAddressSection.style.display = "block";
        localStorage.removeItem('storeAddress');
    }
window.scrollTo(0, 0);
}

const onSelectingStore = (storeAddressIndex) =>{
     const getPlaceHolderDeliveryOption = $('.store-delivery-options');
    getPlaceHolderDeliveryOption.css('display', 'none');
     $('.confirmation-page-deleivery-option').css('display','none');
    localStorage.removeItem('storeAddress');
    const storeAdressStringyfy = JSON.stringify(deleveryOptions[storeAddressIndex]);
    localStorage.setItem('storeAddress', storeAdressStringyfy);
   console.log(deleveryOptions[storeAddressIndex]);
   setAddressDefaults();
   onToggleDescription(event);
}

function setAddressDefaults() {
    getUserDeatils['cartId'] = checkoutcartId;
    getUserDeatils['region'] = 'NA';
    getUserDeatils['region_id'] = 0;
    getUserDeatils['shipping_method_code'] = "flatrate";
    getUserDeatils['shipping_carrier_code'] = "flatrate";
    getUserDeatils['firstname'] = 'NA';
    getUserDeatils['lastname'] = 'NA';
    getUserDeatils['telephone'] = 'NA';
    getUserDeatils['postcode'] = 'NA';
    getUserDeatils['city'] = 'NA';
    const streetArr = ['NA'];
    getUserDeatils['street'] =streetArr;
    getUserDeatils['country_id'] = 'NA';
    getUserDeatils['region_code'] = 'NA';
}

function closeStoreOptions(){

    const getPlaceHolderDeliveryOption = $('.store-delivery-options');
    getPlaceHolderDeliveryOption.css('display', 'none');
     $('.confirmation-page-deleivery-option').css('display','none');

}
function getSelectedState( region_code , item){
    let selectedState = '<option value="none">Select State</option>';
    let otherState = null;
    let selectHTMLstate = null;
    if(item && item.states){
        item.states.forEach((stateitem) =>{
                            if(stateitem.region_code === region_code){
selectedState = "<option value='"+ stateitem.region_code + "' data-attribute='"+ stateitem.region_id +"'/>" + stateitem.state_name + "</option>";
        }
        else {
            otherState = "<option value='"+ stateitem.region_code + "' data-attribute='"+ stateitem.region_id +"'/>" + stateitem.state_name + "</option>";
        }


                            }) ;
selectHTMLstate = selectedState + otherState ;
                const stateELm = document.getElementById('state');
    			stateELm.disabled = false;
         stateELm.innerHTML = selectHTMLstate;
    }
}

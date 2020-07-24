const getUserDeatilsAddAddress = {};
let addcountriesList = [];
let doValidationAddAddress = false;
let submitFormAddAddress = false;
let addAddresscartId=null;
let region_code=null;
$( document ).ready(function() {
getCountriesListDetails();
  });

const getCountriesListDetails = function () {
    let url = '/bin/hclecomm/countrystatelist';
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200 ) {
            addcountriesList = JSON.parse(this.responseText);

            //update the country select box
            let selectHTMLCnty = '<option value="none">Select Country</option>';
            const countryELm = document.getElementById('country');
            addcountriesList.forEach(item => {
                 selectHTMLCnty += "<option value='"+ item.country_id + "' />" + item.country_name + "</option>";
            });
            if(countryELm && selectHTMLCnty) {
                countryELm.innerHTML = selectHTMLCnty;
            }
            if(countryELm){
            countryELm.addEventListener('change', function(event) {
                updateStateListAdd(this.value,event);
            });
            }
        }
    }
    xhttp.open('GET',url,true);
    xhttp.setRequestHeader('Content-Type','application/json;charset=UTF-8');
    xhttp.send();
}

const updateStateListAdd = function(countryId,event) {
    onEnterDeatilsAdd(event);
    const stateELm = document.getElementById('state');
    let selectHTMLstate = '<option value="none">Select State</option>';
    stateELm.innerHTML = selectHTMLstate;
    stateELm.disabled = true;
    if(countryId == "none") {
        return ;
    }
    let countryItem = addcountriesList.filter(item => item.country_id == countryId);
    if(countryItem[0].states && countryItem[0].states.length > 0 ){
        countryItem[0].states.forEach(st => {
           selectHTMLstate += "<option value='"+ st.region_code + "' data-attribute='"+ st.region_id +"'/>" + st.state_name + "</option>";
        })
        stateELm.disabled = false;
        stateELm.innerHTML = selectHTMLstate;
    }
}


function onEnterDeatilsAdd(event) {
    const userDetails = event.name;
    const userDetailsValue = event.value;
    //$('.add-new-address-form').toggleClass('toggle-checkout-description')
    debugger


        letValidateFieldAdd(userDetails, userDetailsValue, event)


}

function onSaveNDeliverAddress() {
    let validationFeilds = true;
    let validateFormFields = true
    submitFormAddAddress = true;
    const getAllAddressFields = $('.add-addr-feilds-add-address');
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
               if($('.add-addr-feilds-add-address #country').val() == 'none' || $('.add-addr-feilds-add-address #state option').length <= 1) {
                    return;
                } else if($('.add-addr-feilds-add-address #state option').length > 1 && userDetailsValue == 'none') {
                    validateFormFields = false;
                }
            }

            letValidateFieldAdd(userDetails, userDetailsValue, fieldItem);


        })
    }
    // if(!checkoutcartId && !custToken){
    //     $('.empty-cartid').text("The Cart is empty");

    //     return;
    // }


    if (!doValidationAddAddress) {
		 let userData = getUserCookie("hcluser");
            if (userData != "") {
                addressCustToken = JSON.parse(userData).customerToken;
                console.log("add addressCustToken ::"+addressCustToken);
                getUserDeatilsAddAddress['email'] = JSON.parse(userData).email;
                 console.log("email ::"+JSON.parse(userData).email);
                 getUserDeatilsAddAddress['first_name']=JSON.parse(userData).firstname;
                 getUserDeatilsAddAddress['last_name']=JSON.parse(userData).lastname;
            }

        $('.empty-cartid').text("");

  console.log("region_code outside"+ region_code);
        getUserDeatilsAddAddress['region'] = region_code;
		getUserDeatilsAddAddress['region_code'] = region_code;
        if(!getUserDeatilsAddAddress['region_id']) {
            console.log("no region id");
            getUserDeatilsAddAddress['region_id'] = 0;
        }




        const xhttp = new XMLHttpRequest();
    		xhttp.onreadystatechange = function () {
    			if (this.readyState == 4 && this.status == 200 ) {
    				if (this.responseText) {
    					const jsonObject = JSON.parse(this.responseText)
                      console.log("Servlet Response"+this.responseText)
    				}
    			}
    		};
    		xhttp.open("PUT", "/bin/hclecomm/addAddress", true);
    		xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    		xhttp.setRequestHeader("CustomerToken", addressCustToken);
    		xhttp.send(JSON.stringify(getUserDeatilsAddAddress));

    	}
    }








function onAddCancelAddress() {
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


function letValidateFieldAdd(userDetails, userDetailsValue, event) {
    switch (userDetails) {
        case 'First Name':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['firstname'] = userDetailsValue;
            break;

        case 'Last Name':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['lastname'] = userDetailsValue;
            break;
        case 'Phone Number':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            const phoneReg = /^\d{10}$/;
            if (!userDetailsValue.match(phoneReg)) {
                event.nextElementSibling.innerText = `Please Enter Valid ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['telephone'] = userDetailsValue;

            break;
        case 'Pin Number':
            const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            if (userDetailsValue.match(pinRegexp) === null) {
                event.nextElementSibling.innerText = `Please Enter valid ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['postcode'] = userDetailsValue;
            break;

        case 'City':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['city'] = userDetailsValue;
            break;

        case 'Street':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            const streetArr = [
                userDetailsValue
            ];

            getUserDeatilsAddAddress['street'] = streetArr;
            break;
        case 'Country':
            if (userDetailsValue == "none") {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['country_id'] = userDetailsValue;
            //getUserDeatils['country_name'] = $(".add-new-address-form #country option:selected").text();
            break;

        case 'State':
            if (userDetailsValue == "none") {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            debugger
            region_code=userDetailsValue;
            getUserDeatilsAddAddress['region_code'] = userDetailsValue;
            console.log("region_code inside"+ getUserDeatilsAddAddress['region_code']);
            let stElm = document.querySelector("#state");
            getUserDeatilsAddAddress['region_id'] = stElm.options[stElm.selectedIndex].getAttribute('data-attribute');
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
    doValidationAddAddress = false;


}

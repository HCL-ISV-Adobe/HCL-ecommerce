let getUserDeatilsAddAddress = {};
const getUserBillingDeatils = {};
const customer={};
let billingandShippingAddress=true;
let addcountriesList = [];
let doValidationAddAddress = false;
let submitFormAddAddress = false;
let addAddresscartId=null;
let region_code=null;
let region_code_billing=null;

const getCountriesListDetails = function (country_id,region_code) {

    let url = '/bin/hclecomm/countrystatelist';
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200 ) {
            addcountriesList = JSON.parse(this.responseText);

            //update the country select box
            let selectHTMLCnty = '<option value="none">Select Country</option>';
            const countryELm = document.getElementById('country');
 			const updatecountryELm = document.getElementById('update-country');
             const billingcountryELm = document.getElementById('billing-country');
              let selectedcountry=null;
                let othercountry=null;

            addcountriesList.forEach(item => {
            if(!country_id){
                 selectHTMLCnty += "<option value='"+ item.country_id + "' />" + item.country_name + "</option>";
            }else{

                if(country_id===item.country_id){
					selectedcountry = "<option value='"+ item.country_id + "' />" + item.country_name + "</option>";
                    updateList(country_id,region_code,item);
                }
                else{
					othercountry+="<option value='"+ item.country_id + "' />" + item.country_name + "</option>";

                }
                selectHTMLCnty=selectedcountry+othercountry;

            }


            });
            if(countryELm && selectHTMLCnty && billingcountryELm && !country_id) {
                countryELm.innerHTML = selectHTMLCnty;
                billingcountryELm.innerHTML=selectHTMLCnty;

            }
             if(updatecountryELm && selectHTMLCnty && country_id) {
                updatecountryELm.innerHTML = selectHTMLCnty;
            }

            if(countryELm){
               countryELm.addEventListener('change', function(event) {
               updateDropdown(this.value,event,'state');
               });
            }
            if(billingcountryELm){
                 billingcountryELm.addEventListener('change', function(event) {
                 updateDropdown(this.value,event,'billing-state');
            });
            }
             if(updatecountryELm){
            updatecountryELm.addEventListener('change', function(event) {
                updateList(this.value);
            });
             }

        }
    }
    xhttp.open('GET',url,true);
    xhttp.setRequestHeader('Content-Type','application/json;charset=UTF-8');
    xhttp.send();
}
const updateList = function(countryId,region_code,item) {

    const stateELm = document.getElementById('update-state');
    let selectHTMLstate = null;
    if(!item){
		selectHTMLstate='<option value="none">Select State</option>';
    }
    else{
        if(item.states && item.states.length>0){
			item.states.forEach(st => {
             if(st.region_code===region_code){
                  selectHTMLstate = "<option value='"+ st.region_code + "' data-attribute='"+ st.region_id +"'/>" + st.state_name + "</option>";
            }

        })

        }
    }

    stateELm.innerHTML = selectHTMLstate;
    stateELm.disabled = true;
    if(countryId == "none") {
        return ;
    }
    let countryItem = addcountriesList.filter(item => item.country_id == countryId);
    if(countryItem[0].states && countryItem[0].states.length > 0 ){
        countryItem[0].states.forEach(st => {
        if(st.region_code!==region_code){
              selectHTMLstate += "<option value='"+ st.region_code + "' data-attribute='"+ st.region_id +"'/>" + st.state_name + "</option>";
        }

        })
        stateELm.disabled = false;
        stateELm.innerHTML = selectHTMLstate;
    }
}
const updateDropdown = function(countryId,event,dropValue) {
    onEnterDeatilsAdd(event);
    const stateELm = document.getElementById(dropValue);
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


function onEnterDeatilsAdd(event,updatestate) {
    const userDetails = event.name;
    const userDetailsValue = event.value;
    letValidateFieldAdd(userDetails, userDetailsValue, event,updatestate)
}

function onSaveNDeliverAddress(updateAddress,index,updateflag) {

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
        	 if (userDetails === 'Billing Phone Number' && userDetailsValue && !billingandShippingAddress) {
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

        	 if (userDetails === 'Billing Pin Number' && userDetailsValue && !billingandShippingAddress) {
                const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
                if (!userDetailsValue.match(pinRegexp)) {

                    validateFormFields = false;
                }
            }
            if (userDetails === 'Country' && userDetailsValue == 'none') {
                validateFormFields = false;
            }

        	  if (userDetails === 'Billing Country' && userDetailsValue == 'none' && !billingandShippingAddress) {
                validateFormFields = false;
            }


            if (userDetails === 'State') {
               if($('.add-addr-feilds-add-address #country').val() == 'none' || $('.add-addr-feilds-add-address #state option').length <= 1) {
                    return;
                } else if($('.add-addr-feilds-add-address #state option').length > 1 && userDetailsValue == 'none') {
                    validateFormFields = false;
                }
            }
        	if (userDetails === 'Billing State' && !billingandShippingAddress) {
               if($('.add-addr-feilds-add-address #billing-country').val() == 'none' || $('.add-addr-feilds-add-address #billing-state option').length <= 1) {
                    return;
                } else if($('.add-addr-feilds-add-address #billing-state option').length > 1 && userDetailsValue == 'none') {
                    validateFormFields = false;
                }
            }

            letValidateFieldAdd(userDetails, userDetailsValue, fieldItem);


        })
    }



    if (!doValidationAddAddress) {
		 let userData = getUserCookie("hcluser");
            if (userData != "") {
                addressCustToken = JSON.parse(userData).customerToken;
                customer['email'] = JSON.parse(userData).email;
                 customer['firstname']=JSON.parse(userData).firstname;
                 customer['lastname']=JSON.parse(userData).lastname;
                  customer['websiteId']=1;

            }

        $('.empty-cartid').text("");



		  if(!getUserDeatilsAddAddress['region_id']) {
            getUserDeatilsAddAddress['region_id'] = 0;
        }
		 if(!getUserBillingDeatils['region_id']) {
            getUserBillingDeatils['region_id'] = 0;
        }
		const region_ship={"region":region_code,"region_code":region_code,"region_id":getUserDeatilsAddAddress['region_id']};
		const region_bill={"region":region_code_billing,"region_code":region_code_billing,"region_id":getUserBillingDeatils['region_id']};
		getUserDeatilsAddAddress['region']=region_ship;
		getUserBillingDeatils['region']=region_bill;
		getUserDeatilsAddAddress['region_id']=0;
		getUserBillingDeatils['region_id']=0;





		var addresss=[];
        if(!updateAddress){
             if(billingandShippingAddress){

			getUserDeatilsAddAddress['default_billing']=true;
			getUserDeatilsAddAddress['default_shipping']=true;

			addresss.push(getUserDeatilsAddAddress);
			customer['addresses']=addresss;

        }else{
			delete getUserDeatilsAddAddress['default_billing'];

			getUserDeatilsAddAddress['default_shipping']=true;
			getUserBillingDeatils['default_billing'] = true;
			addresss.push(getUserDeatilsAddAddress);
			addresss.push(getUserBillingDeatils);
			customer['addresses']=addresss;
        }

        }
        else{
            if(userAddress && userAddress.length > 1){
            if(index==='0'){
                 addresss.push(getUserDeatilsAddAddress);
                 addresss.push(userAddress[1]);
            }
            else{
                 addresss.push(userAddress[0]);
                 addresss.push(getUserDeatilsAddAddress);
            }
            }
            else{
				delete userAddress[0].customer_id;
				delete userAddress[0].id;
                var global_address=JSON.parse(globalAddress);
                delete global_address[0].customer_id;
				delete global_address[0].id;
                if(updateflag==='update_shipping'){
					delete userAddress[0].default_billing;
                    delete global_address[0].default_shipping;
					addresss.push(userAddress[0]);
                    addresss.push(global_address[0]);
                }
                else{
                    delete userAddress[0].default_shipping;
                    delete global_address[0].default_billing;
                    addresss.push(global_address[0]);
                    addresss.push(userAddress[0]);
                }
            }
            customer['addresses']=addresss;
        }

        const userDetailsObj = {customer}
           const xhttp = new XMLHttpRequest();
    		xhttp.onreadystatechange = function () {
    			if (this.readyState == 4 && this.status == 200 ) {
    				if (this.responseText) {
    					const jsonObject = JSON.parse(this.responseText);
                      	 if(jsonObject && jsonObject.status)  {
                        	window.location.reload();
                         }
    				}
    			}
    		};
    		xhttp.open("PUT", "/bin/hclecomm/addAddress", true);
    		xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    		xhttp.setRequestHeader("CustomerToken", addressCustToken);
    		xhttp.send(JSON.stringify(userDetailsObj));

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


function letValidateFieldAdd(userDetails, userDetailsValue, event,updatestate) {
    switch (userDetails) {
        case 'First Name':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['firstname'] = userDetailsValue;
            break;

        case 'Billing First Name':
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['firstname'] = userDetailsValue;
            break;

        case 'Last Name':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['lastname'] = userDetailsValue;
            break;

        case 'Billing Last Name':
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['lastname'] = userDetailsValue;
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

         case 'Billing Phone Number':
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            const phoneRegbilling = /^\d{10}$/;
            if (!userDetailsValue.match(phoneRegbilling) && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter Valid ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['telephone'] = userDetailsValue;
            break;

        case 'Pin Number':
            const pinRegexp = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
            if (!userDetailsValue ) {
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

         case 'Billing Pin Number':
            const pinRegexpbilling = /^[0-9]{5,6}(?:-[0-9]{4})?$/;
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            if (userDetailsValue.match(pinRegexpbilling) === null && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter valid ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['postcode'] = userDetailsValue;
            break;

        case 'City':
            if (!userDetailsValue) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['city'] = userDetailsValue;
            break;

        case 'Billing City':
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['city'] = userDetailsValue;
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

        case 'Billing Street':
            if (!userDetailsValue && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Enter ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            const streetArrBilling = [
                userDetailsValue
            ];

            getUserBillingDeatils['street'] = streetArrBilling;
            break;
        case 'Country':
            if (userDetailsValue == "none") {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserDeatilsAddAddress['country_id'] = userDetailsValue;
            break;

        case 'Billing Country':
            if (userDetailsValue == "none" && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }
            getUserBillingDeatils['country_id'] = userDetailsValue;
            break;

        case 'State':
            if (userDetailsValue == "none") {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }

            region_code=userDetailsValue;
			if(!updatestate){
            let stElm = document.querySelector("#state");
            getUserDeatilsAddAddress['region_id'] = stElm.options[stElm.selectedIndex].getAttribute('data-attribute');
            }else{
                let stElm = document.querySelector("#update-state");
            getUserDeatilsAddAddress['region_id'] = stElm.options[stElm.selectedIndex].getAttribute('data-attribute');
            }
            break;

         case 'Billing State':
            if (userDetailsValue == "none" && !billingandShippingAddress) {
                event.nextElementSibling.innerText = `Please Select ${userDetails}`;
                doValidationAddAddress = true;
                return
            }

            region_code_billing=userDetailsValue;
            let stElmBilling = document.querySelector("#billing-state");
            getUserBillingDeatils['region_id'] = stElmBilling.options[stElmBilling.selectedIndex].getAttribute('data-attribute');
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
const onSelectingAddress = () =>{
         $('.billing-address').toggleClass('billing-address-display');
    	 billingandShippingAddress=!billingandShippingAddress;
    }

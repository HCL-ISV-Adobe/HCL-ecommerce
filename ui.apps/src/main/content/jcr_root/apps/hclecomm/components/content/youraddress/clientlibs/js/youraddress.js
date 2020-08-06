let userAddress=null;
$( document ).ready(function() {
   let addressCustToken = null;

            let userData = getUserCookie("hcluser");
            if (userData != "") {
                addressCustToken = JSON.parse(userData).customerToken;
            }
            const xhttp = new XMLHttpRequest();
            if (addressCustToken) {
                xhttp.onreadystatechange = function() {

                    if (this.readyState == 4 && this.status == 200) {

                        userAddress = JSON.parse(this.responseText);

                        if (userAddress && userAddress.length > 0) {
                        $('.cmp-my-address--add-address').css('display', 'none');
                   const userALLSection = userAddress.map((userAddressItems,index) => {
                        return (
                        `
           	            <div class ='cmp-my-address-users-address'>
                             <p>Shipping Address: <p>
                        	<p>${userAddressItems.firstname} ${userAddressItems.lastname}<p>
                        	<p>${userAddressItems.street.join()}, ${userAddressItems.city}<p>
                        	<p>Post Code-${userAddressItems.postcode}<p>
                        	<p>Mob No - ${userAddressItems.telephone}<p>
                                <a class ='cmp-my-address-update-user-address' onclick = "updateUserProfile('${index}')">Update</a>
						 </div>

						<div class ='cmp-my-address-users-address'>
                            <p> Billing Address: <p>
                        	<p>${userAddressItems.firstname} ${userAddressItems.lastname}<p>
                                <p>${userAddressItems.street.join()}, ${userAddressItems.city}<p>
                        	<p>Post Code-${userAddressItems.postcode}<p>
                        	<p>Mob No - ${userAddressItems.telephone}<p>
                              <a class ='cmp-my-address-update-user-address' onclick = "updateUserProfile('${index}')">Update</a>
						 </div>

                        `)})


                            const userAddressPlaceHolder = $('.cmp-my-address--user-address');
                            if (userAddressPlaceHolder[0] && userAddress) {
                                for(var i=0;i<userAddress.length;i++){
 					               if(userAddress[i].default_shipping && userAddress[i].default_billing){
                                    userAddressPlaceHolder[0].innerHTML=userALLSection.join(" ");
                               		 }
                                	else if(userAddress[i].default_shipping){
									 const userShippingAddress = (
        								`<div class ='cmp-my-address-users-address'>
                                               <p>Shipping Address: <p>
                        					<p>${userAddress[i].firstname} ${userAddress[i].lastname}<p>
                                         <p>${userAddress[i].street.join()}, ${userAddress[i].city}<p>
                      						  <p>Post Code-${userAddress[i].postcode}<p>
                     						   <p>Mob No - ${userAddress[i].telephone}<p>
                                                 <a class ='cmp-my-address-update-user-address' onclick = "updateUserProfile('${i}')">Update</a>
										 </div>
									  `  )

									userAddressPlaceHolder[0].innerHTML += userShippingAddress;
                                }
                                else if(userAddress[i].default_billing){
									 const userBillingAddress = (
        								`<div class ='cmp-my-address-users-address'>
                                               <p>Billing Address: <p>
                        					<p>${userAddress[i].firstname} ${userAddress[i].lastname}<p>
                                         <p>${userAddress[i].street.join()}, ${userAddress[i].city}<p>
                      						  <p>Post Code-${userAddress[i].postcode}<p>
                     						   <p>Mob No - ${userAddress[i].telephone}<p>
                                                   <a class ='cmp-my-address-update-user-address' onclick = "updateUserProfile('${i}')">Update</a>

										 </div>
									  `  )
									userAddressPlaceHolder[0].innerHTML += userBillingAddress;
                                }
                                }
                            }
                        }
                    };


                }
                    xhttp.open("GET", "/bin/hclecomm/customerAddress", true);
                    xhttp.setRequestHeader("CustomerToken", addressCustToken);
                   xhttp.send();
                 }
            });

let addressFormsfields = [];
let addresseFormName = [];
let  fieldObj = {};
const onUserUpdateFormFieldChange = (ele) =>{
    fieldObj[ele.name] =  ele.value;
    const findIndex = addresseFormName.indexOf(ele.name);
    switch(ele.name) {
        case 'first name':
            if(ele.value.length > 3){
                if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
            }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }

            }

        break;
        case 'street':
        if(ele.value.length > 0){
                if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
            }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }

         }
        break;
         case 'area':
        if(ele.value.length > 0){
                if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
            }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }
         }
        break;
         case 'zone':
        if(ele.value.length > 0){
                if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
            }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }
             }
        break;

       case 'mobile number':

        if(ele.value.length > 0){
                const mobRegex = new RegExp(/^[789]\d{9}$/);
                if(mobRegex.test(ele.value)){
                   if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
                }
                else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }

            }

        }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }

            }
        break;

        case 'company':
        if(ele.value.length > 0){
                if(findIndex === -1) {
                    addresseFormName.push(ele.name);
                }
            }
            else {
                if (findIndex > -1) {
                addresseFormName.splice(findIndex, 1);
                }
        break;

}

}

if(addresseFormName && addresseFormName.length ===6){
            $('.update-address-update-btn').removeClass('update-address-update-btn--disabled').removeAttr('disabled');

        }
        else {
            $('.update-address-update-btn').addClass('update-address-update-btn--disabled').attr("disabled", true);
        }
}





const onClosePop = () =>{
    $('.cmp-my-address--user-update-form').css('display', 'none');
}


const updateUserProfile = (index) =>{

    $('.cmp-my-address--user-update-form').css('display', 'block');
   const userUpdateForm = (
        `
          <div class ='add-new-address-form-add-address'>
    			<div class='updated-user-form--header'> <p>Update Details</p>
                    <i class="fa fa-window-close" aria-hidden="true" onclick = "onClosePop()"></i>

                </div>

 			 <div class="add-new-address-bottom-container">
                <p class='empty-cartid'></p>
                <div>
                    <div>
                        <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='First Name'
                        placeholder="First Name"
    					value = ${userAddress[index].firstname}
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                            <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Last Name'
                        placeholder="Last Name"
    					value = ${userAddress[index].lastname}
                        />
                            <p class ='error-message-add-address'></p>
                    </div>
                    </div>
                <div>
                    <div>
                        <input type="text"
                        class ='add-addr-feilds-add-address' onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Pin Number'
                        placeholder="Pin Code"
    					value=${userAddress[index].postcode}
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                    <input type="text"
                    class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='City'
                    placeholder="City"
    				value=${userAddress[index].city}
                    />
                    <p class ='error-message-add-address'></p>
                    </div>
                </div>

                <div>
                    <div>
                    <textarea class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)" name ='Street'
                    placeholder="Street/Apartment"

                    >${userAddress[index].street.join()}</textarea>
                    <p class ='error-message-add-address txt-area-err-message'></p>
                    </div>
                </div>
                <div>
                    <div>
                    <input type="text" class ='add-addr-feilds-add-address'
                    placeholder="Phone Number"
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='Phone Number'
					value=${userAddress[index].telephone}
    				/>

                    <p class ='error-message-add-address'></p>
                    </div>
                </div>
 				<div>
                    <div>
                        <select class="add-addr-feilds-add-address" name="Country" id="update-country">
                            <option value="none">Select Country</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                        <select class="add-addr-feilds-add-address" name="State" id="update-state" disabled="disabled" onChange="onEnterDeatilsAdd(this,'update-state')">
                            <option value="none">Select State</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>
                </div>

    <button class = 'update-address-update-btn ' onclick = "onSaveNDeliverAddress('${userAddress[index]}','${index}')">Update Address</button>
</div>

        `
    )

   const userAddressFormPlaceHolder = $('.cmp-my-address--user-update-form');
    if(userAddressFormPlaceHolder ){
        userAddressFormPlaceHolder[0].innerHTML = userUpdateForm;
    }

    getCountriesListDetails(userAddress[index].country_id,userAddress[index].region.region);
   getUserDeatilsAddAddress=userAddress[index];
}


const addUserAddress = () =>{
    $('.cmp-my-address--user-update-form').css({'display': 'block'} );

    $('.updated-user-form').css('height' , '600px');
   const addAddressForm = (
        `<div class ='add-new-address-form-add-address'>
      		 <div class='updated-user-form--header'> <p>Add Details</p>
                    <i class="fa fa-window-close" aria-hidden="true" onclick = "onClosePop()"></i>
                </div>
       <div class="add-new-address-bottom-container">
         <label>Shipping Address:</label>
                <p class='empty-cartid'></p>
                <div>
                    <div>
                        <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='First Name'
                        placeholder="First Name"
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                            <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Last Name'
                        placeholder="Last Name"
                        />
                            <p class ='error-message-add-address'></p>
                    </div>
                    </div>
                <div>
                    <div>
                        <input type="text"
                        class ='add-addr-feilds-add-address' onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Pin Number'
                        placeholder="Pin Code"
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                    <input type="text"
                    class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='City'
                    placeholder="City"
                    />
                    <p class ='error-message-add-address'></p>
                    </div>
                </div>
                <div>
                    <div>
                    <textarea class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)" name ='Street'
                    placeholder="Street/Apartment"
                    ></textarea>
                    <p class ='error-message-add-address txt-area-err-message'></p>
                    </div>
                </div>
                <div>
                    <div>
                    <input type="text" class ='add-addr-feilds-add-address'
                    placeholder="Phone Number"
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='Phone Number'/>
                    <p class ='error-message-add-address'></p>
                    </div>
                </div>
                <div>
                    <div>
                        <select class="add-addr-feilds-add-address" name="Country" id="country">
                            <option value="none">Select Country</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>

                    <div>
                        <select class="add-addr-feilds-add-address" name="State" id="state" disabled="disabled" onChange="onEnterDeatilsAdd(this)">
                            <option value="none">Select State</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>
                </div>
				<div class="switch-address">
				<label class="switch">
                <input type="checkbox" checked onchange="onSelectingAddress()">
                <span class="slider round"></span>

                </label>
                <span>Billing Address is same as Shipping Address</span>

       			</div>

    			<div class='billing-address'>
      		  <p class="billing-label"> Billing Address:</p>

				<div>
                    <div>
                        <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Billing First Name'
                        placeholder="First Name"
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                            <input
                        type="text"
                        class ='add-addr-feilds-add-address'
                        onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Billing Last Name'
                        placeholder="Last Name"
                        />
                            <p class ='error-message-add-address'></p>
                    </div>
                    </div>
                <div>
                    <div>
                        <input type="text"
                        class ='add-addr-feilds-add-address' onkeyup ="onEnterDeatilsAdd(this)"
                        name ='Billing Pin Number'
                        placeholder="Pin Code"
                        />
                        <p class ='error-message-add-address'></p>
                    </div>
                    <div>
                    <input type="text"
                    class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='Billing City'
                    placeholder="City"
                    />
                    <p class ='error-message-add-address'></p>
                    </div>
                </div>
                <div>
                    <div>
                    <textarea class ='add-addr-feilds-add-address'
                    onkeyup ="onEnterDeatilsAdd(this)" name ='Billing Street'
                    placeholder="Street/Apartment"
                    ></textarea>
                    <p class ='error-message-add-address txt-area-err-message'></p>
                    </div>
                </div>
                <div>
                    <div>
                    <input type="text" class ='add-addr-feilds-add-address'
                    placeholder="Phone Number"
                    onkeyup ="onEnterDeatilsAdd(this)"
                    name ='Billing Phone Number'/>
                    <p class ='error-message-add-address'></p>
                    </div>
                </div>
                <div>
                    <div>
                        <select class="add-addr-feilds-add-address" name="Billing Country" id="billing-country">
                            <option value="none">Select Country</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>

                    <div>
                        <select class="add-addr-feilds-add-address" name="Billing State" id="billing-state" disabled="disabled" onChange="onEnterDeatilsAdd(this)">
                            <option value="none">Select State</option>
                        </select>
                        <p class ='error-message-add-address'></p>
                    </div>
                </div>
       		</div>

                <div class ='add-address-btn'>
                    <button onclick = 'onSaveNDeliverAddress()'>Add Address </button>
                </div>
            </div>
       </div>

        `

    )

   const userAddressFormPlaceHolder = $('.cmp-my-address--user-update-form');
    if(userAddressFormPlaceHolder ){
        userAddressFormPlaceHolder[0].innerHTML = addAddressForm;
    }
	const onClosePop = () =>{
    $('.cmp-my-address--user-update-form').css('display', 'none');
	}
    getCountriesListDetails();
}
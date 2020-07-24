$( document ).ready(function() {
   let addressCustToken = null;

            let userData = getUserCookie("hcluser");
            if (userData != "") {
                addressCustToken = JSON.parse(userData).customerToken;
                console.log("addressCustToken::"+addressCustToken);
            }
            const xhttp = new XMLHttpRequest();
            if (addressCustToken) {
                console.log("In");
                xhttp.onreadystatechange = function() {

                    if (this.readyState == 4 && this.status == 200) {

                        userAddress = JSON.parse(this.responseText);

                        if (userAddress && userAddress.length > 0) {

                          $('.cmp-my-address--add-address').css('display', 'none');
                            const userAddSection = userAddress.map((userAddressItem) => {
                                //const userDeatails = {'firstname' : ${userAddressItem.firstname} };
                                return (
                                    `<div class ='cmp-my-address-users-address'>
                        <p>${userAddressItem.firstname} ${userAddressItem.lastname}<p>
                        <p>${userAddressItem.street.join()}<p>
                        <p>${userAddressItem.city}, ${userAddressItem.region.region}<p>
                        <p>Mob No - ${userAddressItem.telephone}<p>
                        <p>Company - ${userAddressItem.company}<p>
                        <a class ='cmp-my-address-update-user-address' onclick = "updateUserProfile('${userAddressItem.firstname}', '${userAddressItem.lastname}', '${userAddressItem.street.join()}',
                            '${userAddressItem.city}','${userAddressItem.region.region}', '${userAddressItem.telephone}',
                            '${userAddressItem.company}'

                        )">Update</a>
                    </div>`
                                )
                            })

                            const userAddressPlaceHolder = $('.cmp-my-address--user-address');
                            if (userAddressPlaceHolder[0] && userAddSection) {

                                userAddressPlaceHolder[0].innerHTML = userAddSection.join(" ");
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


const updateUserProfile = (...userAddDetails) =>{
    $('.cmp-my-address--user-update-form').css('display', 'block');
    const userObj = {
        'firstname' : userAddDetails[0],
        'lastname' : userAddDetails[1],
        'street' : [userAddDetails[2],userAddDetails[3],userAddDetails[4],],
       
        'telephone' : userAddDetails[5],
        'company' : userAddDetails[6],
    }
   const userUpdateForm = (
        `
            <form class='updated-user-form'>
                <div class='updated-user-form--header'> <p>Update Details</p>
                    <i class="fa fa-window-close" aria-hidden="true" onclick = "onClosePop()"></i>

                </div>
                <div>
                <input type ='text' value = ${userAddDetails[0]} placeholder ='first name' class ='update-form-field'
                name ='first name'  onkeyup = "onUserUpdateFormFieldChange(this)" disabled/>
               
                </div>
                <input type ='text' value = ${userAddDetails[1]} placeholder ='last name' class ='update-form-field'
                name ='last name' disabled
                />
                <div>
                <input type ='text' value = ${userAddDetails[2]} placeholder ='street' class ='update-form-field'
                name ='street' disabled
                />
               
                </div>

                <div>

                    <input type ='text' value = ${userAddDetails[3]} placeholder ='area' class ='update-form-field'
                    name ='area' disabled

                />
                 
                </div>
                 <div>
                <input type ='text' value = ${userAddDetails[4]} placeholder ='zone' class ='update-form-field'
                name ='zone' disabled
                />
               
                </div>
                <div>
                <input type ='text' value = ${userAddDetails[5]} placeholder ='mobile number' class ='update-form-field'
                name ='mobile number' disabled
                />
               
                </div>
                <div>
                <input type ='text' value = ${userAddDetails[6]} placeholder ='company' class ='update-form-field'
                 name ='company' disabled
                />
                 
                </div>
                <button class = 'update-address-update-btn update-address-update-btn--disabled' disabled >Update Address</button>
            </form>

        `

    )

   const userAddressFormPlaceHolder = $('.cmp-my-address--user-update-form');
    if(userAddressFormPlaceHolder ){
        userAddressFormPlaceHolder[0].innerHTML = userUpdateForm;
    }
}


const addUserAddress = () =>{
	getCountriesListDetails();

    $('.cmp-my-address--user-update-form').css({'display': 'block'} );
    $('.updated-user-form').css('height' , '600px');
   const addAddressForm = (
        `<div class ='add-new-address-form-add-address'>
      		 <div class='updated-user-form--header'> <p>Add Details</p>
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
                <div class ='add-address-btn'>

 

                    <!--/*<input type="button" value ='Save And Deliver Here'  class='continue-btn-add' onclick = "onSaveNDeliver()"/>*/-->
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
}
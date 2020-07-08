
function displayError(errorhtml, formElmName) {
    const ErrorMsgElm = document.getElementById('cmp-contactus-errormsg');
    document.contactus_form[formElmName].focus();
    ErrorMsgElm.style.visibility = "visible";
    ErrorMsgElm.innerHTML = errorhtml;
    return;
}
function validateSignupFrom(e) {
    const passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
    const mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    const phoneno = /^\d{10}$/;
    const ErrorMsgElm = document.getElementById('cmp-contactus-errormsg');
    let isValid = true;     
    ErrorMsgElm.innerHTML = "";
    ErrorMsgElm.style.visibility = "hidden";
    let errorhtml = "";
    //Cannot read property 'forEach' of undefined
    for(let i=0; i<document.contactus_form.elements.length;i++){
        if (document.contactus_form.elements[i].required) {
            errorhtml = '<span> Please enter '+document.contactus_form.elements[i].name+'<span>';
            displayError(errorhtml, document.contactus_form.elements[i].name);
            isValid = false;
            return false;
        }
    }
    // document.contactus_form.elements.forEach(element => {
    //     if (element.required) {
    //         errorhtml = '<span> Please enter '+element.name+'<span>';
    //         displayError(errorhtml, element.name);
    //         isValid = false;
    //         return false;
    //     }       

    // });
  
    if(isValid){
        debugger;
        return true;
    }
    // var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
    // var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    // var phoneno = /^\d{10}$/;
    // debugger;
    // if (document.signup_form.firstname.value == "") {
    //     errorhtml = '<span>Please enter First Name<span>';
    //     displayError(errorhtml, 'firstname');
    //     return false;
    // }
    // if (document.signup_form.lastname.value == "") {
    //     errorhtml = '<span>Please enter Last Name<span>';
    //     displayError(errorhtml, 'lastname');
    //     return false;
    // }
    // if (document.signup_form.email.value == "") {
    //     errorhtml = '<span>Please enter Email<span>';
    //     displayError(errorhtml, 'email');
    //     return false;
    // } else if (!document.signup_form.email.value.match(mailformat)) {
    //     errorhtml = '<span>Please enter Email in proper format<span>';
    //     displayError(errorhtml, 'email');
    //     return false;
    // }
    // if (document.signup_form.phone.value == "") {
    //     errorhtml = '<span>Please enter Phone Number<span>';
    //     displayError(errorhtml, 'phone');
    //     return false;
    // } else if (!document.signup_form.phone.value.match(phoneno)) {
    //     errorhtml = '<span>Phone number must be 10 digits<span>';
    //     displayError(errorhtml, 'phone');
    //     return false;
    // }
    // if (document.signup_form.password.value == "") {
    //     errorhtml = '<span>Please enter Password<span>';
    //     displayError(errorhtml, 'password');
    //     return false;
    // } else if (!document.signup_form.password.value.match(passw)) {
    //     errorhtml = '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter and lenght 8 to 10<span>';
    //     displayError(errorhtml, 'password');
    //     return false;
    // }
    // if (document.signup_form.cfpassword.value == "") {
    //     errorhtml = '<span>Please enter Confirm password <span>';
    //     displayError(errorhtml, 'cfpassword');
    //     return false;
    // }
    // if (document.signup_form.password.value !== document.signup_form.cfpassword.value) {
    //     errorhtml = '<span>Confirm password should be same as password<span>';
    //     displayError(errorhtml, 'cfpassword');
    //     return false;
    // }
    // if (document.signup_form.terms.checked === false) {
    //     errorhtml = '<span>Please accept terms and conditions<span>';
    //     displayError(errorhtml, 'terms');
    //     return false;
    // }


    // const formData = {
    //     firstname: document.signup_form.firstname.value,
    //     email: document.signup_form.email.value,
    //     phone: document.signup_form.phone.value,
    //     password: document.signup_form.password.value
    // }
    // formData.lastname = (document.signup_form.lastname.value) ? document.signup_form.lastname.value : "";


    // let url = '/bin/hclecomm/customerSignup';
    // await handleHttpServerRequestJson2(url,formData);


    
}
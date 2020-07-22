
if (document.contactus_form) {
    let formLabels = document.contactus_form.querySelectorAll('label');
    for (let i = 0; i < formLabels.length; i++) {
        if (document.contactus_form.querySelectorAll('label')[i].nextElementSibling.required) {
            let startHtml = document.createElement('span');
            startHtml.classList.add('star');
            startHtml.innerHTML = '*';
            document.contactus_form.querySelectorAll('label')[i].appendChild(startHtml);
        }
    }
}
function displayFormError(errorhtml, formElmName) {
    document.contactus_form[formElmName].focus();
    document.contactus_form[formElmName].style.borderColor = "red";
    const errorElm = document.contactus_form[formElmName].parentElement.nextElementSibling;
    errorElm.style.visibility = "visible";
    errorElm.innerHTML = errorhtml;
    return;
}

function validateContactFrom(e) {
    const passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
    const mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    const phoneno = /^\d{10}$/;
    const errorMsgElm = document.getElementsByClassName("cmp-contactus-errormsg");
    for (let i = 0; i < errorMsgElm.length; i++) {
        errorMsgElm[i].innerHTML = "";
        errorMsgElm[i].style.visibility = "hidden";
    }
    let isValid = true;
    let errorhtml = "";
    for (let i = 0; i < document.contactus_form.elements.length; i++) {
        document.contactus_form.elements[i].style.borderColor = "#d1d1d1";
        if (document.contactus_form.elements[i].required && document.contactus_form.elements[i].value === '') {
            errorhtml = '<span> Please enter ' + document.contactus_form.elements[i].name + '<span>';
            displayFormError(errorhtml, document.contactus_form.elements[i].name);
            isValid = false;
            return false;
        }
        if (document.contactus_form.elements[i].type == 'email' && !document.contactus_form.elements[i].value.match(mailformat)) {
            errorhtml = '<span> Please enter valid mail format <span>';
            displayFormError(errorhtml, document.contactus_form.elements[i].name);
            isValid = false;
            return false;
        }
        if (document.contactus_form.elements[i].type == 'tel' && !document.contactus_form.elements[i].value.match(phoneno)) {
            errorhtml = '<span>Please enter a valid phone number<span>';
            displayFormError(errorhtml, document.contactus_form.elements[i].name);
            isValid = false;
            return false;
        }
    }

    if (isValid) {
        return true;
    }
	
	const ErrorMsgElement = document.getElementById('cmp-contactus-errormsg');
    if(validateRecaptcha() == false) {
        ErrorMsgElement.style.visibility = "visible";
        ErrorMsgElement.innerHTML = '<span>please verify you are humann!<span>';
         isValid = false;
        return false;
    } else {
        ErrorMsgElement.style.visibility = "hidden";
        ErrorMsgElement.innerHTML = '';
    }

}
document.querySelector('.cancel-btn').addEventListener("click", function(){
    document.querySelector(".contactus form").reset();
  });


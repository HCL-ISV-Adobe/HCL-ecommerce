function changePasswordServerRequest(url, formdata) {
    loader(true);
    const othrParm = {
        headers: { "content-type": "application/json; charset=UTF-8", 'Accept': 'application/json' },
        body: JSON.stringify(formdata),
        method: "PUT"
    }

    fetch('/libs/granite/csrf/token.json')
        .then(
            (response) => { return response.json(); },
            (rejected) => {
                console.log(rejected);
            })
        .then(msg => {
            othrParm.headers['CSRF-Token'] = msg.token;
            return fetch(url, othrParm);
        })
        .then(
            (response) => { return response.json(); },
            (rejected) => {
                console.log(rejected);
            })
        .then(data => {
            loader(false);
            const status = (data.status) ? JSON.parse(data.status) : false;
            if (status === true) {
				document.cookie = "hcluser" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
                setTimeout(function(){window.location.pathname = '/content/hclecomm/us/en/sign-in.html';}, 1000);                  
            } else {
                let error = "Server status failed. ";
                if (data.message.error) {
                    console.log(data.message.error);
                    error += data.message.error;
                }
                console.log(error);
                const ErrorMsgElm = document.getElementById('cp-errormsg');
                ErrorMsgElm.style.visibility = "visible";
                ErrorMsgElm.innerHTML = '<span>' + error + '</span>';
            }
        })
        .catch((error) => {
            console.log('promise error', error);
        });
}
function displaySettingError(errorhtml, formElmName) {
    const ErrorMsgElm = document.getElementById('cp-errormsg');
    document.changePasswordForm[formElmName].focus();
    ErrorMsgElm.style.visibility = "visible";
    ErrorMsgElm.innerHTML = errorhtml;
    return;
}
async function validateChangePasswordForm(e) {                  
    const ErrorMsgElm = document.getElementById('cp-errormsg');
    ErrorMsgElm.innerHTML = "";
    ErrorMsgElm.style.visibility = "hidden";
    let errorhtml = "";
    var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
    if ((document.changePasswordForm.oldpassword.value == "") ||
        (!document.changePasswordForm.oldpassword.value.match(passw))) {
        errorhtml = (document.changePasswordForm.oldpassword.value == "") ? '<span>Please enter Old Password<span>' :
            '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter and lenght 8 to 10<span>';
        displaySettingError(errorhtml, 'oldpassword');
        return false;
    }
    if ((document.changePasswordForm.newpassword.value == "") ||
        (!document.changePasswordForm.newpassword.value.match(passw))) {
        errorhtml = (document.changePasswordForm.newpassword.value == "") ? '<span>Please enter New Password<span>' :
            '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter and lenght 8 to 10<span>';
        displaySettingError(errorhtml, 'newpassword');
        return false;
    }
    if (document.changePasswordForm.newpassword.value === document.changePasswordForm.oldpassword.value) {
        errorhtml = '<span>password should not be same as old password<span>';
        displaySettingError(errorhtml, 'confirmpassword');
        return false;
    }
    if ((document.changePasswordForm.confirmpassword.value == "") ||
        (!document.changePasswordForm.confirmpassword.value.match(passw))) {
        errorhtml = (document.changePasswordForm.confirmpassword.value == "") ? '<span>Please enter Confirm Password<span>' :
            '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter and lenght 8 to 10<span>';
        displaySettingError(errorhtml, 'confirmpassword');
        return false;
    }
    if (document.changePasswordForm.newpassword.value !== document.changePasswordForm.confirmpassword.value) {
        errorhtml = '<span>Confirm password should be same as password<span>';
        displaySettingError(errorhtml, 'confirmpassword');
        return false;
    }

    // getting token from cookie
    let customerToken = '';
    let custId = '';
	let userData = getUserCookie("hcluser");
	let id='';
    if(userData != "") {
             customerToken = JSON.parse(userData).customerToken;
    	     id = JSON.parse(userData).id;
            if(typeof id !== "undefined"){
    			custId = JSON.parse(userData).id;
            }
            else{
                custId = JSON.parse(userData).custId;
            }
    }

    const changePasswordData = {
        customerToken: customerToken,
        custId: custId.toString(),
        currentPassword: document.changePasswordForm.oldpassword.value,
        newPassword: document.changePasswordForm.newpassword.value
    }


    let url = '/bin/hclecomm/customerChangePassword';
    await changePasswordServerRequest(url, changePasswordData);


    return true;

}

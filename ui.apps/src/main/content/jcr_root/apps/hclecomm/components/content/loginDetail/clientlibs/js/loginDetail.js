function changePasswordServerRequest(url, formdata) {
    loader(true);
    const othrParm = {
        headers: { "content-type": "application/json; charset=UTF-8", 'Accept': 'application/json' },
        body: JSON.stringify(formdata),
        method: "POST"
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
                const ErrorMsgElm = document.getElementById('cp-errormsg');
                ErrorMsgElm.style.visibility = "visible";
                ErrorMsgElm.innerHTML = '<span> Password Updated Successfully </span>';
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
    const getCookies = document.cookie;
    if (getCookies.indexOf('hcluser') > -1) {
        const userCookies = getCookies.split(';');
        userCookies && userCookies.length > 0 ?
            (Object.keys(userCookies).forEach((item) => {
                const splitCookie = userCookies[item].split('=')
                if (splitCookie[0] === 'hcluser') {
                    customerToken = splitCookies[1].customerToken;
                    custId = splitCookie[1].custId;
                }
            })) : null;
    }
    const changePasswordData = {
        customerToken: customerToken,
        custId: custId,
        currentPassword: document.changePasswordForm.oldpassword.value,
        newpassword: document.changePasswordForm.newpassword.value
    }


    let url = '/bin/hclecomm/customerChangePassword';
    await changePasswordServerRequest(url, changePasswordData);


    return true;

}
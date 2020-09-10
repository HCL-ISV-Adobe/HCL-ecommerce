  const findRedriectUrl2 = function(domId) {
		let redirectURL = "";
		const params = new URLSearchParams(window.location.search);
		const keys = ['sign-in.html', 'sign-up.html'];
		if(params.has('referer')) {
			if(!keys.some(k => params.get('referer').includes(k) )) {
				redirectURL = params.get('referer');
                if(redirectURL.includes('checkout.html'))
                {
					redirectURL = domId.getAttribute("data-default");
                }
			}
		} else if(domId.getAttribute("data-default")) {
			redirectURL = domId.getAttribute("data-default");
		}
		return redirectURL;
	}

  const successSignUpCallback = (respData) =>  {
	if(checkUserCookie("hcluser") === false) {
		setUserCookie("hcluser",JSON.stringify(respData.message),1);
      	document.cookie = "cartId" +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	}
	setTimeout(function(){window.location = findRedriectUrl2(document.signup_form);}, 1000);
  }

  const handleHttpServerRequestJson2 = function (url, formdata) {
	  loader(true);
	const othrParm = {
	  headers: {"content-type":"application/json; charset=UTF-8", 'Accept': 'application/json'},
	  body: JSON.stringify(formdata),
	  method: "POST"
	}

	fetch('/libs/granite/csrf/token.json')
	.then(
		(response) => {return response.json();},
		(rejected) => {console.log(rejected);
	})
	.then( msg => {
			othrParm.headers['CSRF-Token'] = msg.token;
				return fetch(url, othrParm);
	})
	.then(
		(response) => {return response.json();},
		(rejected) => {console.log(rejected);
	})
	.then(data => {
		loader(false);
		const status = (data.status)?JSON.parse(data.status): false;
		if(status === true) {
		  successSignUpCallback(data);
		} else {
		  let error = "Email Already Exist . Please Sign up using another email or Sign in with registered credentials.";
		  if(data.message.error) {
			console.log(data.message.error);
			 error += data.message.error;
		  }
		  console.log(error);
		  const ErrorMsgElm = document.getElementById('cmp-signup-errormsg');
		  ErrorMsgElm.style.visibility = "visible";
		  ErrorMsgElm.innerHTML  = '<span>'+ error+ '</span>';
		}
	})
	.catch((error) => {
	  console.log('promise error',error);
	});
  }
function displayError(errorhtml, formElmName) {
	const ErrorMsgElm = document.getElementById('cmp-signup-errormsg');
	document.signup_form[formElmName].focus();
	ErrorMsgElm.style.visibility = "visible";
	ErrorMsgElm.innerHTML = errorhtml;
	return;
}
  async function validateSignupFrom(e) {
	  const ErrorMsgElm = document.getElementById('cmp-signup-errormsg');
	  ErrorMsgElm.innerHTML = "";
	  ErrorMsgElm.style.visibility = "hidden";

	  let errorhtml = "";
	  var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
	  var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	  var phoneno = /^\d{10}$/;
      var alphaExp = /^[A-Za-z]+$/

	  if (document.signup_form.firstname.value == "") {
		  errorhtml = '<span>Please enter First Name<span>';
		  displayError(errorhtml, 'firstname');
		  return false;
	  }

	  else if (!document.signup_form.firstname.value.match(alphaExp)){
      		  errorhtml = '<span>Invalid Characters in First Name field. Only Alphabets allowed.<span>';
      		  displayError(errorhtml, 'firstname');
      		  return false;
      	  }

	  if (document.signup_form.lastname.value == "") {
		  errorhtml = '<span>Please enter Last Name<span>';
		  displayError(errorhtml, 'lastname');
		  return false;
	  }
  else if (!document.signup_form.lastname.value.match(alphaExp)){
      		  errorhtml = '<span>Invalid Characters in Last Name field. Only Alphabets allowed.<span>';
      		  displayError(errorhtml, 'lastname');
      		  return false;
      	  }

	  if (document.signup_form.email.value == "") {
		  errorhtml = '<span>Please enter Email<span>';
		  displayError(errorhtml, 'email');
		  return false;
	  } else if (!document.signup_form.email.value.match(mailformat)) {
		  errorhtml = '<span>Please enter Email in proper format<span>';
		  displayError(errorhtml, 'email');
		  return false;
	  }
	  if (document.signup_form.phone.value == "") {
		  errorhtml = '<span>Please enter Phone Number<span>';
		  displayError(errorhtml, 'phone');
		  return false;
	  } else if (!document.signup_form.phone.value.match(phoneno)) {
		  errorhtml = '<span>Phone number must be 10 digits<span>';
		  displayError(errorhtml, 'phone');
		  return false;
	  }
	  if (document.signup_form.password.value == "") {
		  errorhtml = '<span>Please enter Password<span>';
		  displayError(errorhtml, 'password');
		  return false;
	  } else if (!document.signup_form.password.value.match(passw)) {
		  errorhtml = '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter and lenght 8 to 10<span>';
		  displayError(errorhtml, 'password');
		  return false;
	  }
	  if (document.signup_form.cfpassword.value == "") {
		  errorhtml = '<span>Please enter Confirm password <span>';
		  displayError(errorhtml, 'cfpassword');
		  return false;
	  }
	  if (document.signup_form.password.value !== document.signup_form.cfpassword.value) {
		  errorhtml = '<span>Confirm password should be same as password<span>';
		  displayError(errorhtml, 'cfpassword');
		  return false;
	  }
	  if (document.signup_form.terms.checked === false) {
		  errorhtml = '<span>Please accept terms and conditions<span>';
		  displayError(errorhtml, 'terms');
		  return false;
	  }
	if(typeof recaptchaValue !== 'undefined' && recaptchaValue == true){
		if(validateRecaptcha() == false) {
			errorhtml = '<span>please verify you are human!<span>';
			displayError(errorhtml, 'terms');
			return false;
		}
    }


	const formData = {
		firstname: document.signup_form.firstname.value,
		email: document.signup_form.email.value,
		phone: document.signup_form.phone.value,
		password: document.signup_form.password.value
	}
	formData.lastname = (document.signup_form.lastname.value)?document.signup_form.lastname.value:"";


	let url = '/bin/hclecomm/customerSignup';
	await handleHttpServerRequestJson2(url,formData);


	return true;
  }
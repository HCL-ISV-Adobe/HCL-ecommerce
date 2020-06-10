  const findRedriectUrl2 = function(domId) {
		let redirectURL = "/content/hclecomm/us/en/home.html";// default home url
		const params = new URLSearchParams(window.location.search);
		const keys = ['signin.html', 'signup.html'];
		if(params.has('referer')) {
			if(!keys.some(k => params.get('referer').includes(k) )) {
				redirectURL = params.get('referer');
			}
		} else if(domId.getAttribute("data-default")) {
			edirectURL = domId.getAttribute("data-default");
		}
		return redirectURL;
	}

  const successSignUpCallback = (respData) =>  {
	if(checkUserCookie("hcluser") === false) {
		//set cookies if rememberme is set, expiry time increased to 5 days
		// const exdays = (document.login_form.rememberme.checked)?5:1;
		setUserCookie("hcluser",JSON.stringify(respData.message),1);
	}
	setTimeout(function(){window.location = findRedriectUrl2(document.signup_form);}, 1000);
  }

  const handleHttpServerRequestJson2 = function (url, formdata) {
	const othrParm = {
	  headers: {"content-type":"application/json; charset=UTF-8", 'Accept': 'application/json'},
	  body: JSON.stringify(formdata),
	  method: "POST"
	}

	fetch(url, othrParm)
	.then((response) => {return response.json();}
		  ,(rejected) => {console.log(rejected);}
	)
	.then(data => {
		const status = (data.status)?JSON.parse(data.status): false;
		if(status === true) {
		  successSignUpCallback(data);
		} else {
		  let error = "Server status failed. ";
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
	  //server login validation failed
	});
  }

  async function validateSignupFrom(e) {
	var passw = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
	const ErrorMsgElm = document.getElementById('cmp-signup-errormsg');
	ErrorMsgElm.innerHTML = "";
	ErrorMsgElm.style.visibility = "hidden";
	// ErrorMsgElm.style.display = "none";
	let errorhtml = "";
	
	if(document.signup_form.firstname.value == "") {
	  document.signup_form.firstname.focus();
	  return false;
	}
	if(document.signup_form.lastname.value == "") {
		  document.signup_form.lastname.focus();
		  return false;
		}
	
	if(document.signup_form.email.value == "") {
	  document.signup_form.email.focus();
	  return false;
	}
	if(document.signup_form.phone.value == "") {
	  document.signup_form.phone.focus();
	  return false;
	}
	if(document.signup_form.password.value == "") {
	  document.signup_form.password.focus();
	  return false;
	} else if(!document.signup_form.password.value.match(passw)) {
		errorhtml = '<span>Password must contain at least one numeric digit, one uppercase and one lowercase letter<span>';
		ErrorMsgElm.style.visibility = "visible";
		ErrorMsgElm.innerHTML = errorhtml;
		document.signup_form.password.focus();
		return false;
	}
	if(document.signup_form.cfpassword.value == "") {
	  document.signup_form.cfpassword.focus();
	  return false;
	}
	if( document.signup_form.password.value !== document.signup_form.cfpassword.value) {
		document.signup_form.cfpassword.focus();
		return false;
	}
	if(document.signup_form.terms.checked === false) {
	  document.signup_form.terms.focus();
	  errorhtml = '<span>Please accept terms and conditions<span>';
	  ErrorMsgElm.style.visibility = "visible";
	  ErrorMsgElm.innerHTML = errorhtml;
	  return false;
	}

	//ajax call to catch server validation response
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
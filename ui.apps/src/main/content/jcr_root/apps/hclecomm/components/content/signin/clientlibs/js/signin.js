	const findRedriectUrl = function(domId) {
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

  const setUserCookie = function(cname,cvalue,exdays) {
	let d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	const expires = "expires=" + d.toGMTString();
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
  }

  const getUserCookie = function(cname) {
	let name = cname + "=";
	let decodedCookie  = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for(let i = 0; i < ca.length; i++) {
	  let c = ca[i].trim();
	  if(c.indexOf(name) === 0){
		return c.substring(name.length, c.length);
	  }
	}
	return "";
  }
  
  const checkUserCookie = function(cname) {
	if(getUserCookie("hcluser") != "") {
	  return true;
	} else {
	  return false;
	}
  }

  const handleHttpServerRequestJson = function (url, formdata) {
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
		console.log(JSON.parse(data.status));
		const status = (data.status)?JSON.parse(data.status): false;
		if(status === true) {
		  if(checkUserCookie("hcluser") === false){
			const exdays = (formdata.rememberme)?5:1;
			setUserCookie("hcluser",JSON.stringify(data.message),exdays);
		  }
		  console.log("Redirecting to the url in 1 seconds...");
		  setTimeout(function(){window.location = findRedriectUrl(document.login_form);}, 1000);
		} else {
		  let error = "Server status failed. ";
		  if(data.message.error) {
			console.log(data.message.error);
			 error = data.message.error;
		  }
		  console.log(error);
		  const errorElm = document.getElementById('cmp-login-errormsg');
		  errorElm.style.visibility = "visible";
		  errorElm.innerHTML  = '<span>'+ error+ '</span>';
		}
	})
	.catch((error) => {
	  console.log('promise error',error);
	});
				
  }
  
  async function validateLoginFrom(e) {
	const ErrorMsgElm = document.getElementById('cmp-login-errormsg');
	ErrorMsgElm.innerHTML = "";
	ErrorMsgElm.style.visibility = "hidden";
	if(document.login_form.username.value == "") {
	  document.login_form.username.focus();
	  return false;
	}
	if(document.login_form.password.value == "") {
	  document.login_form.password.focus();
	  return false;
	}
	
	const data = {  
		username: document.login_form.username.value,
		password: document.login_form.password.value,
		rememberme: document.login_form.rememberme.checked
	}
	
	let url = '/bin/hclecomm/customerSignin';
	await handleHttpServerRequestJson(url,data);
	
	return true;
  }
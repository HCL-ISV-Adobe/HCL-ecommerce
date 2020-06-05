var redirectURL = "http://www.hclstore.com";
  const params = new URLSearchParams(window.location.search)
  if(params.has('referer')) {
	redirectURL = params.get('referer');
  } else if(document.login_form.getAttribute("data-default")) {
	redirectURL = document.login_form.getAttribute("data-default");
  }

  const setCookie = function(cname,cvalue,exdays) {
	let d = new Date();
	d.setTime(d.getTime() + (exdays*24*60*60*1000));
	const expires = "expires=" + d.toGMTString();
	//should append to documnet.cookie
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
  }

  const getCookie = function(cname) {
	let name = cname + "=";
	let decodedCookie  = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for(let i = 0; i < ca.length; i++) {
	  let c = ca[i].trim();
	  console.log(c);
	  // while( c.chartAt(0) == ' '){
	  //   c = c.substring(1);
	  // }
	  if(c.indexOf(name) === 0){
		return c.substring(name.length, c.length);
	  }
	}
	return "";
  }
  
  const checkCookie = function(cname) {
	if(getCookie("hcluser") != "") {
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
	console.log(othrParm);

	fetch(url, othrParm)
	.then((response) => {return response.json();}
		  ,(rejected) => {console.log(rejected);}
	
	)
	.then(data => {
		console.log(JSON.parse(data.status));
		const status = (data.status)?JSON.parse(data.status): false;
		if(status === true) {
		  //set cookies if rememberme is set, expiry time increased to 5 days
		  if(checkCookie("hcluser") === false){
			const exdays = (formdata.rememberme)?5:1;
			setCookie("hcluser",JSON.stringify(data.message),exdays);
		  }
		  console.log("Redirecting to the url in 1 seconds...");
		  setTimeout(function(){window.location = redirectURL;}, 1000);
		} else {
		  let error = "Server status failed. ";
		  if(data.message.error) {
			console.log(data.message.error);
			 error += data.message.error;
		  }
		  console.log(error);
		  const errorElm = document.getElementById('cmp-login-errormsg');
		  errorElm.style.visibility = "visible";
		  errorElm.innerHTML  = '<span>'+ error+ '</span>';
		}
	})
	.catch((error) => {
	  console.log('promise error',error);
	  //server login validation failed
	});
				
  }
  
  async function validateLoginFrom(e) {
	
	if(document.login_form.username.value == "") {
	  document.login_form.username.focus();
	  return false;
	}
	if(document.login_form.password.value == "") {
	  document.login_form.password.focus();
	  return false;
	}
	
	//ajax call to catch server validation response
	const data = {  
		username: document.login_form.username.value,
		password: document.login_form.password.value,
		rememberme: document.login_form.rememberme.checked
	}
	
	let url = '/bin/hclecomm/customerSignin';
	// url = 'http://www.mocky.io/v2/5eda369e3300005f0079e9f2';
	// url = 'http://www.mocky.io/v2/5eda3b97330000530079ea24';// status false
	await handleHttpServerRequestJson(url,data);
	
	return true;
  }
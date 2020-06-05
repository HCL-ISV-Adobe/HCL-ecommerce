var redirectURL = "http://www.hclstore.com";
const params = new URLSearchParams(window.location.search)
if(params.has('referer')) {
redirectURL = params.get('referer');
} else if(document.login_form.getAttribute("data-default")) {
redirectURL = document.login_form.getAttribute("data-default");
}

const handleHttpServerRequestJson = function (url, data) {
const othrParm = {
  headers: {"content-type":"application/json; charset=UTF-8", 'Accept': 'application/json'},
  body: JSON.stringify(data),
  method: "POST"
}
console.log(othrParm);

fetch(url, othrParm)
.then((response) => {return response.json();}
		// let data = response.json();
		// console.log(response.json());
		// let res =  response.json();
		// console.log(res);
	  // },
	  ,(rejected) => {console.log(rejected);}

)
.then(data => {
	console.log(JSON.parse(data.status));
	const status = (data.status)?JSON.parse(data.status): false;
	if(status === true) {
	  console.log("Redirecting to the url in 1 seconds...");
	  setTimeout(function(){window.location = redirectURL;}, 3000);
	} else {
	  let error = "Server status failed. ";
	  if(data.message.error) {
		console.log(data.message.error);
		 error += data.message.error;
	  }
	  console.log(error);
	  const errorElm = document.getElementById('cmp-login-errormsg');
	  // const errorElm = document.querySelector('cmp-login-errormsg');
	  errorElm.style.visibility = "visible";
	  // errorElm.style.display = "block";
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

//const url = '/bin/hclecomm/customerSignin';
let url = 'http://www.mocky.io/v2/5eda369e3300005f0079e9f2';
// url = 'http://www.mocky.io/v2/5eda3b97330000530079ea24';// status false
await handleHttpServerRequestJson(url,data);

return true;
}
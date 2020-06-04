function validateLoginFrom(e) {

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
		password: document.login_form.password.value
	}
	const othrParm = {
	  headers: {
		"content-type":"application/json; charset=UTF-8"
	  },
	  body: JSON.stringify(data),
	  method: "POST"
	};
	const url = '/bin/hclecomm/customerSignin';
	fetch( url, othrParm)
	.then(response => {
		console.log(response);
		return response.json;
	})
	.then(result => {
	  console.log(result);
	  return true;
	  //redirect to returnURL after sucess
	})
	.catch(error => {
	  console.log(error);
	  return false;
	  //server login validation failed
	});
	return true;
}
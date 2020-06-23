	const findRedriectUrl = function(domId) {
		let redirectURL = "";
		const params = new URLSearchParams(window.location.search);
		const keys = ['signin.html', 'signup.html'];
		if(params.has('referer')) {
			if(!keys.some(k => params.get('referer').includes(k) )) {
				redirectURL = params.get('referer');
			}
		} else if(domId.getAttribute("data-default")) {
			redirectURL = domId.getAttribute("data-default");
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

  let userData = getUserCookie("hcluser");
    if(userData != "") {
         custToken = JSON.parse(userData).customerToken;
    }

const handleHttpServerRequestJson = function (url, formdata) {
            var othrParm = {
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
                const status = (data.status)?JSON.parse(data.status): false;
                if(status === true) {
                  if(checkUserCookie("hcluser") === false){
                    const exdays = (formdata.rememberme)?5:1;
                    setUserCookie("hcluser",JSON.stringify(data.message),exdays);
                               setUserCookie("cartId","",-1);
                        custToken = data.message.customerToken;
                        const xhttp = new XMLHttpRequest();
                   		 xhttp.onreadystatechange = function () {
                        if (this.readyState == 4 && this.status == 200) {
                            if(this.responseText)
                            {
								const data = JSON.parse(this.responseText)
                               const cartItem = [];
                               let cartId = "";
							   const cartdetails = [];
                               let totalPrice = 0;
                               if(data.length!=0){
									data.forEach((item) =>{
                                        console.log(item.name);
                               			const itemObj = {
                                            "image":"",
                                            "title":item.name,
                                            "qty" : item.qty,
                                            "price" :item.price
                                        }
                						totalPrice = totalPrice + (item.qty * item.price);
										cartId = item.quote_id;
                                        cartdetails.push({cartItem : itemObj})

                                    })

									document.cookie = "cartId = " + cartId + "; path=/";
                               		const fprice= totalPrice;
                                    const coupondiscount="";
                                    const delivercharges="";


                                    const productDescription = {
                                        cartdetails,
                                        fprice,
                                        coupondiscount,
                                        delivercharges
                                	}

                                    localStorage.setItem('productDescription', JSON.stringify(productDescription ))

                              }
                            }
                            else
                            {
                                 console.log('No cart Id present');
                            }
							setTimeout(function(){window.location = findRedriectUrl(document.login_form);}, 1000);
                        }
                		};
                        xhttp.open("GET", "/bin/hclecomm/getCustomerCart", true);
                        xhttp.setRequestHeader("CustomerToken", custToken);
                        xhttp.send();


                  }
                 // setTimeout(function(){window.location = findRedriectUrl(document.login_form);}, 1000);
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

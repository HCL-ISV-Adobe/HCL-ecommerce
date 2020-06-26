let hasCart = false;
const findRedriectUrl = function(domId) {
		let redirectURL = "";
		const params = new URLSearchParams(window.location.search);
		const keys = ['signin.html', 'signup.html'];
		if(params.has('referer')) {
			if(!keys.some(k => params.get('referer').includes(k) )) {
				redirectURL = params.get('referer');
                if(!hasCart && redirectURL.includes('checkout.html'))
                {
					redirectURL = "/content/hclecomm/us/en/home.html";
                }
			}
		} else if(domId.getAttribute("data-default")) {
			redirectURL = domId.getAttribute("data-default");
		}
		return redirectURL;
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
                               if(data.status == 200) {
                                   const cartItem = [];
                                   let cartId = "";
                                   const cartdetails = [];
                                   let totalPrice = 0;
                               		const cartDetail = JSON.parse(data.message);
                                   	if(cartDetail.length!=0){
                               			hasCart = true;
                                        cartDetail.forEach((item) =>{
                                            console.log(item.name);
                                            const itemObj = {
                                                "image":"https://www.hcltech.com/sites/default/files/styles/large/public/images/guideline_based1.png",
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
                                        const coupondiscount="0";
                                        const delivercharges="0";


                                        const productDescription = {
                                            cartdetails,
                                            fprice,
                                            coupondiscount,
                                            delivercharges
                                        }

                                        localStorage.setItem('productDescription', JSON.stringify(productDescription ))
                                 }

                              }
                              else if(data.status == 404)
                              {
									console.log('No cart Id present');
                                    localStorage.removeItem('productDescription');
                                    localStorage.removeItem('checkOutDetails');
                                    $('.checkouttotal-cmp').text("The Cart is empty");
                              }
                            }
							setTimeout(function(){window.location = findRedriectUrl(document.login_form);}, 1000);
                        }
                		};
                        xhttp.open("GET", "/bin/hclecomm/getCustomerCart", true);
                        xhttp.setRequestHeader("CustomerToken", custToken);
                        xhttp.send();
                  }
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

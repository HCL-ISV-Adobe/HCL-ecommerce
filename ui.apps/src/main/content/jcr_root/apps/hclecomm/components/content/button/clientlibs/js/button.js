$(function() {
            let userData = getUserCookie("hcluser");
            if(userData != "") {
                let username = (JSON.parse(userData).firstname)?JSON.parse(userData).firstname:"User";
                $('.signin').css('display','none');
                $('.signup').css('display','none');
                $('.logout').css('display','inline-block');
				$('.myaccount').css('display','inline-block');
                let html = '<span class="btn-cmp-logout-text">Hi, '+ username +'</span>';
                const logoutIconElm = document.querySelector('.btn-cmp-logout-icon');
                logoutIconElm.insertAdjacentHTML("beforebegin",html);
                document.querySelector('.logout').addEventListener('click', function(){
                    setUserCookie("hcluser","",-1);
                    setUserCookie("cartId","",-1);
                    location.reload();
                });
            } else {
            	 let currentWCMMode = getUserCookie("wcmmode");
				 if (currentWCMMode == "edit") {
							$('.logout').css('display', 'inline-block');
							$('.myaccount').css('display','inline-block');
				 }else{
					 $('.logout').css('display', 'none');
					 $('.myaccount').css('display','none');
				 }
                $('.signin').css('display','inline-block');
                $('.signup').css('display','inline-block');
            }
        });



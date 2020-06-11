$(function() {
            let userData = getUserCookie("hcluser");     
            if(userData != "") {
                let username = (JSON.parse(userData).firstname)?JSON.parse(userData).firstname:"User";
                $('.btn-cmp-signin').css('display','none');
                $('.btn-cmp-signup').css('display','none');
                $('.btn-cmp-logout').css('display','inline-block');
                let html = '<span class="btn-cmp-logout-text">Hi, '+ username +'</span>';
                const logoutIconElm = document.querySelector('.btn-cmp-logout-icon');
                logoutIconElm.insertAdjacentHTML("beforebegin",html);
                document.querySelector('.btn-cmp-logout > a').addEventListener('click', function(){
                    console.log("on click event listner code ")
                    setUserCookie("hcluser","",-1);
                    location.reload();					
                });
            } else {
            	 let currentWCMMode = getUserCookie("wcmmode");
				 if (currentWCMMode == "edit") {
							$('.btn-cmp-logout').css('display', 'inline-block');
				 }else{
					 $('.btn-cmp-logout').css('display', 'none');
				 }
                $('.btn-cmp-signin').css('display','inline-block');
                $('.btn-cmp-signup').css('display','inline-block');
            }
        });



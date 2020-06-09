$(function() {
            let userData = getUserCookie("hcluser");     
            if(userData != "") {
                //hide signin, create account buttons
                //enable logout button
                $('.btn-cmp-signin').css('display','none');
                $('.btn-cmp-signup').css('display','none');
                $('.btn-cmp-logout').css('display','inline-block');
                let html = '<span class="btn-cmp-logout-text">Hi, '+ JSON.parse(userData).firstname +'</span>';
                const logoutIconElm = document.querySelector('.btn-cmp-logout-icon');
                logoutIconElm.insertAdjacentHTML("beforebegin",html);
                document.querySelector('.btn-cmp-logout > a').addEventListener('click', function(){
                    console.log("on click event listner code ")
                    setUserCookie("hcluser","",-1);
                    
                    window.location=(logoutIconElm.getAttribute('data-logoutRedirect'))?logoutIconElm.getAttribute('data-logoutRedirect'):"/content/hclecomm/us/en/home.html";
					
                });
            } else {
                //hide logout button
                //enable sign, create account button
                $('.btn-cmp-logout').css('display','none');
                $('.btn-cmp-signin').css('display','inline-block');
                $('.btn-cmp-signup').css('display','inline-block');
            }
        });
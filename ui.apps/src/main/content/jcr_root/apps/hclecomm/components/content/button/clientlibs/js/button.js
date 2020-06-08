$(function() {

            let userData = getUserCookie("hcluser");
            if(userData != "") {
                
                //hide signin, create account buttons
                //enable logout button
                $('.btn-cmp-signin').css('display','none');
                $('.btn-cmp-signup').css('display','none');
                $('.btn-cmp-logout').css('display','inline-block');
                $('.btn-cmp-logout span').html(JSON.parse(userData).firstname);
                // JSON.parse(userData).firstname
            } else {
                //hide logout button
                //enable sign, create account button
                $('.btn-cmp-logout').css('display','none');
                $('.btn-cmp-signin').css('display');
                $('.btn-cmp-signin').css('display','inline-block');
                $('.btn-cmp-signup').css('display','inline-block');
            }
        });
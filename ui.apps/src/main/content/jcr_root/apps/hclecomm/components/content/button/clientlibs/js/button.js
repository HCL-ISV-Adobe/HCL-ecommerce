$(function() {    
    const userLogout = function(){
        setUserCookie("hcluser","",-1);
        setUserCookie("cartId","",-1);
        location.reload();
    }
    
    //if(checkmode && checkmode == "noEdit") {
    console.log('checkmode - buttonjs', checkmode)
    if(checkmode !== "edit") {
        const userData = getUserCookie("hcluser");
        console.log(userData)
        if(userData) {
            let customerName = (JSON.parse(userData).firstname)?JSON.parse(userData).firstname:"User";
            $('.signin').css('display','none');
            $('.signup').css('display','none');
            
            $('.logout').css('display','inline-block');
            let html = '<span class="btn-cmp-logout-text">Hi, '+ customerName +'</span>';
            document.querySelector('.btn-cmp-logout-icon').insertAdjacentHTML("beforebegin",html)
            $('.logout span.cmp-button__text').css('display','none');
            document.querySelector('.logout').addEventListener('click', userLogout);

            $('.myaccount').css('display','inline-block');    
        } else {
            $('.signin').css('display','inline-block');
            $('.signup').css('display','inline-block');
            $('.logout').css('display', 'none');
            //$('logout span.cmp-button__text').css('display','inline-block');
            $('.myaccount').css('display','none');
        }
    }
});

/*
in Edit mode:
1. Display all buttons
    - signin, signup, logout( implicitly with UserName)

In Non Edit mode
1. display based on the user cookies exist
    a) if user does not exist 
        - show signup, signin
        - hide logout, myaccount
    b) else if user does exist
        - show logout but replace the text with "Hi Username",
        - show myaccount
        - hide signin, signup
        
*/
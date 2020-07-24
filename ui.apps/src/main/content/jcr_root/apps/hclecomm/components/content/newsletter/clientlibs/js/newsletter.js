$(document).ready(function () {
    let userData = getUserCookie("hcluser");
    let custEmail;
    if(userData != "") {
         custEmail = JSON.parse(userData).email;
        if(document.querySelector(".cmp-newsletter-form .text  input")){
        	document.querySelector(".cmp-newsletter-form .text  input").value = custEmail;
        }
    }
 });
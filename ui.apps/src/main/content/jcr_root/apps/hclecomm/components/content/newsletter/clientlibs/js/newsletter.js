$(document).ready(function () {
    let userData = getUserCookie("hcluser");
    let custEmail;
    if(userData != "") {
         custEmail = JSON.parse(userData).email;
        document.querySelector(".cmp-newsletter-form .text  input").value = custEmail;
    }
 });
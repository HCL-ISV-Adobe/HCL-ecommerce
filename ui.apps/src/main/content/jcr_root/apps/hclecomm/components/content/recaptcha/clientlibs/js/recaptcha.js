var recaptchaValue = $('#recaptcha').data('custom-property');
if(recaptchaValue == true){
function validateRecaptcha(){

    var response = grecaptcha.getResponse();
    if(response.length == 0)
    {
        return false;
    }

	return true;

}
}
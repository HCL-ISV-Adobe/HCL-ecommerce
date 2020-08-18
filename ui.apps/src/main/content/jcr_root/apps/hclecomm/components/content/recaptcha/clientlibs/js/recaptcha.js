var recaptchaValue = $('#recaptcha').data('custom-property');
if(typeof recaptchaValue !== 'undefined' && recaptchaValue == true){
function validateRecaptcha(){

    var response = grecaptcha.getResponse();
    if(response.length == 0)
    {
        return false;
    }

	return true;

}
}
var value = $('#recaptcha').data('custom-property');
if(value){
function validateRecaptcha(){

    var response = grecaptcha.getResponse();
    if(response.length == 0)
    {
        return false;
    }

		return true;

}
}
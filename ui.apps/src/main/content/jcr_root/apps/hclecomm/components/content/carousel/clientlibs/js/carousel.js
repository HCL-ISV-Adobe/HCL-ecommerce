$(document).ready(function(){
    var numSlides = $('.cmp-carousel__item').length;
    if(numSlides==1)
    {
        $( ".cmp-carousel__action" ).remove();
        $( ".cmp-carousel__indicators" ).remove();

    }
});

$(document).ready(function () {

    // Single Slide
    var numSlides = $('.carousel-single .cmp-carousel__item').length;
    if (numSlides == 1) {
        $(".carousel-single .cmp-carousel__action").remove();
        $(".carousel-single .cmp-carousel__indicators").remove();

    }

    // Multi Slide 
    var itemsMainDiv = ('.carousel-multi .cmp-carousel');
    var itemsDiv = ('.carousel-multi .cmp-carousel__content');
    var delay =  $(".carousel-multi .cmp-carousel").attr("data-cmp-delay");
    var itemWidth = "";
    $('.carousel-multi .cmp-carousel__action--previous, .carousel-multi .cmp-carousel__action--next').click(function () {
        var condition = $(this).hasClass("cmp-carousel__action--previous");
        if (condition)
            click(0, this);
        else
            click(1, this)
    });

    ResCarouselSize();
    $(window).resize(function () {
        ResCarouselSize();
    });

    //this function define the size of the items
    function ResCarouselSize() {
        var incno = 0;
        var itemClass = $('.cmp-carousel__item');
        var id = 0;
        var sampwidth = $(itemsMainDiv).width();
        var bodyWidth = $('body').width();
        $('.cmp-carousel__item:nth-child(2)').addClass('active');
        (bodyWidth >= 768) ? $('.cmp-carousel__item:nth-child(2)').addClass('active') : $('.cmp-carousel__item:nth-child(1)').addClass('active');
        $(itemsDiv).each(function () {
            var itemNumbers = $(this).find(itemClass).length;
            incno = (bodyWidth >= 768) ? 3 : 1;
            itemWidth = sampwidth / incno;
            $(this).css({ 'transform': 'translateX(0px)', 'width': itemWidth * itemNumbers });
            $(this).find(itemClass).each(function () {
                $(this).outerWidth(itemWidth);
            });

            $(".cmp-carousel__action--previous").addClass("over");
            $(".cmp-carousel__action--next").removeClass("over");

        });   
        autoPlay();    
  
    }


    //this function used to move the items
    function ResCarousel(e, el, s) {
        var leftBtn = ('.cmp-carousel__action--previous');
        var rightBtn = ('.cmp-carousel__action--next');
        var translateXval = '';
        var divStyle = $(itemsDiv).css('transform');
        var values = divStyle.match(/-?[\d\.]+/g);
        var xds = Math.abs(values[4]);
        if (e == 0) {
           
            var last = $('.cmp-carousel__item').last()
            last.prependTo(itemsDiv);
            $('.cmp-carousel__item.active').removeClass('active').prev().addClass('active');
        }
        else if (e == 1) {
           
            var first = $('.cmp-carousel__item').first()
            first.appendTo(itemsDiv);
            $('.cmp-carousel__item.active').removeClass('active').next().addClass('active');
         
        }
      
        $(itemsDiv).css('transform', 'translateX(' + -translateXval + 'px)');  

    }

    function click(ell, ee) {
        var Parent = ('.cmp-carousel');
        var slide = 1;
        ResCarousel(ell, Parent, slide);
    }

    function autoPlay(){
        let timedelay=5000;
          if(checkmode == "edit"){
            timedelay=20000;
          }
         delay= (delay =='') ? timedelay :$(".carousel-multi .cmp-carousel").attr("data-cmp-delay");
        let intervalId=null;
        if(!intervalId){

      intervalId =  window.setInterval(function() {

        if($(".carousel-multi .cmp-carousel__action--play").hasClass('cmp-carousel__action--disabled')){
           
           var first = $('.cmp-carousel__item').first()
            first.appendTo(itemsDiv);
            $('.cmp-carousel__item.active').removeClass('active').next().addClass('active');  
        }

        }, delay);
        }
    }
    $(".carousel-multi .cmp-carousel__action--play").click(function(){
        autoPlay();
    });
    $(".carousel-multi .cmp-carousel__action--pause").click(function(){
        autoPlay();
    });

});
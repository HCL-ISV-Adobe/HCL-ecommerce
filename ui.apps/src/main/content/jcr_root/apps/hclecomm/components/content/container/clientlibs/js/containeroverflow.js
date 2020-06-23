$(function() {
    if(document.querySelectorAll('.cmp-product-tileOverflow').length > 0) {
    
        $('.cmp-product-tileOverflow').children().children().addClass('my-slider');
    
        var slider = tns(
             {
                 container: '.my-slider',
                 autoplay: false,
                 items: 1,
                 nav: false,
                 // edgePadding: 50,
                 swipeAngle: false,
                 arrowKeys: true,
                 loop: false,
                 speed: 400,
                 controls: true,
                 // controlsContainer: "#customize-controls",
                 responsive: {
                     640: {
                         edgePadding: 50,
                         gutter: 20,
                         items: 1
                     },
                     700: {
                         gutter: 10,
                         items: 2
                     },
                     900: {
                         items: 4
                     }
                 }
             }
         );
    }
  });
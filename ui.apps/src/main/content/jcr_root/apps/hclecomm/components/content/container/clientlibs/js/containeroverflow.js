$(function() {

    if(document.querySelectorAll('.cmp-product-tileOverflow').length > 0) {
       console.log("checkmode",checkmode);
       if(checkmode !== "edit"){
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
                     500: {
                         edgePadding: 50,
                         gutter: 20,
                         items: 1
                     },
                     700: {
                         gutter: 10,
                         items: 2
                     },
                     900: {
                         gutter: 5,
                         items: 3
                     },
                     1100: {
                         items: 4
                     }
                 }
             }
         );
       }
    }
  });
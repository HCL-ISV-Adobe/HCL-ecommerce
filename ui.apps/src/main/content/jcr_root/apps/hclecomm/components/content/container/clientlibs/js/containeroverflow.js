$(function() {

    if(document.querySelectorAll('.cmp-product-tileOverflow').length > 0) {
       console.log("checkmode",checkmode);
       if(checkmode !== "edit"){

        const tnsSliders = document.querySelectorAll('.cmp-product-tileOverflow');
        tnsSliders.forEach( (el,k) => {
            console.log('eachkeyitem', el, k);
            el.firstElementChild.firstElementChild.classList.add('my-slider'+k);
            let slider = tns({
                 container: '.my-slider'+k,
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
                     400: {
                         //edgePadding: 50,
                         //gutter: 20,
                         items: 1
                     },
                     600: {
                         //gutter: 10,
                         items: 2
                     },
                     700: {
                         //gutter: 5,
                         items: 3
                     },
                     900: {
                         items: 4
                     }
                 }
             });
        });
       }
    }
  });
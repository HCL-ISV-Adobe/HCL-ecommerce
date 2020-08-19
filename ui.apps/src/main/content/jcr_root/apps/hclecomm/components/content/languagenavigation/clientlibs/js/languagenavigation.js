$(document).ready(function () {
    const selectedLanguage = $(".cmp-languagenavigation__item--active > a").text();
    $(".language-navigation-btn span").text(selectedLanguage);

    $(".language-navigation-btn").click( function (e) {
        $(".language-navigation-btn .fa-drpdwn").hasClass("fa-angle-down") ?
            ($(".language-navigation-btn .fa-drpdwn").addClass("fa-angle-up").removeClass("fa-angle-down")) :
            ($(".language-navigation-btn .fa-drpdwn").addClass("fa-angle-down").removeClass("fa-angle-up"));

        $(".cmp-languagenavigation").toggleClass("show");

        e.stopPropagation();
    });

    //stop propagation for nav layer onclick event

    window.onclick = function(e) {
        if ( !e.target.matches('.language-navigation-btn') ) {
            if($('.cmp-languagenavigation').hasClass('show')) {
				$(".cmp-languagenavigation").removeClass("show");
                $(".language-navigation-btn .fa-drpdwn").addClass("fa-angle-down").removeClass("fa-angle-up");
            }
        }
    }
});	
$(document).ready(function () {
    const selectedLanguage = $(".cmp-languagenavigation__item--active > a").text();
    $(".language-navigation-btn span").text(selectedLanguage);
    $(".language-navigation-btn").click(function () {
        $(".language-navigation-btn .fa").hasClass("fa-angle-down") ?
            ($(".language-navigation-btn .fa").addClass("fa-angle-up").removeClass("fa-angle-down")) :
            ($(".language-navigation-btn .fa").addClass("fa-angle-down").removeClass("fa-angle-up"));
        $(".cmp-languagenavigation").toggleClass("show");
    });
});
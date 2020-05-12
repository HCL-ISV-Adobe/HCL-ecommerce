const toggleNavigation = (menuicon) => {
    menuicon.classList.toggle("menu-icon");
    document.getElementById("cmp-navbar").classList.toggle("toggle-navigation");
}

const toggleMenuIcon = (selectedNavigationITEM, event) => {
    event.stopPropagation()
$(selectedNavigationITEM).children(".cmp-navigation__icon-show").toggleClass("toggle-rotate-icon");
    if($(selectedNavigationITEM).hasClass("toggle-sub-menu")) {
$(selectedNavigationITEM).removeClass("toggle-sub-menu")
}
    else{
$(selectedNavigationITEM).addClass("toggle-sub-menu")
} 
}


$(document).ready(manageNav);
    $(window).on('resize',manageNav);
    function manageNav(){
        if($(window).width()<=1023){
            $('li.cmp-navigation__item').children('ul.cmp-navigation__group').prev().css('pointer-events', 'none');
    $('li.cmp-navigation__item').children('ul.cmp-navigation__group').siblings('span.cmp-navigation__icon').addClass('cmp-navigation__icon-show');
        }
    }

const toggleNavigation = (menuicon) => {
    menuicon.classList.toggle("menu-icon");
    document.getElementById("cmp-navbar").classList.toggle("toggle-navigation");
}


$(document).ready(function () {

		if($(window).width()<=1023){
    	$(".cmp-navigation__item--level-0 a.cmp-navigation__item-link").click(function(e){

   console.log("hi");

    		if($(event.target).closest('li').has('ul.cmp-navigation__group'))
    		{

   console.log("bye");
				e.preventDefault()

			}

		});
	}

});
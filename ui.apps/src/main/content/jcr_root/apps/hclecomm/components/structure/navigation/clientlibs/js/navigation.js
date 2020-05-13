
const toggleNavigation = (menuicon) => {
    menuicon.classList.toggle("menu-icon");
    document.getElementById("cmp-navbar").classList.toggle("toggle-navigation");
}

const toggleMenuIcon = (selectedNavigationITEM, event) => {
    event.stopPropagation()
    $(selectedNavigationITEM).children(".cmp-navigation__icon-show").toggleClass("toggle-rotate-icon");
    if ($(selectedNavigationITEM).hasClass("toggle-sub-menu")) {
        $(selectedNavigationITEM).removeClass("toggle-sub-menu")
    } else {
        $(selectedNavigationITEM).addClass("toggle-sub-menu")
    }
}


$(document).ready(manageNav);
$(window).on('resize', manageNav);

function manageNav() {
    if ($(window).width() <= 1023) {
        $('li.cmp-navigation__item').children('ul.cmp-navigation__group').prev().css('pointer-events', 'none');
        $('li.cmp-navigation__item').children('ul.cmp-navigation__group').siblings('span.cmp-navigation__icon').addClass('cmp-navigation__icon-show');
    }
}

$(document).ready(appendChildToNav);
 $(window).on('resize', appendChildToNav);
function appendChildToNav() {
    const appendChildClass = 'cmp-navigation__item' + ' ' + 'cmp-navigation__item--level-1';
    if ($(window).width() <= 1023) {
        const parentObject = document.getElementsByClassName('cmp-navigation__item--level-0');
        [...parentObject].forEach((parent, i) => {
            if (parent.lastElementChild.classList.contains('cmp-navigation__group')) {
                console.log(parent.lastElementChild)
                const copyParent = parent.children[1].cloneNode(true);
                copyParent.removeAttribute('style');
                let parentLi = document.createElement("li");
                if(!parent.lastElementChild.firstElementChild.classList.contains('hide--desktop')){
                parentLi.className = 'cmp-navigation__item cmp-navigation__item--level-1 hide--desktop';
                parentLi.append(copyParent)
                parent.lastElementChild.prepend(parentLi);
                appendChildOnce = true;
            }
            }
        });

    }


    }
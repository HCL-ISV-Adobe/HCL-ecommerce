let whishlistLink = ' ';
let signinLink = ' ';
$(document).ready(function () {
wishlistLink = $('.wish-list-navigation-item').attr('href');
   $(".wish-list-navigation-item").removeAttr("href");
    signinLink = $('.wish-list-navigation-item').attr('loginUrl');

})
function onNavigateToWishlist(){
     let userData = getUserCookie("hcluser");
  if (userData != "") {
      window.location.href = wishlistLink;
  }
    else {
        window.location.href = `${signinLink}?referer=${wishlistLink}`;
    }
}

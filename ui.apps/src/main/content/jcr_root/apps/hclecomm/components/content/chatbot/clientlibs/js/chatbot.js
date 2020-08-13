$(window).scroll(function() {
    var height = $(window).scrollTop();
    if (height > 500) {
        $(".chatbtn").css({"bottom": "90px"});
    } else {
        $(".chatbtn").css({"bottom": "10px"});
    }
});

$(document).ready(function() {
var botid = $('#botid').data('custom-property');
var hostPath = $('#hostPath').data('custom-property');
    var botTitle = $('#botTitle').data('custom-property');
    var botAvatar = $('#botAvatar').data('custom-property');
 window.botpressWebChat.init({
     host:hostPath,
        botId:botid,
          hideWidget: true,
          showConversationsButton:false,
          botConvoTitle:botTitle ,
          //botAvatarUrl:botAvatar
});
$("#show-bp").on("click", function(){

window.botpressWebChat.sendEvent({ type: 'show' })
});
});
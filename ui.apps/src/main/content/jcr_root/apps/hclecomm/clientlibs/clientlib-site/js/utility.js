const setUserCookie = function (cname, cvalue, exdays) {
	let d = new Date();
	d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	const expires = "expires=" + d.toGMTString();
	document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}



const getUserCookie = function (cname) {
	let name = cname + "=";
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');
	for (let i = 0; i < ca.length; i++) {
		let c = ca[i].trim();
		if (c.indexOf(name) === 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

const checkUserCookie = function(cname) {
if(getUserCookie("hcluser") != "") {
return true;
} else {
return false;
}
}


var oldXHR = window.XMLHttpRequest;
function newXHR() {
    var checkmode =$('.checkmode').attr('checkmode');
    var realXHR = new oldXHR();
    if(checkmode !== 'edit'){
        realXHR.addEventListener("readystatechange", function() {
            if(realXHR.readyState==1){
                loader(true);
            }
            if(realXHR.readyState==4){
                loader(false);           
            }
        }, false);
    }
    return realXHR;
}
window.XMLHttpRequest = newXHR;
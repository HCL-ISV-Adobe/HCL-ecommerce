
function loader(flag){
    let body=document.getElementsByTagName("body")[0];
    
    if(flag===true){
        //body.style.overflow="hidden";
        body.insertAdjacentHTML('beforeend', `<div class="overlay"><div class="loader"></div></div>`);
    }else{
        body.style.removeProperty("overflow");
        let overlay= document.querySelector(".overlay");
        body.removeChild(overlay);
    }
}

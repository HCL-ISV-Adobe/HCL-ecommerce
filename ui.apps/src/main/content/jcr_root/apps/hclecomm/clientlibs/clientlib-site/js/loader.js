function loader(flag){
    let body=document.querySelector("body");
    
    if(flag===true){
        body.style.overflow="hidden";
        body.innerHTML+=`<div class="overlay" style="height:${window.innerHeight}px"><div class="loader"></div></div>`
    }else{
        body.style.removeProperty("overflow");
        let overlay= document.querySelector(".overlay");
        body.removeChild(overlay);
    }
}
/*setTimeout(function(){
    loader(false)
},5000);
loader(true)
*/

(function () {
	if(document.querySelectorAll(".cmp-product-filterTile").length>0){
  let filterHead = document.querySelectorAll(
    ".productfilter .cmp-productFilter-container .cmp-productFilter-heading"
  );
  let filterTxt = document.querySelectorAll(
    ".productfilter .cmp-productFilter-container ul li"
  );
  let filter = document.querySelector(
    ".productfilter .cmp-productFilter-mobile .cmp-productFilter-open span:nth-child(2)"
  );

  let dataAttr = document.querySelectorAll(".product-listing-tile");

    let domHide=document.querySelectorAll(".cmp-product-filterTile .tile");


document.querySelector(".cmp-productFilter .cmp-productFlter-clear").addEventListener("click", clearFilter);



  filterHead.forEach(function (item) {
    item.addEventListener("click", function () {
      categoriesHandle(event);
    });
  });

  filterTxt.forEach(function (item) {
    item.addEventListener("click", function () {
      filterCategories(event);
    });
  });

  filter.addEventListener("click", showFilter);
  document
    .querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-close span:nth-child(2)"
    )
    .addEventListener("click", hideFilter);

  document
    .querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-close span:nth-child(1)"
    )
    .addEventListener("click", clearFilter);

  function categoriesHandle(event) {
    let panel = event.target.nextElementSibling;
	console.log("panel ",panel);
    if (panel.style.display == "block") {
      panel.style.display = "none";
    } else {
      panel.style.display = "block";
    }
  }

  function filterCategories(event) {


      let productFilter=event.target.innerHTML;
	  for(let b=0;b<filterTxt.length;b++){
		  filterTxt[b].style.fontWeight="100";
	  }
	  event.target.style.fontWeight="bold";

        domHide.forEach(function (item) {
          item.style.display = "none";
        });

      for (let i = 0; i < dataAttr.length; i++) {
        if (dataAttr[i].getAttribute("data-tag").includes(productFilter)) {
          dataAttr[i].parentElement.style.display = "block";

        }
      }

  }

  function showFilter() {
    document.querySelector(".productfilter .cmp-productFilter").style.display =
      "block";
    document.querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-close"
    ).style.display = "flex";
    document.querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-open"
    ).style.display = "none";
  }

  function hideFilter() {
    document.querySelector(".productfilter .cmp-productFilter").style.display =
      "none";
    document.querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-close"
    ).style.display = "none";
    document.querySelector(
      ".productfilter .cmp-productFilter-mobile .cmp-productFilter-open"
    ).style.display = "flex";
  }

  function clearFilter() {
    
    domHide.forEach(function (item) {
      item.style.display = "block";
    });

      for(let b=0;b<filterTxt.length;b++){
		  filterTxt[b].removeAttribute("style");
	  }
  }
  }
  
  window.onscroll=function(){
	if(document.querySelectorAll(".cmp-productFilter").length>0){
		if(window.pageYOffset> 50){
			document.querySelector(".cmp-productFilter").style.top ="0px";
		}else{
			document.querySelector(".cmp-productFilter").style.top ="50px";
		}
	}
}
})();



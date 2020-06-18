document.querySelector(".search i.cmp-search__icon").addEventListener("click", headerSearch);

 function headerSearch() {
    let panel = document.querySelector(".search .cmp-search__input");
    if (panel.style.display == "block") {
      panel.style.display = "none";
    } else {
      panel.style.display = "block";
    }
  }
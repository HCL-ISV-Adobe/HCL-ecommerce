(function () {
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

  let productFilter = [],
    filterBreak = true;

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
    if (panel.style.display === "block") {
      panel.style.display = "none";
    } else {
      panel.style.display = "block";
    }
  }

  function filterCategories(event) {
    if (productFilter.length == 0) {
      productFilter.push(event.target.innerHTML);
    } else {
      if (!productFilter.includes(event.target.innerHTML)) {
        productFilter.push(event.target.innerHTML);
      }
    }

    productFilter.forEach(function (value) {
      if (filterBreak) {
        dataAttr.forEach(function (item) {
          item.style.display = "none";
        });
        filterBreak = false;
      }
      for (let i = 0; i < dataAttr.length; i++) {
        if (dataAttr[i].getAttribute("data-tag").includes(value)) {
          dataAttr[i].style.display = "block";
        }
      }
    });
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
    productFilter = [];
    filterBreak = true;
    dataAttr.forEach(function (item) {
      item.style.display = "block";
    });
  }
})();

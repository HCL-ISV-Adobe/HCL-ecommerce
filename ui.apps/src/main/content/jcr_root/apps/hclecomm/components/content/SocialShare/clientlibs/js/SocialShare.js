let productName = $(".product-details-cmp__prduct-name").text();
    let swfImageMailTo = document.getElementById('swfImageMailTo');
    let swfMailTo = document.getElementById('swfMailTo');
 swfMailTo.href = swfImageMailTo.href = "mailto:?"
 + "&subject=" + encodeURIComponent("I want to recommend this " + productName)
 + "&body=" + encodeURIComponent("I want to recommend this product at HCL.com " + productName + "Learn more: " + window.location.href);

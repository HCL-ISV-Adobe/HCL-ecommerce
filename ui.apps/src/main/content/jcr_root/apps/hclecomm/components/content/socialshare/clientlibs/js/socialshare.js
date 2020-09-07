let productName = $(".product-details-cmp__prduct-name").text();
let swfImageMailTo = document.getElementById('swfImageMailTo');
if(swfImageMailTo){
swfImageMailTo.href = "mailto:?"
 + "&subject=" + encodeURIComponent("I want to recommend this " + productName)
 + "&body=" + encodeURIComponent("I want to recommend this product at HCL.com " + productName +"." + "\nLearn more: " + window.location.href);
}
$(document).ready(function () {
  debugger
          const xhttp = new XMLHttpRequest();
          xhttp.onreadystatechange = function () {
              if (this.readyState == 4 && this.status == 200) {
         console.log(this.responseText)
              }
          };
          xhttp.open("GET", "/bin/hclecomm/productDetails", true);
          xhttp.send();
  
  
  });
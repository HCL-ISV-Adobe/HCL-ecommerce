const onClickofCmpre  = () =>{
    const skuObj = {sku1 : selectedItems[0].id, sku2 : selectedItems[1].id} ;
         const productCompareContainer = $('.compare-product-pop-up');
        const xhr = new XMLHttpRequest();
           const  url = "/bin/hclecomm/addToCompare?";
              xhr.open("POST", url, true);
               xhr.setRequestHeader("Content-Type", "application/json");
               //xhr.setRequestHeader("CustomerToken", custToken);
               xhr.onreadystatechange = function() {
                        if (xhr.readyState === 4 && xhr.status === 200) {
                             const productDetails = JSON.parse(this.responseText);
                       const productCompareContainer = $('.compare-product-list');
          const productPopUpCompareContainer = $('.compare-product-pop-up');
          if(productDetails && productDetails.length > 0) {
			  const header = `<div class = 'compare-product-header'>Compare Products <i onclick ="closeCompareProductPopup()" class="fa fa-window-close" aria-hidden="true"></i></div>`
              const footer = `<div class = 'compare-product-footer'><button onclick ="closeCompareProductPopup()" class = 'compare-footer-btn'>Ok</button></div>`
              const staticList = `<div class ='compare-product-details'><div></div><p><span>Product Name</span></p> <p><span>Price</span></p><p><span>Product Sku</span></p><p><span></span></p></div>`
              productPopUpCompareContainer.css('display', 'block');
              const productDetailsItem = productDetails.map((productDetailsItem , index) =>{
                  return (
                                                     `<div class ='compare-product-details'>
                                                         <div><img class = 'cmp-product-img' src = ${selectedItems[index].productimgurl}></div>
                                                         <p><span>${productDetailsItem.name}</span></p>
                                                         <p><span>${productDetailsItem.price}</span></p>
                                                         <p><span>${productDetailsItem.sku}</span></p>
							 <p><a class = 'compare-product-continue' href = ${selectedItems[index].producthref}> Continue </a></p>

                                                     </div>`
                                                 )
                                              }).join('');
                      const conatiner = `${header} <div class = 'product-detail-table'> ${staticList}   ${productDetailsItem} </div> ${footer}`;
                  if( productCompareContainer && productDetailsItem){
                      productCompareContainer[0].innerHTML = (conatiner);
                      }
    }
                        }
               }
               xhr.send(JSON.stringify(skuObj));
    }
    const closeCompareProductPopup = () =>{
        const productPopUpCompareContainer = $('.compare-product-pop-up');
        productPopUpCompareContainer.css('display', 'none');
    }

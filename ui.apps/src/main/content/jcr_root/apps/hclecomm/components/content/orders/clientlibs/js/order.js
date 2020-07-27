$( document ).ready(function() {
    const orderListContainer = $('.cmp-my-order');
	let userData = getUserCookie("hcluser");
    let custEmail = '';
    if (userData != "") {

		custEmail = JSON.parse(userData).email;
        var payload = {"custEmail" : custEmail};
        const xhttp = new XMLHttpRequest();
       xhttp.onreadystatechange = function() {
       if (this.readyState == 4 && this.status == 200) {

         const ordersListResponse =  JSON.parse(this.responseText); 
                if(ordersListResponse['items'] && ordersListResponse['items'].length> 0){
					const ordersElmen = ordersListResponse['items'].map((orderArrayItem) =>{
										const created_date = orderArrayItem.created_at.split(" ")[0];

									  let lowerItems = null
                                        if(orderArrayItem['items'] && orderArrayItem['items'].length> 0){
                                            lowerItems  = orderArrayItem['items'].map((orderItem) =>{
                                                return(
                                        				`<div class ='cmp-orders-list--lower-container'>
															<div >
                                                                <p class="cmp-product-name"><b> ${orderItem.name}</b></p>
                                                                <p><span>${orderItem.sku}</span></p>
                                                                <p>Qty:<span>${orderItem.qty_ordered}</span></p>
                                                            </div>
                                                            <p><span> $</span><span>${orderItem.row_total}</span></p>
                                                            <p><span>${orderArrayItem.status}</span></p>
                                                        </div>`
                                            	)
                                                
                                            })

                                        } 

                                             return (
                                                    `<div class ='cmp-orders-list'>

                                                        <div class ='cmp-orders-list--upper-container'>

                                                                <p><b>Order Id :  ${orderArrayItem.increment_id}</b></p>
                                                                <p>Created on : <span>${created_date}</span></p>
                                                                <p><span>${orderArrayItem.status}</span></p>

                                                        </div>
                                                        ${lowerItems.join(" ")}

                                                    </div> `
                                             )
											
                                       })	
									 if(orderListContainer[0]) {
                                           orderListContainer[0].innerHTML = ordersElmen.join(" "); }
                                       
				}
                else
                {
					$('.cmp-my-order').addClass('hide-cmp-order');
					$('.cmp-no-order').removeClass('hide-cmp-order')
                }
    	}
     };
    	 xhttp.open("POST","/bin/hclecomm/getOrders", true);
         xhttp.send(JSON.stringify(payload));


    }

});

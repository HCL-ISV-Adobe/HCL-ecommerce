const selectedItems = [];
const ImageandProductUrl = [];
$( document ).ready(function() {
    $( "input.compare-checkbox").prop('checked',false); 
     $(".cmp-product-filterTile input.compare-checkbox").parent().removeClass("compare-checkbox--hide");
})
const onAddToCompare = (event,ele) =>{

  const id = $(ele).parent().parent().attr('data-productSku');
  const producthref = $(ele).parent().prev().children().children().attr('href') ;
  const productimgurl = $(ele).parent().parent().children().children().children().children().attr('src') ;
    const temObj = {
	 id ,producthref ,productimgurl
}
 if(event.target.checked){
            selectedItems.push(temObj);

            //element.removeAttr( "compare-checkbox" );
        }
        else {
            const findInex = selectedItems.findIndex((selectedItem) => selectedItem.id === id);
            if(findInex > -1){
                        selectedItems.splice(findInex, 1);
                        //element.addAttr( 'name', "compare-checkbox" );
            }
        }
		console.log(selectedItems);
        if(selectedItems && (selectedItems.length  <= 2)) {
            $( "input.compare-checkbox:not(:checked) " ).attr("disabled", false); 

        if(selectedItems && (selectedItems.length  < 2)) {
                $('.compare-to-cmp').css('display', 'none');
        }

        if(selectedItems && (selectedItems.length  == 2)) {
                $( "input.compare-checkbox:not(:checked) " ).attr("disabled", true);
             $('.compare-to-cmp').css('display', 'inline-block');
        }
        }

}

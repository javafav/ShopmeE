$(document).ready(function(){
	$("#productList").on("click" ,".linkRemove",function(e){
		
		e.preventDefault();
		if(doesOrderHaveOnlyOneProduct()){
			showWarningModal("Could not remove product. The order must have one product. ")
		}else {
			removeProduct($(this));
			updateOrderAmounts();
		}

		
		
		
	});
});



function removeProduct(link){
	
	rowNumber = link.attr("rowNumber");
	$("#row" + rowNumber).remove();
    $("#blankLine" +rowNumber ).remove();
	$(".divCount").each(function(index , element){
		element.innerHTML = "" + (index + 1);
	})
}

function doesOrderHaveOnlyOneProduct(){
		produtCount = $(".hiddenProductId").length ;
		return produtCount == 1;

}
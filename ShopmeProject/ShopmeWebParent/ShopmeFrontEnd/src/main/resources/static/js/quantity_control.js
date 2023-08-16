$(document).ready(function(){
	$(".linkMinus").on("click" ,function(evt){
		evt.preventDefault();
	productId = $(this).attr("pid");
	qunatityInput = $("#quantity" + productId)
	newQuantity = parseInt(qunatityInput.val()) - 1;
	if(newQuantity > 0){
		qunatityInput.val(newQuantity)
	}else{
	showWarningModal("Minmum Quantity is 1")
	}
	});
	
	$(".linkPlus").on("click" ,function(evt){
		evt.preventDefault();
	productId = $(this).attr("pid");
	qunatityInput = $("#quantity" + productId)
	newQuantity = parseInt(qunatityInput.val()) + 1;
	if(newQuantity < 6){
		qunatityInput.val(newQuantity)
	}else{
	showWarningModal("Maximum Quantity is 5")
	}
	});
	
	});
	

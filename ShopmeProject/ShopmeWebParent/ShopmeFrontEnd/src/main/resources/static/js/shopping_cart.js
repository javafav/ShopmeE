decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';

$(document).ready(function(){
		
	$(".linkMinus").on("click" ,function(evt){
		evt.preventDefault();
	     decreseQuantiy($(this));
	   
	});
	
	$(".linkPlus").on("click" ,function(evt){
		evt.preventDefault();
	    increseQuantity($(this));
	    
	});
		$(".linkReomve").on("click" ,function(evt){
		evt.preventDefault();
	  url =   $(this).attr("href");
removeProduct($(this));

	});
		

	});
	
	
	
	function removeProduct (link){
		 url =link.attr("href");
       
		 
		$.ajax({
			type: "GET",
			url: url,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			}
		}).done(function(response) {


			rowNumber = link.attr("rowNumber")
			removeProductHTML(rowNumber);
			updateTotal();
			showModalDialog("Shopping Cart", response);
			updateCountNumber();
			totalCartItem();
		}).fail(function() {
			showErrorModal("Error while  remoivng product");
		});
		
	}
	
	function updateCountNumber(){
		
	$(".divCount").each(function(index ,element){
		
		element.innerHTML = "" + (index+1);
	});
	}
	
	function removeProductHTML(rowNumber){
		$("#row" +rowNumber).remove();
		$("#blankLine" +rowNumber).remove();

	}
	function decreseQuantiy(link){
	productId = link.attr("pid");
	qunatityInput = $("#quantity" + productId)
	newQuantity = parseInt(qunatityInput.val()) - 1;
	if(newQuantity > 0){
		qunatityInput.val(newQuantity)
		updateQuantity(productId , newQuantity);
    totalCartItem();
	}else{
	showWarningModal("Minmum Quantity is 1")
	}
	}
	
	function increseQuantity(link){
		productId = link.attr("pid");
	qunatityInput = $("#quantity" + productId)
	newQuantity = parseInt(qunatityInput.val()) + 1;
	if(newQuantity < 6){
		qunatityInput.val(newQuantity)
		updateQuantity(productId , newQuantity);
		totalCartItem();
	}else{
	showWarningModal("Maximum Quantity is 5")
	}
	}
	
	function updateQuantity(productId , quantity){
		
		 url = contextPath + "cart/update/" +  productId + "/" + quantity;

		
		$.ajax({
			type: "POST",
			url: url,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			}
		}).done(function(updatedSubtoal) {

			updateSubtotal(updatedSubtoal , productId);
		    updateTotal();
		
		}).fail(function() {
			showErrorModal("Error while  updated product quantity")
		});
	}
	
	
	
	function updateSubtotal(updatedSubtoal , productId){

		$("#subtotal" + productId).text(formatCurrency(updatedSubtoal));
	}
	
	function updateTotal(){
		
		var total = 0.0;
		prodcutCount = 0;
		$(".subtotal").each(function(index , element){
			total += parseFloat(clearCurrencyFormat(element.innerHTML));
			prodcutCount++;
		});
		if(prodcutCount <  1){
			showEmptyShoppintCart();
		}else{

		$("#total").text(formatCurrency(total));
		}
	
		
	}

function showEmptyShoppintCart(){
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
	
}

function formatCurrency(amount){
	return ($.number(amount ,decimalDigits , decimalSeparator , thousandsSeparator));
}
function clearCurrencyFormat(numberString){
	result = numberString.replaceAll(thousandsSeparator , "");
	return result.replaceAll(decimalSeparator ,".");
}

function totalCartItem(){
	
	  url = contextPath + "cart/totalItems";

		
		$.ajax({
			type: "GET",
			url: url,
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeaderName, csrfValue);
			}
		}).done(function(response) {

		$("#cartItems").text(response);
	
		
		}).fail(function() {
			showErrorModal("Error while  updateding cart items")
		});
}

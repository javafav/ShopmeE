var cartItem ;


$(document).ready(function(){
	cartItem = $("#cartItems");
	
	$("#buttonAdd2Cart").on("click" , function(evt){
		
		evt.preventDefault();
	 
		addToCart();
		
		
		
	});
	
	
});

function addToCart(){
	quantity = $("#quantity" + productId).val();
	 url = contextPath + "cart/add/" +  productId + "/" + quantity;

   
$.ajax({
	type:"POST" ,
	url:url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
}).done(function(response){
	
	showModalDialog("Shopping Cart" , response);
	 totalCartItem();


}).fail(function(){
	showErrorModal("Error while product adding to shopping cart")
});
}

function totalCartItem() {

	url = contextPath + "cart/totalItems";


	$.ajax({
		type: "GET",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		if(response == 'null'){
		cartItem.text("");
 }else{
	 cartItem.text(response);
 }

	}).fail(function() {
		showErrorModal("Error while  updateding cart items");
	});
}
var fieldProductCost;
var fieldSubtotal;
var fieldShippingCost;
var fieldTax;
var fieldTotal;


$(document).ready(function() {

	fieldProductCost = $("#productCost");
	fieldSubtotal = $("#subtotal");
	fieldShippingCost = $("#shippingCost");
	fieldTax = $("#tax");
	fieldTotal = $("#total");
	formatOrderAmount();
	formatProductAmount();
	$("#productList").on("change", ".quantity-input", function() {

		updateSubtotalWhenQuantityChanged($(this));
		updateOrderAmounts();

	});
	$("#productList").on("change", ".price-input", function() {

		updateSubtotalWhenPriceChanged($(this));
		updateOrderAmounts();

	});

	$("#productList").on("change", ".cost-input", function() {


		updateOrderAmounts();


	});

	$("#productList").on("change", ".ship-input", function() {


		updateOrderAmounts();


	});




});


function updateOrderAmounts() {
	totalCost = 0.0;
	$(".cost-input").each(function(e) {
		costInputField = $(this);
		rowNumber = costInputField.attr("rowNumber");
		quantityValue = $("#quantity" + rowNumber).val();

		productCost = getNumberValueRemovedThousandSepartor(costInputField);
		totalCost += productCost * parseInt(quantityValue);

	});;
	setAndFormatNumberForField("productCost", totalCost);

	orderSubtotal = 0.0;

	$(".subtotal-output").each(function(e) {
		prodductSubtoal = getNumberValueRemovedThousandSepartor($(this));
		orderSubtotal += prodductSubtoal;


	});
	setAndFormatNumberForField("subtotal", orderSubtotal);

	shippingCost = 0.0;

	$(".ship-input").each(function(e) {
		productShip = getNumberValueRemovedThousandSepartor($(this));

		shippingCost += productShip;

	});
	setAndFormatNumberForField("shippingCost", shippingCost);

	tax = getNumberValueRemovedThousandSepartor(fieldTax);
	orderTotal = orderSubtotal + tax + shippingCost;
	setAndFormatNumberForField("total", orderTotal);

}


function setAndFormatNumberForField(fieldId, fieldValue) {
	formattedValue = $.number(fieldValue, 2);
	$("#" + fieldId).val(formattedValue);

}

function getNumberValueRemovedThousandSepartor(fieldRef) {
	fieldValue = fieldRef.val().replace(",", "");
	return parseFloat(fieldValue);
}


function updateSubtotalWhenPriceChanged(input) {
	rowNumber = input.attr("rowNumber");
	quantityVal = $("#quantity" + rowNumber).val();

	priceField = $("#price" + rowNumber)
	priceValue = getNumberValueRemovedThousandSepartor(input);
	newSubtotal = parseFloat(quantityVal) * priceValue;

	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);
	//subtoal.val($.number(newSubtotal, 2));
}






function updateSubtotalWhenQuantityChanged(input) {

	quantityVal = input.val();
	rowNumber = input.attr("rowNumber");

	priceValue = getNumberValueRemovedThousandSepartor($("#price" + rowNumber));
	newSubtotal = parseFloat(quantityVal) * priceValue;

	setAndFormatNumberForField("subtotal" + rowNumber, newSubtotal);



}

function formatProductAmount() {
	$(".cost-input").each(function(e) {

		foramtNumberForField($(this));
	});
	$(".price-input").each(function(e) {

		foramtNumberForField($(this));
	});
	$(".subtotal-output").each(function(e) {

		foramtNumberForField($(this));
	});
	$(".ship-input").each(function(e) {

		foramtNumberForField($(this));
	});

}



function formatOrderAmount() {
	foramtNumberForField(fieldProductCost);
	foramtNumberForField(fieldSubtotal);
	foramtNumberForField(fieldShippingCost);
	foramtNumberForField(fieldTax);
	foramtNumberForField(fieldTotal);



}

function foramtNumberForField(fieldRef) {


	fieldRef.val($.number(fieldRef.val(), 2));
}


function processFormBeforeSubmit(){
   setCountryName();
	removeThousandSepartorForField(fieldProductCost);
	removeThousandSepartorForField(fieldShippingCost);
    removeThousandSepartorForField(fieldTax);
    removeThousandSepartorForField(fieldSubtotal);
    removeThousandSepartorForField(fieldTotal);
   
   $(".cost-input").each(function(e){
	   
	   removeThousandSepartorForField($(this));
   });
    $(".price-input").each(function(e){
	   
	   removeThousandSepartorForField($(this));
   });
	  $(".subtotal-output").each(function(e){
	   
	   removeThousandSepartorForField($(this));
   });
   
     $(".ship-input").each(function(e){
	   
	   removeThousandSepartorForField($(this));
   });
}
function removeThousandSepartorForField(fieldRef){
	fieldRef.val(fieldRef.val().replace(",",""));
	
}

function setCountryName(){
selectedCountry = $("#country option:selected");
countryName = selectedCountry.text();
$("#countryName").val(countryName);
}


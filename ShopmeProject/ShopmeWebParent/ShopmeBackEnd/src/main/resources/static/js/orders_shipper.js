var iconNames = {
	'PICKED'    : 'fa-people-carry',
	'SHIPPING'  : 'fa-shipping-fast',
	'DELIVERED' : 'fa-box-open',
	'RETURNED'  : 'fa-undo'
	
};

var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;
$(document).ready(function() {

	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);

		showUpdateConfirmModal(link);

	});
	addEventHandlerForYEsButton();
});
function addEventHandlerForYEsButton() {
yesButton.click(function(e){
	e.preventDefault();
	sendRequestToUpdateOrderStatus($(this));
	
});

}
function sendRequestToUpdateOrderStatus(button){
	requestURL = button.attr("href");
		$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		
	
	}).done(function(response){
	showMessageModal("Order status updated successfuly.");
	console.log(response);
	updateStatusIconColor(response.orderId , response.status);
	}).fail(function(){
   showMessageModal("Error ,unable to update order status.");
	
	})
	
}
function updateStatusIconColor(orderId, status){
	link = $("#link"+ status + orderId);
    link.replaceWith("<i class= 'fa " + iconNames[status] + " fa-2x icon-green'></i>");
	
}
function showUpdateConfirmModal(link) {
	noButton.text("No");
	yesButton.show();
	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href" , link.attr("href"));
	confirmText.text("Are you sure you want to update the status of the order ID #" + orderId +
		" to " + status + "?");
	confirmModalDialog.modal();
	

}
function showMessageModal(message){
	
	noButton.text("Close");
	yesButton.hide();
	confirmText.text(message);
	
}

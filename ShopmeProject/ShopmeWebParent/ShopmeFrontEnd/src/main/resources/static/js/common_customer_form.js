var dropDownCountry;
var dataListState;
var fieldState;

$(document).ready(function() {
	dropDownCountry = $("#country");
	dataListState = $("#listStates");
	ieldState = $("#state");

	dropDownCountry.on("change", function() {
		loadStatesForCounry();
	})
});

function loadStatesForCounry() {
	selectedCounry = $("#country option:selected")
	countryId = selectedCounry.val();

	url = contextPath + "settings/list_states_by_country/" + countryId;

	$.get(url, function(responseJSOn) {
		dataListState.empty();

		$.each(responseJSOn, function(index, state) {
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);
			$("#state").val("").focus();
		});
	});
}


function checkPasswordMatch(confirmPassword) {

	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Passwords do not match!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}



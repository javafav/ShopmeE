var dropdownCountries;
	var dropdownStates;

$(document).ready(function() {
		
			$("#buttonCancel").on('click', function() {
			
			window.location = moduleURL ;
					
				});
		
		dropdownCountries = $("#country");
		dropdownStates = $("#listStates");
	
		dropdownCountries.on("change", function() {
			loadStates4Country();
			$("#state").val("").focus();
		});	
		
		loadStates4Country();
		
		
		
		$("#fileImage").change(function() {
			if(!checkFileSize(this))
			{
				
				return
			}
		showImageThumbnail(this);
		});
	});
	
	function checkFileSize(fileInput){
		fileSize = fileInput.files[0].size;
		if (fileSize > MAX_FILE_SIZE)
			 {
			 let uploadLimt = (MAX_FILE_SIZE /100000);
			 uploadLimt = parseInt(uploadLimt);
				fileInput.setCustomValidity("You must choose an image less than" + uploadLimt + "MB!");
				fileInput.reportValidity();
				return false
			} else {
				fileInput.setCustomValidity("");
			
				return true			
			}
		
	}
	
	
	
	
	
	function showImageThumbnail(fileInput) {
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbnail").attr("src", e.target.result);
		};
		
		reader.readAsDataURL(file);
	}
	
function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();

}

function showErrorModal(message) {

	showModalDialog("Error", message)

}
function showWarningModal(message) {

	showModalDialog("Warning", message)

}

	
	
	$(document).ready(function() {
			
	});
	
	function loadStates4Country() {
		selectedCountry = $("#country option:selected");
		countryId = selectedCountry.val();
		url =   contextPath + "states/list_by_country/" + countryId
		
		
		$.get(url, function(responseJson) {
			dropdownStates.empty();
			
			$.each(responseJson, function(index, state) {
				$("<option>").val(state.name).text(state.name).appendTo(dropdownStates);
			});
		}).fail(function() {
			showErrorModal("Error loading states/provinces for the selected country.");
		})	
	}
	
	function cancelButton(){
		
		window.location = moduleURL ;
		
	}
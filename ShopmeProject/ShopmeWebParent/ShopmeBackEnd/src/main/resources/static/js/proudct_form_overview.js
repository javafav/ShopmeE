
dropdownBrands = $("#brand");
dropdownCategory =$("#category");



$(document).ready(function(){
	
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	dropdownBrands.change(function(){
		dropdownCategory.empty();
		getCategories();
		
	});	
	
	getCategoriesNewFrom();
	

	
});
	function getCategoriesNewFrom(){
	catIdField = $("#categoryId");
	editMode = false ;
		if(catIdField.length){
			editMode = true;
		}
	if(!editMode) getCategories;
	
	}

	
	
	function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	
csrfValue = $("input[name='_csrf']").val();

url = moduleURL +"/check_unique";
params = {id:productId , name:productName , _csrf :csrfValue};


$.post(url, params, function(response){
	if(response=="OK"){
	form.submit();	
	}else if(response=="DuplicateName"){
		showWarningModal("There is another prodcut having same name : "	+ productName);
	}else {
		showErrorModal("Unknown Error Occcured");
		
	}
	
	
}).fail(function(){
	showErrorModal("Could not connect to the Server");
})
;
return false
}
	
	
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
	
	
	
	function getCategories(){
		brandId = dropdownBrands.val();
		url =  moduleBrandURL +"/" + brandId + '/categories' ;
		 
		$.get(url ,function(responseJson){
			
		$.each(responseJson ,function(index ,category){
			
		$("<option>").val(category.id).text(category.name).appendTo(dropdownCategory);
			
		});
			
			
		});
		 
		 
	}


		
	

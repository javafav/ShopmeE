package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

	@Autowired private ProductService service;
	
	@PostMapping("/products/check_unique")
	public String checkUniqueCategory( Integer id , @RequestParam(name="name")  String name 
			 ) {
		
		
		return service.checkUnique(id, name);
	
	}
	
	
}

package com.shopme.admin.brand;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;



@RestController
public class BrandRestController {

	@Autowired
	private BrandService service;
		
		
		@PostMapping("/brands/check_unique")
		public String checkUniqueCategory( Integer id , @RequestParam(name="name")  String name 
				 ) {
			
			
			return service.checkUnique(id, name);
		
		}
		
		
        @GetMapping("/brands/{id}/categories")
public List<CategoryDTO> listCategories(@PathVariable("id") Integer brandId) throws BrandNotFoundRestException{
        	
        	List<CategoryDTO>  listCategories = new ArrayList<>();
        	
        	try {
				Brand brand = service.getBrandById(brandId);
				Set<Category> categories = brand.getCategories();
				for(Category category : categories) {
					
					CategoryDTO categoryDTO = new  CategoryDTO(category.getId(), category.getName());
							listCategories.add(categoryDTO);
					
				}
				return listCategories;
			} catch (NoSuchElementException ex) {
				
			throw new BrandNotFoundRestException();
			}
        	
        }



}





package com.shopme.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

@Service
public class CategoryService {

	@Autowired private CategoryRepository repo;
	
	
	public List<Category>  listNoChildrenCateogries(){
		
		List<Category>  listNoChildrenCateogries = new ArrayList<>();
		
		List<Category> listEnabledCategories = repo.finAllEnabled();
		
	listEnabledCategories.forEach(category -> {
		
	Set<Category> children = category.getChildren();
	
	 if(children == null || children.isEmpty()) {
		 
		    listNoChildrenCateogries.add(category);
	 }
		
	});
	
	return listNoChildrenCateogries;
		
		}
		
		
		
	public Category getCategory(String alias) throws CategoryNotFoundException {
		
		Category category = repo.findByAliasEnabled(alias);
		if(category == null ) {
			throw new CategoryNotFoundException("Could not find any category with alias  " +  alias);
		}
		return category;
		
	}
	
	
	
	public List<Category> getCategoryParnet(Category child){
		
		List<Category> listParents = new ArrayList<>();
		Category parent = child.getParent();
		
		while(parent != null) {
			listParents.add(0 , parent);
			parent = parent.getParent();
		}
		
		listParents.add(child);
	
		return listParents;
		
	}
	
		 
	}
	
	


package com.shopme.category;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class CategoryRepositoryTest {

	@Autowired private CategoryRepository repo;
	
	
	@Test
	public void  testListAllEnabled() {
		List<Category> listCategory = repo.finAllEnabled();
		listCategory.forEach(cat ->{
			System.out.println(cat.getName() + "( " + cat.isEnabled() + " )");
			
		});
		
	}
	
	@Test
	public void  listNoChildrenCateogries(){
		
		List<Category>  listNoChildrenCateogries = new ArrayList<>();
		
		List<Category> listEnabledCategories = repo.finAllEnabled();
		
		for(Category children : listEnabledCategories) {
		if(!children.isHasChildren()) {
			
			listNoChildrenCateogries.add(children);
		}
		
		
		}
		
		listNoChildrenCateogries.forEach(cat ->{
			System.out.println(cat.getName() );
			
		});
		 
	}
	
	
	@Test
	public void testFindByAliasAndEabled() {
		String alias = "electronics";
	
	
		Category category = repo.findByAliasEnabled(alias);
		System.out.println(category.getName());
		assertThat(category).isNotNull();
	}
	
	
}

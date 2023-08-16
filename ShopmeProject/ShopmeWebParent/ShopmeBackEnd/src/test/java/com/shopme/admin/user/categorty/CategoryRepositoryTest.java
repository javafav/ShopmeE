package com.shopme.admin.user.categorty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;

import ch.qos.logback.core.status.StatusListenerAsList;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository repo;


@Test
public void testCreateRootCategory() {
	Category category = new Category("Electronics");
	repo.save(category);
	assertThat(category.getId()).isGreaterThan(0);
	
}
@Test
public void testCreateChildCategory() {
	Category parent = new Category(10);
	
	Category gLaptop =  new Category("HP Gaming Laptop",parent);
	repo.save(gLaptop);
	//assertThat(savedCategory .getId()).isGreaterThan(0);
	
	
}

@Test
public void testGetCategory() {
Category category = repo.findById(2).get();
System.out.println(category.getName());
Set<Category> children = category.getChildren();

for(Category child :children) {
	System.out.println(child.getName());
	
	
}
	
assertThat(children.size())	.isGreaterThan(0);
}
@Test
public void testPrintHierarachicalCategories() {
	
	Iterable<Category> categories = repo.findAll();
	
	for(Category category : categories) {
		if(category.getParent() == null) {
			
			System.out.println(category.getName());
			Set<Category> children = category.getChildren();
			for(Category childCategory : children) {
				
				
				System.out.println("--" + childCategory.getName() );
				printChildren(childCategory, 1);
			}
			
		}
		
		
		
		
	}
	
	
}
public void printChildren(Category parent , int subLevel) {
	
	int newSubLevel = subLevel + 1 ;
	Set<Category> children = parent.getChildren();
	for(Category subCategory : children) {
		for(int i = 0;i<newSubLevel;i++) {
		
		System.out.println("---" );
		}
		System.out.println(subCategory.getName());
		printChildren(subCategory, newSubLevel);
	}
	
	
	
	
	
}

@Test
public void testRootElement() {
	
	List<Category> listRoot = repo.findRootCategories(Sort.by("name").ascending());
	
	listRoot.forEach(list -> System.out.println(list.getName()));
	
	
}


@Test
public void testFindByName() {
	String name = "Computrs";
	Category result = repo.findByName(name);
	assertThat(result).isNotNull();
	assertThat(result.getName()).isEqualTo(name);
}

}

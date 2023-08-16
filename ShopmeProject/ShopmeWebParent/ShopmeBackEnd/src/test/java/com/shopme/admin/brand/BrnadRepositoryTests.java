package com.shopme.admin.brand;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.RoleRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class BrnadRepositoryTests {
	@Autowired
private BrandsRepository repo;
	
	
	@Test
	public void testCreateNewBrand() {
		Category lapop = new Category(6);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(lapop);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
		
		
		
	}
	
	@Test
	public void testCreatBrand2() {
		Category cellphones = new Category(4);
		Category tablets = new Category(7);
		
		
		Brand apple = new Brand("Apple");
		
		apple.getCategories().add(cellphones);
		apple.getCategories().add(tablets);
		
Brand savedBrand = repo.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	
		
	}
	
	@Test
	public void testCreateBrand3() {
		Brand samsung = new Brand("Samsung");
		
		samsung.getCategories().add(new Category(29));
		samsung.getCategories().add(new Category(24));
		
		
Brand savedBrand = repo.save(samsung);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
	}
	@Test
	public void listAll() {
		Iterable<Brand> brands = repo.findAll();
		
		
		brands.forEach(brand -> System.out.println(brand));
		
		assertThat(brands).isNotEmpty();
		
		
	}
	
	
	@Test
	public void testGetById() {
		Brand brand = repo.findById(1).get();
		
		assertThat(brand.getName()).isEqualTo("Acer");
		
		
	}
	
	@Test
	public void testUpdate() {
		String update ="Samsung Electronics";
	Brand  samsung = repo.findById(4).get();
	samsung.setName(update);
	Brand savedBrand = repo.save(samsung);
	assertThat(savedBrand.getName()).isEqualTo("Samsung Electronics");	
		
	}
	@Test
	public void testDelete() {
		repo.deleteById(3);
		Optional<Brand> result = repo.findById(2);
		assertThat(result.isEmpty());
	}
	
	
	
	
	
	
	
}
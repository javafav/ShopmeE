package com.shopme.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository repo;
	
	
	@Autowired
	private TestEntityManager entityManager;
	
	
	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 37);
		Category category =  entityManager.find(Category.class, 5);
		
		Product product = new Product();
		
		product.setName("Acer Aspire Inspire");
		product.setShortDescription("A good desktop from Acer");
		product.setFullDescription("This is a very good computer full descrption");
		product.setAlias("acer_inspiration_3000");
		product.setBrand(brand);
		product.setCategory(category);	
		
		product.setPrice(648);
		product.setCost(147);
		product.setInStock(true);
		product.setEnabled(true);
		product.setCreatedTime(new Date());	
		product.setUpdatedTime(new Date());	
	
	
	Product savedProduct = repo.save(product);
	
	assertThat(savedProduct.getId()).isGreaterThan(0);
	
	assertThat(savedProduct).isNotNull();
	
	}
	
	
	
	
	@Test
	public void listAll() {
		Iterable<Product> listProducts = repo.findAll();
		
		listProducts.forEach(product -> System.out.println("Product Name is "+product.getName()));
	
		assertThat(listProducts).isNotNull();
		
		assertThat(listProducts).hasSize(3);
	
	}
	
	@Test
	public void testGetProdcut() {
		
		Product findById = repo.findById(2).get();
	System.out.println(findById);
		assertThat(findById).isNotNull();
		
	
	
	
	}
	
	@Test
	public void testUpdateProdcut() {
		
		Product findById = repo.findById(2).get();
	 findById.setName("Samsung Galxy A32");
	 Product updatedProduct = repo.findById(2).get();
	 String result = updatedProduct.getName();
	 assertThat(result).isEqualTo("Samsung Galxy A32");
		
	
	
	
	}
	
	@Test
	public void testDeleteProdcut() {
		
		Product findById = repo.findById(2).get();
	repo.delete(findById);
	Optional<Product> updatedProduct = repo.findById(2);

	 assertThat(updatedProduct ).isNull();
		
	
	
	
	}
	
	
	@Test
	public void testSaveImages() {
		Product product = repo.findById(1).get();
		
		product.setMainImage("main_image.jpeg");
		
		product.addExtraImages("extra_image 1");
		product.addExtraImages("extra_image2");
		product.addExtraImages("extra-image3");
		
		Product savedProduct = repo.save(product);
		
		assertThat(savedProduct.getImages().size()).isEqualTo(3);
		
		
	}
	@Test
	public void testGetProductsFromsub() {
		Integer categoryId = 29;
		String keyword = "16GB";
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		int pageNo = 0;
		int pageSize = 4 ;
			
			Pageable page =PageRequest.of(pageNo,pageSize);
		
		Page<Product> searchInCategory = repo.searchInCategory(categoryId, categoryIdMatch, keyword, page);
		
		List<Product> listProduct = searchInCategory.getContent();
	listProduct.forEach(product -> System.out.println(product.getName()));
		assertThat(listProduct.size()).isEqualTo(2);
		//assertThat(savedProduct.getImages().size()).isEqualTo(3);
		
		
	}
	
	
	
	
	
	
}

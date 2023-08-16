package com.shopme.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.shopme.common.entity.product.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProductRepositoryTest {

	@Autowired private ProductRepository repo;
	
	@Test
	public void testFindByAlias() {
		String alias ="canon-eos-m50";
		Product product = repo.findByAlias(alias);
		
	assertThat(product).isNotNull();
		
		
	}
	
	@Test
	public void testSearch() {
		String keyword = "Samsung" ;
		int pageNo = 0;
		int pageSize = 10 ;
			
			Pageable page =PageRequest.of(pageNo,pageSize);
		Page<Product> search = repo.search(keyword, page);
		List<Product> list = search.getContent();
		list.forEach(l -> System.out.println(l.getName()));
		
	}
}

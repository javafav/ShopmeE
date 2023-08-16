package com.shopme.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.product.Product;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	
	@Query("SELECT p FROM Product p WHERE p.enabled = true"
			+ " AND  (p.category.id = ?1 "
			+ "OR p.category.allParentIDs LIKE %?2%)"
			+ "ORDER BY p.name ASC")
	
	public Page listByCategoyr(Integer categoryId ,String CategoryIdMatch,Pageable pageable);
	
	
	public Product findByAlias(String alias);

//	@Query(value = "SELECT * FROM products WHERE enabled = true AND "
//			+ " MATCH(name,short_description,full_description) AGAINST (?1) ", 
//			nativeQuery = true)
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
		+ "OR p.shortDescription LIKE %?1%"
			+ "OR p.fullDescription LIKE %?1%"  )
		
		
	public Page<Product> search(String keyword, Pageable pageable);
}

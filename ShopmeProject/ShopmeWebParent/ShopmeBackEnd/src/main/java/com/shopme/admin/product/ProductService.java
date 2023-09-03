package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class ProductService {
	public static final int PRODUCTS_PER_PAGE = 10;
	public static final int PRODUCTS_PER_FRAME = 5;

	@Autowired
	private ProductRepository repo;

	public List<Product> listAll() {

		return (List<Product>) repo.findAll();

	}

	public Product save(Product product) {
		if (product.getId() == null) {

			product.setCreatedTime(new Date());

		}
		if (product.getAlias() == null || product.getAlias().isEmpty()) {

			String defaultAlias = product.getName().replace(" ", "-");
			product.setAlias(defaultAlias);

		} else {

			product.setAlias(product.getAlias().replace(" ", "-"));

		}

		product.setUpdatedTime(new Date());
		return repo.save(product);

	}

	public String checkUnique(Integer id, String name) {

		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);

		if (isCreatingNew) {

			if (productByName != null) {
				return "DuplicateName";

			}

		} else {

			if (productByName != null && productByName.getId() != id)
				return "DuplicateName";

		}

		return "OK";

	}

	@Transactional
	@Modifying
	public void updateCategoryStatus(Integer id, boolean status) throws ProductNotFoundException {
		Long countById = repo.countById(id);
		if (countById == 0 || countById == null) {
			throw new ProductNotFoundException("Could not found product with given id" + id);
		}
		repo.changeStatus(id, status);

	}

	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = repo.countById(id);
		if (countById == 0 || countById == null) {
			throw new ProductNotFoundException("Could not found product with given id" + id);
		}
		repo.deleteById(id);

	}

	public Product getProductById(Integer id) throws ProductNotFoundException {

		try {
			Product product = repo.findById(id).get();
			return product;
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("No productfoutn with given id" + id);
		}

	}

//	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
//
//		Sort sort = Sort.by(sortField);
//		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
//		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
//		if (keyword != null && !keyword.isEmpty()) {
//			if (categoryId != null && categoryId > 0) {
//				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
//				
//				return repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
//			}
//
//			return repo.findAll(keyword, pageable);
//
//		}
//		if (categoryId != null && categoryId > 0) {
//			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
//			;
//			return repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
//		}
//
//		return repo.findAll(pageable);
//
//	}
	public void listByPage(int pageNum, PagingAndSortingHelper helper, Integer categoryId) {
		Pageable pageable =  helper.createPageable(PRODUCTS_PER_PAGE, pageNum);
		String keyword = helper.getKeyword();
		Page<Product> page = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			} else {
				page = repo.findAll(keyword, pageable);
			}
		} else {
			if (categoryId != null && categoryId > 0) {
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				page = repo.findAllInCategory(categoryId, categoryIdMatch, pageable);
			} else {		
				page = repo.findAll(pageable);
			}
		}
		
		helper.updateModelAttributes(pageNum, page);
	}


	public void searchProduct(int pageNum , PagingAndSortingHelper helper) {
		
		Pageable pageable = helper.createPageable(PRODUCTS_PER_FRAME , pageNum);
		String keyword = helper.getKeyword();
		System.out.println("Keyword : "+ keyword);
		Page<Product> page = repo.searchProductsByName(keyword, pageable);
//		List<Product> products = page.getContent();
//		for(Product product : products) {
//			
//			System.out.println("Product Name is  : "+ product.getShortName());
//		}
	helper.updateModelAttributes(pageNum, page);
		
	}
	
	public void saveProductPrice(Product productInForm) {

		Product productInDB = repo.findById(productInForm.getId()).get();

		productInDB.setPrice(productInForm.getPrice());
		productInDB.setCost(productInForm.getCost());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());

		repo.save(productInDB);

	}

}

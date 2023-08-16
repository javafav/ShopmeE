package com.shopme.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopme.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {

	@Autowired private CategoryService categoryService;
	@Autowired private ProductsService productsService;
	
	
	
@GetMapping("/c/{category_alias}")
	
	public String viewCategoryFirstPage(@PathVariable("category_alias") String alias , Model model) throws ProductNotFoundException, CategoryNotFoundException {
	

	return viewCategoryByPage(alias, 1, model);

}
	
	@GetMapping("/c/{category_alias}/page/{pageNum}")
	
	public String viewCategoryByPage(@PathVariable("category_alias") String alias ,@PathVariable  int pageNum , Model model) throws ProductNotFoundException, CategoryNotFoundException{
		
		Category category;
		try {
			category = categoryService.getCategory(alias);
			List<Category> listCategoryParent = categoryService.getCategoryParnet(category);
			
			Page<Product> pageProducts = productsService.listByCategory(pageNum, category.getId());
			
			List<Product> listProducts = pageProducts.getContent();
			
			
			long startCount = (pageNum - 1) * ProductsService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductsService.PRODUCTS_PER_PAGE ;
			if (endCount > pageProducts.getTotalElements()) {

				endCount = pageProducts.getTotalElements();
			}
			
			
			// listUsers.forEach(user ->System.out.println(user));
			
		
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());
			model.addAttribute("listProducts", listProducts);
			
			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			
			model.addAttribute("category", category);
			model.addAttribute("listCategoryParent", listCategoryParent);
			model.addAttribute("pageTitle", category.getName());
		
			
			return "product/products_by_category" ;
		} catch (CategoryNotFoundException e) {
			return "error/404";
		}
		
	
	
	}
	
	
	@GetMapping("/p/{prodcut_alias}")
	public String  viewProductDetail(@PathVariable("prodcut_alias") String alias , Model model) {
		
		try {
		Product product = productsService.getProduct(alias);
		List<Category> listCategoryParent = categoryService.getCategoryParnet(product.getCategory());

		model.addAttribute("product", product);
		model.addAttribute("listCategoryParent", listCategoryParent);
		model.addAttribute("pageTitle", product.getShortName());
		return "product/product_detail" ;	
		}catch (ProductNotFoundException e){
			
			return "error/404";
		}
		
		
		
	}
	
	@GetMapping("/search")
	public String searchFirstPage(@Param ("keyword") String keyword , Model model) {
		return  searchByPage(keyword, 1, model);
	}
	
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@Param ("keyword") String keyword ,
			@PathVariable("pageNum") int pageNum , Model model) {
		
		Page<Product> pageProducts = productsService.search(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();
		
		long startCount = (pageNum - 1) * ProductsService.SEARCH_RESULT_PER_PAGE + 1;
		long endCount = startCount + ProductsService.SEARCH_RESULT_PER_PAGE ;
		if (endCount > pageProducts.getTotalElements()) {

			endCount = pageProducts.getTotalElements();
		}
		
		
		// listUsers.forEach(user ->System.out.println(user));
		
	
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("pageTitle", keyword + "- Search Result");
		
		model.addAttribute("keyword", keyword);
		model.addAttribute("listResult", listResult);
		
		return "product/search_result";
	}
	
}

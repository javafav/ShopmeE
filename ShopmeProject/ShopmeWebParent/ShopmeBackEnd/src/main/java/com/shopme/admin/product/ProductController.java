package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandService;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Controller
public class ProductController {
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";


	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

//	@GetMapping("/products")
//	public String lisFirstPage(Model model) {
//
//		return listPageByProduct(1, model, "name", "asc", null , 0);
//	}
	
	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {
		List<Brand> listBrands = brandService.listAll();
		// System.out.println(listBrands);

		Product product = new Product();

		product.setEnabled(true);
		product.setInStock(true);
		Integer numberOfExistingExtraImages = product.getImages().size();

		model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Create New Product");

		return "products/product_form";

	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainIamgeMultipartFile,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipartFile,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValue,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames ,
			 @AuthenticationPrincipal ShopmeUserDetails loggedUser) throws IOException {

	if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor"))	{
		if(loggedUser.hasRole("Salesperson"))  {
			productService.saveProductPrice(product);
			ra.addFlashAttribute("message", "The product has been saved succsfully.");
			return "redirect:/products"; 
			}
			
		}
		
		ProductSaveHelper.setMainImage(mainIamgeMultipartFile, product);
		ProductSaveHelper.setExistingExtraImagesNames(imageIDs, imageNames, product);
		ProductSaveHelper.setNewExtraImaageName(extraImageMultipartFile, product);
		ProductSaveHelper.setDetailNameAndVlaues(detailNames, detailIDs, detailValue, product);

		Product savedProduct = productService.save(product);

		ProductSaveHelper.saveUploadedImages(mainIamgeMultipartFile, extraImageMultipartFile, savedProduct);
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(savedProduct);
		ra.addFlashAttribute("message", "The product has been saved succsfully.");
		return "redirect:/products";

	}

	
	@GetMapping("products/{id}/status/{status}")
	public String changeProductStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) throws ProductNotFoundException {

		String messageStatus = status ? "enabled" : "disabled";
		String message = "Product   (ID : " + id + " ) has been  " + messageStatus + " successfuly !";
		redirectAttributes.addFlashAttribute("message", message);
		productService.updateCategoryStatus(id, status);
		return "redirect:/products";

	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes)
			throws ProductNotFoundException {

		try {

			productService.delete(id);
			String ExtraImagesDir = "../products-images/" + id + "/extras";
			String mainImagesDir = "../products-images/" + id;
			FileUploadUtil.removeDir(ExtraImagesDir);
			FileUploadUtil.removeDir(mainImagesDir);
			redirectAttributes.addFlashAttribute("message",
					"The prodcut ID:  " + id + " has been deleted successfully");
			return "redirect:/products";

		} catch (ProductNotFoundException ex) {
			// TODO Auto-generated catch block

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}

	}

	@GetMapping("/products/edit/{id}")
	public String updateProductDetail(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra ,  @AuthenticationPrincipal ShopmeUserDetails loggedUser) {

		try {

			Product product = productService.getProductById(id);
			List<Brand> listBrands = brandService.listAll();

			Integer numberOfExistingExtraImages = product.getImages().size();
		
			boolean isReadOnlyForSalesperson = false;
			if(!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor"))	{
				if(loggedUser.hasRole("Salesperson"))  {
					isReadOnlyForSalesperson = true;
				
					}
				}

			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
			model.addAttribute("isReadOnlyForSalesperson", isReadOnlyForSalesperson);
			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", " Edit Product (ID " + id + " )");
			


			return "products/product_form";
		} catch (ProductNotFoundException ex) {

			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}

	}

	@GetMapping("/products/details/{id}")
	public String viewProductDetail(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {

		try {

			Product product = productService.getProductById(id);

			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException ex) {

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/products";
		}

	}

//	@GetMapping("products/page/{pageNum}")
//
//	public String listPageByProduct(@PathVariable int pageNum,
//			Model model, 
//			@Param("sortField") String sortField,
//			@Param("sortDir") String sortDir,
//			@Param("keyword") String keyword ,
//			@Param("categoryId") Integer categoryId) {
//
//		
//
//		Page<Product> pages = productService.listByPage(pageNum, sortField, sortDir, keyword , categoryId);
//		List<Product> listProducts = pages.getContent();
//        List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
//     
//		long endCount = startCount + BrandService.BRANDS_PER_PAGE;
//		if (endCount > pages.getTotalElements()) {
//
//			endCount = pages.getTotalElements();
//		}
//		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//
//		// listUsers.forEach(user ->System.out.println(user));
//		if(categoryId != null) model.addAttribute("categoryId", categoryId);
//		
//		
//		model.addAttribute("startCount", startCount);
//		model.addAttribute("endCount", endCount);
//		model.addAttribute("totalItems", pages.getTotalElements());
//		model.addAttribute("listProducts", listProducts);
//		model.addAttribute("listCategories", listCategories);
//		model.addAttribute("currentPage", pageNum);
//		model.addAttribute("totalPages", pages.getTotalPages());
//		model.addAttribute("sortField", sortField);
//		model.addAttribute("sortDir", sortDir);
//		model.addAttribute("reverseSortDir", reverseSortDir);
//		model.addAttribute("keyword", keyword);
//		model.addAttribute("moduleURL", "/products");
//		return "products/products";
//
//	}
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			Integer categoryId
			) {
		
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		
		return "products/products";		
	}
	

}

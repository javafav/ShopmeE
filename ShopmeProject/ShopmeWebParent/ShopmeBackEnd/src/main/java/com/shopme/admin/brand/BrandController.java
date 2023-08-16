package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryCsvExporter;

import com.shopme.admin.category.CategoryService;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage() {
		
	    return "redirect:/brands/page/1?sortField=name&sortDir=asc";

		
	}
	
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");
		
		return "brands/brand_form";
	}
	@PostMapping("/brands/save")
	public String saveCategory(Brand brand, 
			@RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brands-logos/" + savedBrand.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			brandService.save(brand);
		}
		
		ra.addFlashAttribute("message", "The brand has been saved successfully.");
		return "redirect:/brands";
	}
	
	
	
	@GetMapping("/brands/delete/{id}")
	public String deleeCategory(@PathVariable Integer id,
			Model model,
			RedirectAttributes redirectAttributes) throws  BrandNotFoundException {

		
		try {
			brandService.delete(id);
			String brandsLogoDir = "../brands-logos/" + id ;
		FileUploadUtil.removeDir(brandsLogoDir);
			redirectAttributes.addFlashAttribute("message",
					"The brand ID:  " + id + " has been deleted successfully");
			return "redirect:/brands";

		} catch (BrandNotFoundException ex) {
			// TODO Auto-generated catch block

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}

	}
	
	

	@GetMapping("/brands/edit/{id}")
	public String updateBrandDetail(@PathVariable(name="id")Integer id, Model model, RedirectAttributes redirectAttributes) {

		Brand brand;
		try {
			brand= brandService.getBrandById(id);
          List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("brand", brand);
		
			model.addAttribute("pageTitle", " Edit Brand (ID " + id + " )");
			
			model.addAttribute("listCategories", listCategories);
			return "brands/brand_form";
		} catch (NoSuchElementException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}

	}
	
	
	@GetMapping("brands/page/{pageNum}")

	public String listByPage(@PagingAndSortingParam(listName = "listBrands" ,moduleURL = "/brands") PagingAndSortingHelper helper,@PathVariable int pageNum) {
            brandService.listByPage(pageNum, helper);
		return "brands/brands";

	}
	
	@GetMapping("/brands/export/csv")
	public void expoertToCSV(HttpServletResponse response) throws IOException {
List<Brand> listBrands= brandService.listAll();
	 
	    
	    BrandCsvExporter exporter = new BrandCsvExporter();
		
		exporter.export(listBrands, response);
	}

	
	
	
	
	
	
}

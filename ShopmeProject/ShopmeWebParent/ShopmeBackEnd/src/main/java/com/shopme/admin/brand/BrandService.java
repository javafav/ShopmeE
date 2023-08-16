package com.shopme.admin.brand;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Service
public class BrandService {

	public static final int BRANDS_PER_PAGE = 10;
	@Autowired
	private BrandsRepository repo;
	
	public List<Brand> listAll(){
		return (List<Brand>) repo.findAll();
		
	}
	
	
public Brand save (Brand brand) {
	return repo.save(brand);
}


public void delete(Integer id) throws BrandNotFoundException {
	Long countById = repo.countById(id);
	if( countById == 0 || countById == null) {
		throw new BrandNotFoundException("Could not found brand with given id" + id);
	}
	repo.deleteById(id);
	
}
	
public String checkUnique(Integer id, String name) {

	boolean isCreatingNew = (id == null || id == 0);
	 Brand brandByName = repo.findByName(name);
	

			
			if (isCreatingNew) {

				if (brandByName != null) {
					return "DuplicateName";

				} 
				
			}else  {
				 
					 if(brandByName != null && brandByName.getId() != id)
					return "DuplicateName";
					
				}
			
				return "OK";
				

	
}


public Brand getBrandById(Integer id) {
	// TODO Auto-generated method stub
	return repo.findById(id).get();
}


public void listByPage(int pageNum,PagingAndSortingHelper helper){
helper.listEntites(pageNum, BRANDS_PER_PAGE, repo);
	
	}
	
	
		




}


package com.shopme.admin.paging;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

public class PagingAndSortingHelper {
    
	private ModelAndViewContainer model;
	private String moduleURL ;
	private String listName ;
	private String  sortDir;
    private String  sortField;
	private String  keyword;
	
	
	public PagingAndSortingHelper(ModelAndViewContainer model , String moduleURL , String listName ,
			String  sortDir,String  sortField,String  keyword) {
		this.model = model;
		this.moduleURL = moduleURL;
		this.listName = listName;
	    this.sortDir = sortDir;
	    this.sortField  = sortField;
	    this.keyword = keyword;
	
	
	}
	
	public void updateModelAttributes(int pageNum , Page<?> page  ) {
		
		List<?> listItems = page.getContent();
        int pageSize = page.getSize();
		
		long startCount = (pageNum - 1) * pageSize + 1;
	
		long endCount = startCount + pageSize - 1;
		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		// listUsers.forEach(user ->System.out.println(user));
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute(listName, listItems);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
	
		                    
		model.addAttribute("moduleURL", moduleURL);
		
		
	}
  public void listEntites(int pageNum , int pageSize , SearchRepository<?, Integer> repo) {
	  
	  Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
			Pageable pageable = PageRequest.of(pageNum-1, pageSize,sort);
		  Page<?> page = null;
			if(keyword != null) {
			
			page = repo.findAll(keyword,pageable);
			
		}else {
			page =  repo.findAll(pageable); 
		}
			
			 updateModelAttributes(pageNum, page);
	  
  }
	public String getSortDir() {
		return sortDir;
	}

	public String getSortField() {
		return sortField;
	}

	public String getKeyword() {
		return keyword;
	}
	
	
}
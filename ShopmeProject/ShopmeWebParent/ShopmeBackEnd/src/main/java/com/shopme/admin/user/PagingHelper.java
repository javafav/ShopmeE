package com.shopme.admin.user;

import org.springframework.stereotype.Component;

@Component
public class PagingHelper {

	private int pageNum;
	private int totalPages;
	private int noOfElements;
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getNoOfElements() {
		return noOfElements;
	}
	public void setNoOfElements(int noOfElements) {
		this.noOfElements = noOfElements;
	}
	public PagingHelper(int pageNum, int totalPages, int noOfElements) {
		super();
		this.pageNum = pageNum;
		this.totalPages = totalPages;
		this.noOfElements = noOfElements;
	}
	
	public PagingHelper() {
		super();
	}
	@Override
	public String toString() {
		return "PagingHelper [pageNum=" + pageNum + ", totalPages=" + totalPages + ", noOfElements=" + noOfElements
				+ "]";
	}
	
	
}

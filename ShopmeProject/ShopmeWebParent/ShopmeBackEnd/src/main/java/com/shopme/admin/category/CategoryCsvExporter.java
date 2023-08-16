package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;


public class CategoryCsvExporter extends AbstractExporter{
	
	public void export(List<Category> listCategories,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".csv", "text/csv" , "Categories_");
	
		  String[] csvHeader = new String[] { "Category ID", " Category Name" };
	  String[]  fieldMapping = new String[] { "id", "name"};
	ICsvBeanWriter csvWriter  = new CsvBeanWriter(response.getWriter(), 
			CsvPreference.STANDARD_PREFERENCE);
	csvWriter.writeHeader(csvHeader);
for(Category cat :listCategories) {
	
	cat.setName(cat.getName().replace("--", "  "));	
	csvWriter.write(cat,fieldMapping);
	
}
	csvWriter.close();

	}
	
	
	
}

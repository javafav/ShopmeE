package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;


public class BrandCsvExporter extends AbstractExporter {

	
	public void export(List<Brand> listBrands,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".csv", "text/csv" , "Brands_");
	
		  String[] csvHeader = new String[] { "Brand ID", " Brand Name" };
	  String[]  fieldMapping = new String[] { "id", "name"};
	ICsvBeanWriter csvWriter  = new CsvBeanWriter(response.getWriter(), 
			CsvPreference.STANDARD_PREFERENCE);
	csvWriter.writeHeader(csvHeader);
for(Brand brand :listBrands) {
	
		
	csvWriter.write(brand,fieldMapping);
	
}
	csvWriter.close();

	}
}

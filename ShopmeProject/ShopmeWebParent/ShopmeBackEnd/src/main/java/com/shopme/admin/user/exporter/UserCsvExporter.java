package com.shopme.admin.user.exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.User;

public class UserCsvExporter  extends AbstractExporter{
	private final String[] header = new String[] { "User ID", "E-mail" , "First Name", "Last Name", "Roles" ,"Enabled"};
	public void export(List<User> listUser,HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".csv", "text/csv" ,"Users_");
	
	  String[] header = new String[] { "User ID", "E-mail" , "First Name", "Last Name", "Roles" ,"Enabled"};
	  String[]  fieldMapping = new String[] { "id", "email" , "firstName", "lastName", "roles" ,"enabled"};
	ICsvBeanWriter csvWriter  = new CsvBeanWriter(response.getWriter(), 
			CsvPreference.STANDARD_PREFERENCE);
	csvWriter.writeHeader(header);
for(User user :listUser) {
	csvWriter.write(user,fieldMapping);
	
}
	csvWriter.close();

	}
	
	
}

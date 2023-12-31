package com.shopme.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
public void setResponseHeader(HttpServletResponse response,String fileExtention,
		String contentType ,String prefix) throws IOException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = prefix+timeStamp+fileExtention;
		response.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue =  "attachment; filename=" +fileName;
		response.setHeader(headerKey, headerValue);
}
}

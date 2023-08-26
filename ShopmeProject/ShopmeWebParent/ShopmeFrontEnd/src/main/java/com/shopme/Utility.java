package com.shopme;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

public class Utility {

	public static String getSiteURL(HttpServletRequest request) {
		
	String siteURL = request.getRequestURL().toString();
		
		return siteURL.replace(request.getServletPath(), "");
	} 
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag setting ) {
		
		JavaMailSenderImpl  mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(setting.getHost());
		mailSender.setPort(setting.getPort());
		mailSender.setUsername(setting.getUsername());
		mailSender.setPassword(setting.getPassword());
		
		Properties mailProperties = new Properties();
		
		mailProperties.setProperty("mail.smtp.auth", setting.getSmtpAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", setting.getSmtpSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
		
	}
	
	public static String getEmailAuthenticatedCustomer(HttpServletRequest request) {
		Object principal = request.getUserPrincipal();
	   if(principal == null) return null;
		String customerEmail = null;
	    if(principal instanceof UsernamePasswordAuthenticationToken ||
	    		principal instanceof RememberMeAuthenticationToken  ) {
	    	customerEmail = request.getUserPrincipal().getName();
	    }else if(principal instanceof OAuth2AuthenticationToken ) {
	    	OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken )principal;
	    	CustomerOAuth2User oauth2User = (CustomerOAuth2User) oauth2Token.getPrincipal();
	    	customerEmail =  oauth2User.getEmail();
	    }
	return customerEmail;
	}
	
	public static String formatCurrency(float amount ,CurrencySettingBag setting) {
		
		String symbol = setting.getSymbol();
		String symbolPostion = setting.getSymbolPostion();
		String decimalPointType = setting.getDecimalPointType();
	    String thousnadPointType = setting.getThousnadPointType();
		int decimalDigits = setting.getDecimalDigits();
		
		
		String pattren = symbolPostion.equals("Before price") ? symbol : "";
		pattren +="###,###";
		
		if(decimalDigits>0) {
			pattren += "." ;
			for(int count=1 ; count <=decimalDigits ; count++) pattren+="#";
				
			
		}
		 pattren += symbolPostion.equals("After price") ? symbol : "";

		char thousandSepartor = thousnadPointType.equals("POINT") ? '.' :',' ;
		char decimalSepartor = thousnadPointType.equals("POINT") ? '.' :',' ;
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		decimalFormatSymbols.setDecimalSeparator(decimalSepartor);
		decimalFormatSymbols.setGroupingSeparator(thousandSepartor);
		DecimalFormat formatter = new DecimalFormat(pattren , decimalFormatSymbols);
		return formatter.format(amount);
	}

	
}

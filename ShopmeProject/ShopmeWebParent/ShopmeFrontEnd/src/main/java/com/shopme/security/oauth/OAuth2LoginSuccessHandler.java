package com.shopme.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Customer;
import com.shopme.customer.CustomerService;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired private CustomerService customerService;
	  
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		CustomerOAuth2User oauth2uUser = (CustomerOAuth2User)authentication.getPrincipal();
		
		String name = oauth2uUser.getName();
		String email= oauth2uUser.getEmail();
		String clientName = oauth2uUser.getClientName();
		String countryCode = request.getLocale().getCountry();
//		System.out.println("OAuth2LoginSuccessHandler ..."+ name +"| " + email);
//		System.out.println("Client Name is " + clientName);
		
		AuthenticationType authenticationType = getAauthenticationType(clientName);
		Customer customer = customerService.getCustomerByEmail(email);
		       if(customer == null) {
		    	    customerService.addNewCustomerUponOAuthLogin(name , email , countryCode , authenticationType);
		    	   
		       }else {
		    	   oauth2uUser.setFullName(customer.getFullName());
		    	   customerService.updateAuthenticationType(customer, authenticationType);
		    	   
		       }
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

	
	private AuthenticationType getAauthenticationType(String clientName) {
		if (clientName.equals("Google")) {
			return AuthenticationType.GOOGLE;
		} else if (clientName.equals("Facebook")) {
			return AuthenticationType.FACEBOOK;
		} else {
			return AuthenticationType.DATABASE;
		}
		
	 }
	
}

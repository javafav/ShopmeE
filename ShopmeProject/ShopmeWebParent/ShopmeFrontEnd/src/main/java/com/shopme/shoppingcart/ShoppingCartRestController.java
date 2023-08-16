package com.shopme.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.CustomerNotFoundException;
import com.shopme.customer.CustomerService;

@RestController
public class ShoppingCartRestController {

	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId")Integer productId , 
			@PathVariable("quantity") Integer quantity , HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
		    Integer updateQuantity = cartService.addProduct(productId, quantity, customer);
		    return updateQuantity + "item(s) of this product were added to your shopping cart.";
		} catch (CustomerNotFoundException e) {
			return "You must login to add this product to cart.";
		} catch(ShoppingCartException e) {
			return e.getMessage();
		}
	
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailAuthenticatedCustomer(request);
	    if(email == null) {
	    	throw new CustomerNotFoundException("No authenticated customer ");
	    }
		Customer customer = customerService.getCustomerByEmail(email);
	    return customer;
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId")Integer productId , 
			@PathVariable("quantity") Integer quantity , HttpServletRequest request) {
		try {
			Customer customer = getAuthenticatedCustomer(request);
		   float subtotal = cartService.updateQuantity(productId, quantity, customer);
		    return String.valueOf(subtotal);
		} catch (CustomerNotFoundException e) {
			return "You must login to cahnge the quantity of product.";
		} 
		
	} 



   @GetMapping("/cart/remove/{productId}")
   public String removeProduct(@PathVariable("productId")Integer productId , HttpServletRequest request ) {
	   try {
			Customer customer = getAuthenticatedCustomer(request); 
	        Integer customerId = customer.getId();
	      System.out.println(customerId + " prd id" + productId);
	        cartService.removeProduct(customerId, productId);
	       return "The product has removed successfullly from your cart.";
   } catch (CustomerNotFoundException e) {
	return "You must login to remove product.";  
   }
   }
   @GetMapping("/cart/totalItems")
   public String getTotalCartItem(HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request); 
			Integer totalCartItem = cartService.totalCartItem(customer);
	  
	       return String.valueOf(totalCartItem);

	   
   }  catch (CustomerNotFoundException e) {
	return "You must login to remove product.";  
   }
 }
}

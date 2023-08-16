package com.shopme.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.Utility;
import com.shopme.address.AddressService;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerService;
import com.shopme.shipping.ShippingRateService;

@Controller
public class ShoppingCartContoller {
  
	@Autowired private CustomerService customerService;
	@Autowired private ShoppingCartService cartService ;
	@Autowired private AddressService addressService;
	@Autowired private ShippingRateService shipService ;
	@GetMapping("/cart")
	public String viewCart(Model model , HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItem = cartService.listCartItem(customer);
		 float estimatedTotal = 0.0F;
		for(CartItem item : cartItem) {
			estimatedTotal += item.getSubtotal();
		}
		
	Address defaultAddress = addressService.getDefaultAddress(customer);
	ShippingRate shippingRate = null;
	boolean usePrimaryAddressAsDefault = false;
	if(defaultAddress != null ) {
	    
		shippingRate =  shipService.getShippingRateForAddress(defaultAddress);
		
	} else {
		usePrimaryAddressAsDefault = true ;
		shippingRate = shipService.getShippingRateForCustomer(customer);
		
	}
	
	 
		
		Integer totalCartItem = cartService.totalCartItem(customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
		model.addAttribute("shippingSupported", shippingRate != null);

		model.addAttribute("cartItem", cartItem);
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("totalCartItem", totalCartItem);
		return "cart/shopping_cart"; 
		
	}
	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailAuthenticatedCustomer(request);
	   Customer customer = customerService.getCustomerByEmail(email);
	    return customer;
	}

}

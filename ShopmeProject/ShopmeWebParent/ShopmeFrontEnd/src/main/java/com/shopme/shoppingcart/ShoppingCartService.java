package com.shopme.shoppingcart;

import java.util.List;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.product.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {
  @Autowired private CartItemRepository cartRepo;
  @Autowired private ProductRepository productRepo;
  public Integer addProduct(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {

		Integer updateQunatity = quantity;

		Product product = new Product(productId);

		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);

		if (cartItem != null) {
			updateQunatity = cartItem.getQuantity() + quantity;
             if(updateQunatity >5) {
            	 throw new ShoppingCartException("Could not add more" +  quantity + "  item(s) " 
            			 + "because there's already " +cartItem.getQuantity() +  " item(s)" + 
            			 " in your shopping cart. Maximum allowd quantity is 5.");
             }
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);

		}
		
		cartItem.setQuantity(updateQunatity);
		
		cartRepo.save(cartItem);
		
		return updateQunatity;

	}
  
	
	public List<CartItem> listCartItem(Customer customer){
	 return cartRepo.findByCustomer(customer);
	}
	
	public Integer totalCartItem(Customer customer) {
		return cartRepo.getTotalCartItem(customer.getId());
	}
	
	public float updateQuantity(Integer productId , Integer quantity , Customer customer) {
		
		cartRepo.updateQuantity(quantity, customer.getId(), productId);
	   Product product = productRepo.findById(productId).get();	
	   float subtotal = product.getDiscountPrice() * quantity ;
	   return subtotal ;
	
	}
	public void removeProduct(Integer productId ,Integer customerId) {
		cartRepo.deleteByCustomerAndProduct(productId, customerId);
	    System.out.println("Product Id " + productId + "  Customer Id" + customerId);
	}
	
	
	
	
	
}

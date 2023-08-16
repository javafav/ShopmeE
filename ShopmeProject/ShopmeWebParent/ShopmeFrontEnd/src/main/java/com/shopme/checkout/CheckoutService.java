package com.shopme.checkout;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
public class CheckoutService {

	private static final int DIM_DIVISER = 139 ;
	
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems , ShippingRate shippingRate ) {
		
		CheckoutInfo  checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems );
		float productTotal = calculateProductTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems , shippingRate);
		float paymentTotal = productTotal + shippingCostTotal ;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		
		return checkoutInfo;
		
	}

	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
	    float shippingCostTotal = 0.0f;
	     for(CartItem item : cartItems) {
	    	 
	    	Product product = item.getProduct();
	    	float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISER ;
	    	float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight ;
	        float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();
	     
	        shippingCostTotal += shippingCost ;
	     }
		return shippingCostTotal;
	}

	private float calculateProductTotal(List<CartItem> cartItems) {
		   float total = 0.0f;
		    
			for(CartItem item : cartItems) {
				total += item.getSubtotal();
				
			}
			return total;
		
	}

	private float calculateProductCost(List<CartItem> cartItems) {
	    float cost = 0.0f;
	    
		for(CartItem item : cartItems) {
			cost += item.getQuantity()*item.getProduct().getCost();
			
		}
		return cost;
	}
}

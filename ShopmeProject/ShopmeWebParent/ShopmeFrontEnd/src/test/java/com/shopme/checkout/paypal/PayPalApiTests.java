package com.shopme.checkout.paypal;


import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApiTests {
 private static final String BASE_URL = "https://api.sandbox.paypal.com";
 private static final String  GET_ORDER_API= "/v2/checkout/orders/";
 private static final String CLIENT_ID = "Ab7tF_UtBXVUebN53YKszlhSy8qLpI-Q8FvdyVEqE78giVC0anNNNg6S3YH4CykJV5fO0qlLBCcnLgaa";
 private static final String CLIENT_SECRECT = "EFBdgWrdGBUrKsp33mE7UyrAHW2esbKcqCn904qAM0t1uvqrzh3xZCQGStARGzyOSbRtECsBLeX17DTT";
 
 @Test
 public void testGetOrderDetails() {
	 String orderId = "5PT21165T0262932S";
	 String requestURL = BASE_URL + GET_ORDER_API + orderId ;
	 
	 HttpHeaders headers = new HttpHeaders();
	 headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	 headers.add("Accept_Language", "en-US");
	 headers.setBasicAuth(CLIENT_ID, CLIENT_SECRECT ); 
	 HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
	 RestTemplate restTemplate = new RestTemplate();
	 ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL,
			 HttpMethod.GET, request, PayPalOrderResponse.class);
	 PayPalOrderResponse orderResponse = response.getBody();
	
	 System.out.println("Order ID: "+ orderResponse.getId());
	 System.out.println("Validated: "+ orderResponse.validate(orderId));
 }
}

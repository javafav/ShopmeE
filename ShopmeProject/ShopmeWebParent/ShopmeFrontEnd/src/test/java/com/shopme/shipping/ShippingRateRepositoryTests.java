package com.shopme.shipping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.address.AddressRepository;
import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.customer.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ShippingRateRepositoryTests {

	@Autowired private ShippingRateRepository repo;
	@Autowired private AddressRepository addressRepository ;
	@Autowired private CustomerRepository customerRepository ;

	@Test
	public void testFindByCountryAndState() {
		Country usa = new Country(234);
		String state = "New York";
		ShippingRate shippingRate = repo.findByCountryAndState(usa, state);

		assertThat(shippingRate).isNotNull();
		System.out.println(shippingRate);
	}
	
 
	@Test
	public void testFndShippingRateByAddress() {

			Customer customer = customerRepository.findById(16).get();
			//List<Address> addresses = addressRepository.findByCustomer(customer);
			
		Address defaultAddress = addressRepository.findDefaultByCustomer(customer.getId());
		if(defaultAddress == null) {
			Country country = customer.getCountry();
			String state = customer.getState();
			ShippingRate shippingRate = repo.findByCountryAndState(country, state);

			assertThat(shippingRate).isNotNull();
			System.out.println(shippingRate);
		//	Country country = defaultAddress.getCountry();
			System.out.println(country);
		}
			System.out.println("Printing with Address " + defaultAddress);

		
		
	}
}
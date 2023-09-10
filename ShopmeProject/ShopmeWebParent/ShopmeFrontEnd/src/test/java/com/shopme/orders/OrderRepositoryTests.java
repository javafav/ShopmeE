package com.shopme.orders;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import com.shopme.common.entity.order.Order;
import com.shopme.order.OrderRepository;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderRepositoryTests {

	@Autowired private OrderRepository repo;
//	
//	@Test
//	public void testListOrders() {
//		String orderKeyword ="Apple";
//		 repo.findAll(orderKeyword, null, null)
//		
//		assertThat(orders).hasSizeGreaterThan(0);
//		
//		orders.forEach(System.out::println);
//	}
}

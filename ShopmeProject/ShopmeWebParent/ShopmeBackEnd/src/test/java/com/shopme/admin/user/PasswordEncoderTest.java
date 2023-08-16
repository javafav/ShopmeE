package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncdoerPassword() {
		
		BCryptPasswordEncoder bCryptPasswordEncoder = new  BCryptPasswordEncoder();
		String rawPassword ="nam2020";
	String encode = bCryptPasswordEncoder.encode(rawPassword);
		System.out.println(encode);
		boolean matches = bCryptPasswordEncoder.matches(rawPassword, encode);
		assertThat(matches).isTrue();
		System.out.println(matches);
	}
}

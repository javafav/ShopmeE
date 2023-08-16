package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
		
		
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.authorizeRequests()
		//.antMatchers("/images/**","/js/**","/webjars/**").permitAll()
	
		
		.antMatchers("/users/**").hasAuthority("Admin")
		.antMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
		.antMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
		
		
		.antMatchers( "/products/new"  , "/products/delete/**")
		.hasAnyAuthority("Admin", "Editor")
		
		
		.antMatchers( "/products/edit/**"  , "/products/save" , "/products/check_unique")
		.hasAnyAuthority("Admin", "Editor" ,"Salesperson")
		
		
		
		.antMatchers("/products", "/products/"  , "/products/details/**" , "/products/page/**")
		.hasAnyAuthority("Admin","Salesperson" , "Editor" , "Shipper")
		
		
		
		.antMatchers("/products/**").hasAnyAuthority("Admin", "Editor" )
	
		.antMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson" , "Shipper")
		.antMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
		.antMatchers("/articles/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/menus/**").hasAnyAuthority("Admin", "Editor")
		.antMatchers("/settings/**" , "/countries/**" ,"/states/**").hasAuthority("Admin")
		.anyRequest().authenticated()
		
		
		
		
		
		.and()
	    .formLogin()
	    .loginPage("/login")
	    .usernameParameter("email")
	    .permitAll()
	    .and()
	    .logout()
           .permitAll()
           .and()
           .rememberMe().key("aBVefdddfdfddfdfdf1234648769")
           .tokenValiditySeconds(7 * 24 * 60 * 60);
		
		
		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**","/js/**","/webjars/**");
	}
	
	
	
	
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}
	
	
	
	
	
	
	
	
	
	
	
//	@Bean
//	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	
//		http.authorizeHttpRequests(
//				auth -> auth.anyRequest().permitAll()
//				);
//	
////		http.csrf().disable();
//		return http.build();
//		
//	}
	
	
	
	
}

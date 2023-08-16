package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserService;

@RestController
public class UserRestController {
@Autowired
	private UserService service;
	@PostMapping("/users/check-mail")
	public String cehckDuplicateEmail(@Param("id") Integer id,@Param("email") String email) {
	 return service.isEmailUnique(id,email) ? "OK" :"Duplicated";	
		
		
	}
}

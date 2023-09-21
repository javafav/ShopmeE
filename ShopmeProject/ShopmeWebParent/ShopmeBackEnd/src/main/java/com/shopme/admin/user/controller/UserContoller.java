package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.paging.PagingAndSortingParam;
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.exporter.UserCsvExporter;
import com.shopme.admin.user.exporter.UserExcelExporter;
import com.shopme.admin.user.exporter.UserExporterPdf;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller

public class UserContoller {

	@Autowired
	private UserService service;

	@GetMapping("/users")
	public String listFirstPage() {

		return "redirect:/users/page/1?sortField=firstName&sortDir=asc";
	}

//	@GetMapping("/users")
//	public String lisFirstPage(Model model) {
//		Page<User> pages = service.listByPageNum(1, "firstName", "asc");
//		List<User> listUsers = pages.getContent();
//
//		model.addAttribute("listUsers", listUsers);
//		// listUsers.forEach(user ->System.out.println(user));
//		return "users";
//	}

	@GetMapping("users/page/{pageNum}")

	public String listByPage(
			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable int pageNum, Model model) {
//		System.out.println("sortField" + sortField);
//		System.out.println("sortDir)" + sortDir);
		service.listByPageNum(pageNum, helper);

		return "users/users";

	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.getAllRoles();

		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {

		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);

			String uploadDir = "user-photos/" + savedUser.getId();

			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());

		} else {
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			service.save(user);
		}

		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		return getAffectedOrUpdateUserURL(user);
	}

	private String getAffectedOrUpdateUserURL(User user) {
		String firstPartofEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword=" + firstPartofEmail;
	}

	@GetMapping("/users/edit/{id}")
	public String updateUserDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

		User user;
		try {

			user = service.getUserById(id);

			model.addAttribute("user", user);
			// System.out.println((user.getPhotos()));
			List<Role> listRoles = service.getAllRoles();
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", " Edit User (ID " + id + " )");

			return "users/user_form";
		} catch (UserNotFoundException ex) {
			// TODO Auto-generated catch block

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {

		User user;
		try {
			user = service.getUserById(id);
			service.deleteUser(user);
			String userPhotosDir = "user-photos/" + id;
			AmazonS3Util.removeFolder(userPhotosDir);
			
			
			redirectAttributes.addFlashAttribute("message",
					"User:  " + user.getFirstName() + " ( with ID :" + user.getId() + " ) has deleted successfully");
			return "redirect:/users";

		} catch (UserNotFoundException ex) {
			// TODO Auto-generated catch block

			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}

	}

	@GetMapping("users/{id}/status/{status}")
	public String changeUserStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean status,
			RedirectAttributes redirectAttributes) {

		String messageStatus = status ? "Enabled" : "Disabled";
		String message = "User   (ID : " + id + " ) has been  " + messageStatus + " successfuly !";
		redirectAttributes.addFlashAttribute("message", message);
		service.updateUserStatus(id, status);
		return "redirect:/users";

	}

	@GetMapping("/users/export/csv")
	public void expoertToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();

		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/excel")
	public void expoertToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}

	@GetMapping("/users/export/pdf")
	public void expoertTozDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserExporterPdf exporter = new UserExporterPdf();
		exporter.export(listUsers, response);
	}
}

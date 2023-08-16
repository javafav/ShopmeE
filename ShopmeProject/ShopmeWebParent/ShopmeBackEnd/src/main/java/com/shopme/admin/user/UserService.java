package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
public class UserService {
	
	public static final int USER_PER_PAGE = 4;
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void listByPageNum(int pageNum,PagingAndSortingHelper helper){
	helper.listEntites(pageNum, USER_PER_PAGE, userRepo);
	}
	
	public User getUserByEmail(String email) {
		 return userRepo.getUserByEmail(email);
		
		
	}
	
	public List<User> listAll() {

		return (List<User>) userRepo.findAll(Sort.by("firstName").ascending());

	}

	public List<Role> getAllRoles() {

		return (List<Role>) roleRepository.findAll();

	}

	public User save(User user) {

		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user);

			}
		} else {
			encodePassword(user);

		}
		return userRepo.save(user);

	}

	private void encodePassword(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));

	}
	public User updateAccount(User userInForm) {
		User userInDB = userRepo.findById(userInForm.getId()).get();
		
		if (!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if (userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		return userRepo.save(userInDB);
	}
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		if (userByEmail == null)
			return true;
		boolean isCreatingNew = (id == null);
		if (isCreatingNew) {
			if (userByEmail != null)
				return false;

		} else {
			if (userByEmail.getId() != id) {
				return false;
			}

		}
		return true;
	}

	public User getUserById(Integer id) throws UserNotFoundException {

		try {
			User user = userRepo.findById(id).get();
			return user;
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("No User Exist with Given ID " + id);

		}

	}

	public void deleteUser(User user) {
		userRepo.delete(user);

	}

	public void updateUserStatus(Integer id, boolean changeSatatus) {
		userRepo.chnageUserStatus(id, changeSatatus);

	}

}

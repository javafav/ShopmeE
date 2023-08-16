package com.shopme.admin.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shopme.admin.paging.SearchRepository;
import com.shopme.common.entity.User;

@Repository
public interface UserRepository extends  SearchRepository<User, Integer> {

//	@Query("SELECT user FROM User WHERE user.email=:email")
	public User getUserByEmail(String email );
	
	@Query("UPDATE User  u SET u.enabled = ?2 WHERE u.id= ?1 ")
	@Modifying
	public void chnageUserStatus(Integer id,boolean changeStatus);
	
	@Query("SELECT u FROM User  u WHERE CONCAT(u.id, ' ' , u.email, ' ' , u.firstName, ' ' ,u.lastName) LIKE %?1%")
//	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
//			+ " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword,Pageable pageable);
	
	
}

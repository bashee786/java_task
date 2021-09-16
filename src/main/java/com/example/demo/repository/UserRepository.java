package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

	
	@Query(value = "select * from admin_test.users where email=:username", nativeQuery = true)
	UserEntity findByEmail(String username);

	@Query(value = "select email from admin_test.users where email=:email", nativeQuery = true)
	String findByEmailId(String email);
}

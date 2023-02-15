package edu.uclm.serverdielectro.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.serverdielectro.model.User;

public interface UserDAO extends JpaRepository <User, String>{

	User findByUsernameAndPassword(String username, String password);

	User findByTokenPass(String tokenpass);

	User findByUsername(String username);

	List <User> findAll();
	
	User findByUsernameAndEmail(String username, String email);

	User findByEmail(String email);

	void deleteByUsername(String username);

}
package com.pcsma.project;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
	
	public User findByEmail(String email);
}

package com.hotspot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
}

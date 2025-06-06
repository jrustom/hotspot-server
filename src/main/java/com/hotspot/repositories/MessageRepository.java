package com.hotspot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.model.Message;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
}

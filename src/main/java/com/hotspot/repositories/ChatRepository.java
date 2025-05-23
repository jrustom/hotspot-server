package com.hotspot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.model.Message;

@Repository
public interface ChatRepository extends MongoRepository<Message, Integer> {
}

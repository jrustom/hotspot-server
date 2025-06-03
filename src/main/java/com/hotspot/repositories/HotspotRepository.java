package com.hotspot.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hotspot.model.Hotspot;

@Repository
public interface HotspotRepository extends MongoRepository<Hotspot, String> {
}

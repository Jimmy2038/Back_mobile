package com.example.Fiaraamidy.repository;

import com.example.Fiaraamidy.entites.Discussion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DiscussionRepository extends MongoRepository<Discussion,String> {
    Optional<Discussion> findByUserIdUser(String id);
}

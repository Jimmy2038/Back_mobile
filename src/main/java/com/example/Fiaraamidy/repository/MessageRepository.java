package com.example.Fiaraamidy.repository;

import com.example.Fiaraamidy.entites.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {
    Optional<Message> findById(String id);
    List<Message> findByUserFromIdUserAndUserToIdUserOrUserFromIdUserAndAndUserToIdUserOrderByDate(String from,String to,String to1,String from1);

}



//findByUserFromIdUtilisateurAndUserToIdUtilisateurOrUserFromIdUtilisateurAndAndUserToIdUtilisateurOrderByDate

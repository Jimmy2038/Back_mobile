package com.example.Fiaraamidy.service;

import com.example.Fiaraamidy.entites.Message;
import com.example.Fiaraamidy.entites.Utilisateur;
import com.example.Fiaraamidy.model.User;
import com.example.Fiaraamidy.repository.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private MessageRepository messageRepository;
    private UtilisateurService utilisateurService;

    public List<Message> getAll(){
        return this.messageRepository.findAll();
    }
    public List<Message> getDiscu(String user1, String user2){
        return this.messageRepository.findByUserFromIdUserAndUserToIdUserOrUserFromIdUserAndAndUserToIdUserOrderByDate(user1,user2,user2,user1);
    }

    public Message insert(Message message) {

        message.setDate(LocalDateTime.now());

        Utilisateur to=utilisateurService.getById(Integer.parseInt(message.getUserTo().getIdUser()));
        Utilisateur from=utilisateurService.getById(Integer.parseInt(message.getUserFrom().getIdUser()));
        message.setUserTo(User.builder()
                .idUser(Integer.toString(to.getIdUtilisateur()))
                .pseudo(to.getPseudo()).build());
        message.setUserFrom(User.builder()
                .idUser(Integer.toString(from.getIdUtilisateur()))
                .pseudo(from.getPseudo()).build());
        return this.messageRepository.save(message);
    }


}

package com.example.Fiaraamidy.service;

import com.example.Fiaraamidy.entites.Annonce;
import com.example.Fiaraamidy.entites.Discussion;
import com.example.Fiaraamidy.entites.Message;
import com.example.Fiaraamidy.entites.Utilisateur;
import com.example.Fiaraamidy.model.User;
import com.example.Fiaraamidy.repository.DiscussionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DiscussionService {
    private DiscussionRepository discussionRepository;
    private AnnonceService annonceService;
    private UtilisateurService utilisateurService;
    private MessageService messageService;

    public boolean userExistsInList(List<User> list,User userToCheck) {
        if (list != null) {
            for (User user : list) {
                if (user.getIdUser().equals(userToCheck.getIdUser())) {
                    return true; // L'utilisateur existe déjà dans la liste
                }
            }
        }
        return false; // L'utilisateur n'existe pas dans la liste
    }
    public void addDiscussion(User user1,User user2){
        Optional<Discussion> discussionOptional = discussionRepository.findByUserIdUser(user1.getIdUser());
        if (discussionOptional.isPresent()) {
            Discussion discussion = discussionOptional.get();
            boolean exist=userExistsInList(discussion.getListUser(),user2);
            if(exist==false){
                discussion.getListUser().add(user2);
                this.discussionRepository.save(discussion);
            }
        }else{
            List<User> list=new ArrayList<>();
            list.add(user2);
            Discussion discussion=Discussion.builder()
                    .user(user1)
                    .listUser(list).build();
            this.discussionRepository.save(discussion);
        }
    }
    public void contacter(String idUser,int idAnnonce){
        Annonce annonce=this.annonceService.getById(idAnnonce);
        Utilisateur u1=this.utilisateurService.getById(Integer.parseInt(idUser));
        Utilisateur u2=this.utilisateurService.getById(annonce.getIdUtilisateur());
        User client=User.builder()
                .idUser(Integer.toString(u1.getIdUtilisateur()))
                .pseudo(u1.getPseudo()).build();
        User vendeur=User.builder()
                .idUser(Integer.toString(u2.getIdUtilisateur()))
                .pseudo(u2.getPseudo()).build();
        Message message=Message.builder()
                .userFrom(vendeur)
                .userTo(client)
                .content("Bonjour,je suis interessee par la "+annonce.getModel().getMarque().getNomMarque()+" "+annonce.getModel().getNomModel())
                .date(LocalDateTime.now()).build();
        this.messageService.insert(message);
        addDiscussion(client,vendeur);
        addDiscussion(vendeur,client);
    }

    public Discussion getByIdUser(String id){
        Optional<Discussion> discussionOptional = discussionRepository.findByUserIdUser(id);
        if (discussionOptional.isPresent()) {
             return discussionOptional.get();
        }else{
            try {
                throw new Exception("Discussion not found");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

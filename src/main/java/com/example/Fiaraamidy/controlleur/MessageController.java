package com.example.Fiaraamidy.controlleur;

import com.example.Fiaraamidy.entites.Discussion;
import com.example.Fiaraamidy.model.ChatNotification;
import com.example.Fiaraamidy.service.DiscussionService;
import com.example.Fiaraamidy.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.example.Fiaraamidy.entites.Message;

import java.util.List;

@RestController
@AllArgsConstructor
public class MessageController {
    private MessageService messageService;
    private DiscussionService discussionService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(path="message/get")
    public List<Message> get(){
        return this.messageService.getAll();
    }

    @GetMapping(path="message/discu/{user1}/{user2}")
    public List<Message> getDiscu(@PathVariable String user1,@PathVariable String user2){
        return this.messageService.getDiscu(user1,user2);
    }

    @PostMapping(path="message/insert")
    public String insert(@RequestBody Message message){
        try{
            this.messageService.insert(message);
            return "Message envoye";
        } catch (Exception e){
            return e.getMessage();
        }
    }

    @PostMapping(path="message/contacter/{idUser}/{idAnnonce}")
    public String contacter(@PathVariable String idUser,@PathVariable int idAnnonce){
        try{
            this.discussionService.contacter(idUser,idAnnonce);
            return "okeee";
        } catch (Exception e){
            return e.getMessage();
        }
    }

    @GetMapping(path="message/listUser/{idUser}")
    public Discussion get(@PathVariable String idUser){
        return this.discussionService.getByIdUser(idUser);
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload Message message) {

        Message savedMsg=messageService.insert(message);
        messagingTemplate.convertAndSendToUser(
                message.getUserFrom().getIdUser(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getContent(),
                        savedMsg.getUserTo(),
                        savedMsg.getUserFrom()
                )
        );
    }
}

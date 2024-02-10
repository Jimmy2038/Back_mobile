package com.example.Fiaraamidy.entites;

import com.example.Fiaraamidy.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Discussion {
    @Id
    private String id;
    private User user;
    private List<User> listUser;
}

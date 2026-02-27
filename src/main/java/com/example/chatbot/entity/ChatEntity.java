package com.example.chatbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat")
@Getter
@Setter
public class ChatEntity extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    @OneToMany(mappedBy = "chatEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messageEntities = new ArrayList<>();


}

package com.example.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chat")
@Getter
@Setter
public class ChatEntity extends BaseEntity {

    private String name;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

}

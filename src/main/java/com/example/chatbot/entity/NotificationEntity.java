package com.example.chatbot.entity;


import com.example.chatbot.entity.enums.NotificationTypeEnum;
import com.example.chatbot.entity.enums.PersonNotificationRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class NotificationEntity extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private PersonNotificationRole personNotificationRole;

    private String subject;

    private String description;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private PersonEntity personEntity;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private PersonEntity sender;

    private boolean read;



}

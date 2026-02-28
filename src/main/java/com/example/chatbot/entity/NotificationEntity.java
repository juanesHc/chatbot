package com.example.chatbot.entity;

import com.example.chatbot.entity.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class NotificationEntity extends BaseEntity{

    public String subject;

    public String description;

    @ManyToOne
    @JoinColumn(name = "person_id")
    public PersonEntity personEntity;

    public boolean read;



}

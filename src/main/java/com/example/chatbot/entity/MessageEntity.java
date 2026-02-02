package com.example.chatbot.entity;

import com.example.chatbot.entity.enums.MessageRoleEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="message")
@Getter
@Setter
public class MessageEntity extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private MessageRoleEnum messageRoleEnum;

    private String messageContent;

    private ChatEntity chatEntity;

}

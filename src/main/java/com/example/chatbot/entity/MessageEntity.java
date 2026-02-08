package com.example.chatbot.entity;

import com.example.chatbot.entity.enums.MessageRoleEnum;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name="chat_id")
    private ChatEntity chatEntity;

}

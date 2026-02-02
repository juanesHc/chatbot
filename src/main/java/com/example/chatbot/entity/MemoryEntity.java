package com.example.chatbot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="memory")
@Getter
@Setter
public class MemoryEntity extends BaseEntity{

    private String key;

    private String value;

    private Integer priority;

    @ManyToOne
    @JoinColumn(name="chat_id")
    private ChatEntity chatEntity;



}

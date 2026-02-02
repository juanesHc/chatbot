package com.example.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="memory")
@Getter
@Setter
public class MemoryEntity {

    private String key;

    private String value;

    private Integer priority;

    private ChatEntity chatEntity;



}

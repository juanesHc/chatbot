package com.example.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="person")
@Getter
@Setter
public class PersonEntity extends BaseEntity{

    private String name;

    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

}

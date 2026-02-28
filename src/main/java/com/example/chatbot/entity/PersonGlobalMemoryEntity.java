package com.example.chatbot.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="person_global_memory")
@Getter
@Setter
@NoArgsConstructor
public class PersonGlobalMemoryEntity extends BaseEntity{

    private String key;

    private String value;

    private Integer priority;

    @Column(name = "frequency")
    private Integer frequency = 1;

    @ManyToOne
    @JoinColumn(name="person_id")
    private PersonEntity personEntity;

}

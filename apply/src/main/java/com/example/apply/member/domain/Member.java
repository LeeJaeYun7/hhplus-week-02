package com.example.apply.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Builder
    public Member(long id, String name){
        this.id = id;
        this.name = name;
    }

    public static Member of(long id, String name){
        return Member.builder()
                     .id(id)
                     .name(name)
                     .build();
    }
}
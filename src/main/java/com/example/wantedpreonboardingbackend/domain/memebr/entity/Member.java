package com.example.wantedpreonboardingbackend.domain.memebr.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    String email;

    @Column(name = "password",nullable = false)
    String encryptedPassword;

    @Builder
    private Member(String email, String encryptedPassword){
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

}

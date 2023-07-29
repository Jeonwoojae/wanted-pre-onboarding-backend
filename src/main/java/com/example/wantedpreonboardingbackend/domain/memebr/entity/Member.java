package com.example.wantedpreonboardingbackend.domain.memebr.entity;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
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

    @Column(name = "email", nullable = false)
    @Email
    String email;

    @Column(name = "password",nullable = false)
    String encrypted_password;

    @Builder
    private Member(String email, String encrypted_password){
        this.email = email;
        this.encrypted_password = encrypted_password;
    }

}

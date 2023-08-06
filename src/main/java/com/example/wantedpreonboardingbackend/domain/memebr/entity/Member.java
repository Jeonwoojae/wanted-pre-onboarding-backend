package com.example.wantedpreonboardingbackend.domain.memebr.entity;

import com.example.wantedpreonboardingbackend.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "password",nullable = false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "owner")
    private List<Post> posts = new ArrayList<>();

    @Builder
    protected Member(String email, String encryptedPassword){
        this.email = email;
        this.encryptedPassword = encryptedPassword;
    }

}

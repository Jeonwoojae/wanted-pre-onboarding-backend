package com.example.wantedpreonboardingbackend.domain.post.entity;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    Long id;

    @Column(nullable = false)
    String title;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    Member owner;

    @Builder
    public Post(String title, Member owner) {
        this.title = title;
        this.owner = owner;
    }
}

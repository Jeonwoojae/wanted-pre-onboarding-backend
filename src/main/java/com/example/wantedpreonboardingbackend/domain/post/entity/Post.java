package com.example.wantedpreonboardingbackend.domain.post.entity;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "Posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    @Builder
    public Post(String title, Member owner) {
        this.title = title;
        this.owner = owner;
    }

    public void editPost(PostRequestDto requestPostRequestDto) {
        this.title = requestPostRequestDto.getTitle();
    }
}

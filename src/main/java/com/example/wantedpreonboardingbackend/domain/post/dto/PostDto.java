package com.example.wantedpreonboardingbackend.domain.post.dto;

import com.example.wantedpreonboardingbackend.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    Long postId;
    String title;

    public PostDto(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
    }
}

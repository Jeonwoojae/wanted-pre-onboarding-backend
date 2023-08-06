package com.example.wantedpreonboardingbackend.domain.post.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {
    @NotBlank(message = "title can not be blank")
    String title;

    public PostDto(String title) {
        this.title = title;
    }
}

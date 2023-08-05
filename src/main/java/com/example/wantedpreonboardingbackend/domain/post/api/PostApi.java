package com.example.wantedpreonboardingbackend.domain.post.api;

import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.example.wantedpreonboardingbackend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PostApi {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Void> writeNewPost(@Valid @RequestBody PostDto requestPostDto,
                                             @RequestHeader("Authorization")String tokenHeader){
        String token = tokenHeader.substring(7);
        postService.writeNewPost(requestPostDto,token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

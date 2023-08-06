package com.example.wantedpreonboardingbackend.domain.post.api;

import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.example.wantedpreonboardingbackend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> searchPostsListPage(@RequestHeader("Authorization")String tokenHeader, Pageable pageable) {
        String accessToken = tokenHeader.substring(7);
        List<PostDto> response = postService.getPostsListPage(accessToken, pageable);

        return ResponseEntity.ok().body(response);
    }
}

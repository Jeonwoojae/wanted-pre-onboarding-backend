package com.example.wantedpreonboardingbackend.domain.post.api;

import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostRequestDto;
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
    public ResponseEntity<Void> writeNewPost(@Valid @RequestBody PostRequestDto requestPostRequestDto,
                                             @RequestHeader("Authorization")String tokenHeader){
        String token = tokenHeader.substring(7);
        postService.writeNewPost(requestPostRequestDto,token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> searchPostsListPage(@RequestHeader("Authorization")String tokenHeader, Pageable pageable) {
        String accessToken = tokenHeader.substring(7);
        List<PostDto> response = postService.getPostsListPage(accessToken, pageable);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> searchPostsListPage(@RequestHeader("Authorization")String tokenHeader,
                                                       @PathVariable("postId")Long postId) {
        String accessToken = tokenHeader.substring(7);
        PostDto currentPost = postService.findPostOne(postId,accessToken);

        return ResponseEntity.ok().body(currentPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostDto> editMyPost(@RequestHeader("Authorization")String tokenHeader,
                                              @PathVariable("postId")Long postId,
                                              @Valid @RequestBody PostRequestDto requestPostRequestDto) {
        String accessToken = tokenHeader.substring(7);
        PostDto editedPost = postService.editPost(postId,accessToken,requestPostRequestDto);

        return ResponseEntity.ok().body(editedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deleteMyPost(@RequestHeader("Authorization")String tokenHeader,
                                              @PathVariable("postId")Long postId) {
        String accessToken = tokenHeader.substring(7);
        postService.deletePost(postId,accessToken);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

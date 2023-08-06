package com.example.wantedpreonboardingbackend.domain.post.service;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.memebr.repository.MemberRepository;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostRequestDto;
import com.example.wantedpreonboardingbackend.domain.post.entity.Post;
import com.example.wantedpreonboardingbackend.domain.post.repository.PostRepository;
import com.example.wantedpreonboardingbackend.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Post writeNewPost(PostRequestDto dto, String token){
        String currentUserEmail = tokenProvider.getEmailFromToken(token);
        Member currentMember = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        Post newPost = Post.builder()
                .title(dto.getTitle())
                .owner(currentMember)
                .build();

        return postRepository.save(newPost);
    }

    public List<PostDto> getPostsListPage(String accessToken, Pageable pageable) {
        String email = tokenProvider.getEmailFromToken(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostDto> postDtoList = posts.map(PostDto::new).stream()
                .collect(Collectors.toList());

        return postDtoList;
    }

    public PostDto findPostOne(Long postId, String token){
        String currentUserEmail = tokenProvider.getEmailFromToken(token);
        Member currentMember = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        Post currentPost = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 게시글을 찾을 수 없습니다."));
        PostDto response = Optional.ofNullable(currentPost).map(PostDto::new)
                .orElseThrow(()->new RuntimeException("Post dto 변환 실패"));

        return response;
    }

    public PostDto editPost(Long postId, String token, PostRequestDto requestPostRequestDto) {
        String currentUserEmail = tokenProvider.getEmailFromToken(token);
        Member currentMember = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        Post currentPost = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 게시글을 찾을 수 없습니다."));

        // 게시글 권한 확인
        if (!currentMember.hasPermissionForPost(currentPost)){
            throw new AccessDeniedException("해당 게시글에 권한이 없습니다.");
        }

        // 게시글 수정
        currentPost.editPost(requestPostRequestDto);
        currentPost = postRepository.save(currentPost);

        PostDto response = Optional.ofNullable(currentPost).map(PostDto::new)
                .orElseThrow(()->new RuntimeException("Post dto 변환 실패"));

        return response;
    }

    public void deletePost(Long postId, String token) {
        String currentUserEmail = tokenProvider.getEmailFromToken(token);
        Member currentMember = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new EntityNotFoundException("해당하는 유저를 찾을 수 없습니다."));
        Post currentPost = postRepository.findById(postId)
                .orElseThrow(()->new EntityNotFoundException("해당하는 게시글을 찾을 수 없습니다."));

        // 게시글 권한 확인
        if (!currentMember.hasPermissionForPost(currentPost)){
            throw new AccessDeniedException("해당 게시글에 권한이 없습니다.");
        }

        // 게시글 삭제
        postRepository.delete(currentPost);
    }
}

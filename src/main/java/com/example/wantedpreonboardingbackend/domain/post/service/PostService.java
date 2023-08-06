package com.example.wantedpreonboardingbackend.domain.post.service;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.memebr.repository.MemberRepository;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.example.wantedpreonboardingbackend.domain.post.entity.Post;
import com.example.wantedpreonboardingbackend.domain.post.repository.PostRepository;
import com.example.wantedpreonboardingbackend.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Post writeNewPost(PostDto dto, String token){
        String currentUserEmail = tokenProvider.getEmailFromToken(token);
        Member currentMember = memberRepository.findByEmail(currentUserEmail)
                .orElseThrow(()->new NoSuchElementException("해당하는 유저를 찾을 수 없습니다."));
        Post newPost = Post.builder()
                .title(dto.getTitle())
                .owner(currentMember)
                .build();

        return postRepository.save(newPost);
    }
}

package com.example.wantedpreonboardingbackend.domain.post.service;

import com.example.wantedpreonboardingbackend.domain.memebr.entity.Member;
import com.example.wantedpreonboardingbackend.domain.memebr.repository.MemberRepository;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostRequestDto;
import com.example.wantedpreonboardingbackend.domain.post.entity.Post;
import com.example.wantedpreonboardingbackend.domain.post.repository.PostRepository;
import com.example.wantedpreonboardingbackend.global.error.exception.EntityNotFoundException;
import com.example.wantedpreonboardingbackend.global.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class PostServiceTest {
    @Mock
    PostRepository postRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;

    public static class TestRequestDto extends PostRequestDto {
        public TestRequestDto(String title){
            super(title);
        }
    }

    public static class TestMember extends Member {
        public TestMember(String email, String password){
            super(email,password);
        }
    }

    @Test
    public void testCreatePost() {
        // given
        String token = "THIS_IS_USER";
        String title = "게시글 명";
        PostRequestDto postRequestDto = new PostRequestDto(title);
        Member testMember = new TestMember("email123@naver.com", "12341234");


        when(tokenProvider.getEmailFromToken(token)).thenReturn("email123@naver.com");
        when(memberRepository.findByEmail("email123@naver.com")).thenReturn(Optional.ofNullable(testMember));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Object savedPost = invocation.getArgument(0);
            return savedPost;
        });

        // when
        Post savedPost = postService.writeNewPost(postRequestDto,token);

        // then
        assertEquals(title, savedPost.getTitle());
        assertEquals(testMember, savedPost.getOwner());
    }

    @Test
    public void testWriteNewPostUserNotFound() {
        // Arrange
        PostRequestDto postRequestDto = new TestRequestDto("test title");

        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntityNotFoundException.class,
                () -> postService.writeNewPost(postRequestDto, "dummyToken"));
    }

}
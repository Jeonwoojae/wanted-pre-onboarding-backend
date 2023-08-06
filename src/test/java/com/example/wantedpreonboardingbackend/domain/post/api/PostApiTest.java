package com.example.wantedpreonboardingbackend.domain.post.api;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestLoginDto;
import com.example.wantedpreonboardingbackend.domain.memebr.service.MemberService;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;

    private static final String TEST_USERNAME = "dnwo0719@naver.com";
    private static final String TEST_PASSWORD = "12341234";

    private String testToken;

    @BeforeEach
    public void setup() throws Exception {
        // 회원 가입 API를 통해 테스트용 사용자 생성
        RequestJoinMemberDto dto = RequestJoinMemberDto.createForTest(TEST_USERNAME, TEST_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)));

        // 로그인 API를 통해 테스트용 사용자 로그인 및 토큰 발급
        RequestLoginDto loginDto = new RequestLoginDto(TEST_USERNAME, TEST_PASSWORD);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        // 토큰 추출
        String tokenHeaderValue = result.andReturn().getResponse().getHeader("Authorization");
        if (tokenHeaderValue != null && tokenHeaderValue.startsWith("Bearer ")) {
            testToken = tokenHeaderValue.substring(7);
        }
    }

    @Nested
    @DisplayName("게시글 작성 API")
    class writePost {
        @Test
        public void testWriteNewPostEndpoint() throws Exception {
            PostDto postDto = new PostDto("Test Post Title");

            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                            .header("Authorization", "Bearer " + testToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회 API")
    class searchPostsPage {
        @Test
        public void testSearchPostsListPageEndpoint() throws Exception {

            // 게시글 작성
            PostDto postDto = new PostDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                            .header("Authorization", "Bearer " + testToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postDto)));

            // 게시글 조회
            mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

}
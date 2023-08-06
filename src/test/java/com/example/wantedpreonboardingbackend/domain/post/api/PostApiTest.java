package com.example.wantedpreonboardingbackend.domain.post.api;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestLoginDto;
import com.example.wantedpreonboardingbackend.domain.post.dto.PostRequestDto;
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

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_USERNAME = "dnwo0719@naver.com";
    private static final String TEST_PASSWORD = "12341234";
    private static final String TEST_NO_AUTH_USERNAME = "dnwo1234@naver.com";
    private static final String TEST_NO_AUTH_PASSWORD = "00000000";

    private String testAuthUserToken;
    private String testNoAuthUserToken;

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
            testAuthUserToken = tokenHeaderValue.substring(7);
        }

        // 회원 가입 API를 통해 테스트용 사용자2 생성
        dto = RequestJoinMemberDto.createForTest(TEST_NO_AUTH_USERNAME, TEST_NO_AUTH_PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // 로그인 API를 통해 테스트용 사용자2 로그인 및 토큰 발급
        loginDto = new RequestLoginDto(TEST_NO_AUTH_USERNAME, TEST_NO_AUTH_PASSWORD);
        result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)));

        // 토큰 추출
        tokenHeaderValue = result.andReturn().getResponse().getHeader("Authorization");
        if (tokenHeaderValue != null && tokenHeaderValue.startsWith("Bearer ")) {
            testNoAuthUserToken = tokenHeaderValue.substring(7);
        }
    }

    @Nested
    @DisplayName("게시글 작성 API")
    class writePost {
        @Test
        public void testWriteNewPostEndpoint() throws Exception {
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");

            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postRequestDto)))
                    .andExpect(MockMvcResultMatchers.status().isCreated());
        }
    }

    @Nested
    @DisplayName("게시글 목록 조회 API")
    class searchPostsPage {
        @Test
        public void testSearchPostsListPageEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 조회
            mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("특정 게시글 조회 API")
    class searchPostOne {
        @Test
        public void testSearchPostOneEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .header("Authorization", "Bearer " + testAuthUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 목록 조회
            String responseContent = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // 첫 번째 게시글 ID 추출
            long firstPostId = objectMapper.readTree(responseContent)
                    .get(0)
                    .get("postId")
                    .asLong();

            // 게시글 조회
            mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", firstPostId)
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("특정 게시글 수정 API")
    class editPostOne {
        @Test
        public void testEditPostOneEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .header("Authorization", "Bearer " + testAuthUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 목록 조회
            String responsePostsPage = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // 첫 번째 게시글 ID 추출
            long firstPostId = objectMapper.readTree(responsePostsPage)
                    .get(0)
                    .get("postId")
                    .asLong();

            // 게시글 수정
            PostRequestDto postEditDto = new PostRequestDto("Edited Post Title");
            String responseContent = mockMvc.perform(MockMvcRequestBuilders.put("/posts/{postId}", firstPostId)
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .content(objectMapper.writeValueAsString(postEditDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String editedPostTitle = objectMapper.readTree(responseContent)
                    .get("title")
                    .asText();

            assertEquals("Edited Post Title", editedPostTitle);
        }

        @Test
        public void testEditPostDeniedEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .header("Authorization", "Bearer " + testAuthUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 목록 조회
            String responsePostsPage = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // 첫 번째 게시글 ID 추출
            long firstPostId = objectMapper.readTree(responsePostsPage)
                    .get(0)
                    .get("postId")
                    .asLong();

            // 게시글 수정
            PostRequestDto postEditDto = new PostRequestDto("Edited Post Title");
            mockMvc.perform(MockMvcRequestBuilders.put("/posts/{postId}", firstPostId)
                            .header("Authorization", "Bearer " + testNoAuthUserToken)
                            .content(objectMapper.writeValueAsString(postEditDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("특정 게시글 삭제 API")
    class deletePostOne {
        @Test
        public void testDeletePostOneEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .header("Authorization", "Bearer " + testAuthUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 목록 조회
            String responsePostsPage = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // 첫 번째 게시글 ID 추출
            long firstPostId = objectMapper.readTree(responsePostsPage)
                    .get(0)
                    .get("postId")
                    .asLong();

            // 게시글 삭제
            mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", firstPostId)
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        public void testDeletePostDeniedEndpoint() throws Exception {

            // 게시글 작성
            PostRequestDto postRequestDto = new PostRequestDto("Test Post Title");
            mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                    .header("Authorization", "Bearer " + testAuthUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postRequestDto)));

            // 게시글 목록 조회
            String responsePostsPage = mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                            .header("Authorization", "Bearer " + testAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // 첫 번째 게시글 ID 추출
            long firstPostId = objectMapper.readTree(responsePostsPage)
                    .get(0)
                    .get("postId")
                    .asLong();

            // 게시글 삭제
            mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", firstPostId)
                            .header("Authorization", "Bearer " + testNoAuthUserToken)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }
}
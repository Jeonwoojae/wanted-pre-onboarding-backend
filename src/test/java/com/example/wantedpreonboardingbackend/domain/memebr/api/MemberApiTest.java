package com.example.wantedpreonboardingbackend.domain.memebr.api;

import com.example.wantedpreonboardingbackend.domain.memebr.dto.RequestJoinMemberDto;
import com.example.wantedpreonboardingbackend.domain.memebr.service.MemberService;
import jdk.jfr.Name;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberService memberService;

    private static final String TEST_USERNAME = "dnwo0719@naver.com";
    private static final String TEST_PASSWORD = "12341234";

    public void createMember(String email, String password){
        RequestJoinMemberDto dto = RequestJoinMemberDto.createForTest(email,password);
        memberService.createNewMember(dto);
    }

    @Nested
    @DisplayName("사용자 회원가입 API")
    class createMember {
        @Test
        @Name("Join Member Success")
        public void testJoinMemberWithValidData() throws Exception {
            // Given
            String requestBody = "{ \"email\": \"test@example.com\", \"password\": \"password123\" }";

            // When and Then
            mockMvc.perform(post("/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }

        @Test
        @Name("Join Member Fail Invalid Email Format")
        public void testJoinMemberWithInvalidEmailData() throws Exception {
            // Given
            String requestBody = "{ \"email\": \"invalid-email\", \"password\": \"password123\" }";

            // When and Then
            mockMvc.perform(post("/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Name("Join Member Fail Invalid Password Format")
        public void testJoinMemberWithInvalidPasswordData() throws Exception {
            // Given
            String requestBody = "{ \"email\": \"test@example.com\", \"password\": \"1234\" }";

            // When and Then
            mockMvc.perform(post("/members")
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("사용자 로그인 API")
    class loginMember {

        @Test
        @Name("Login Success")
        public void testLoginSuccess() throws Exception {
            //given
            RequestJoinMemberDto dto = RequestJoinMemberDto.createForTest(TEST_USERNAME,TEST_PASSWORD);
            memberService.createNewMember(dto);


            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\": \"" + TEST_USERNAME + "\", \"password\": \"" + TEST_PASSWORD + "\"}"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists("Authorization"));
        }

        @Test
        @Name("Login Fail")
        public void testLoginFailure() throws Exception {
            mockMvc.perform(post("/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\": \"wronguser\", \"password\": \"wrongpassword\"}"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @Name("Not Match Login Form")
        public void testLoginFormNotMatch() throws Exception{
            Assertions.assertThrows(InvalidParameterException.class, ()->{
                mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"wronguser\", \"encryptedPassword\": \"wrongpassword\"}"));
            });
        }
    }
}
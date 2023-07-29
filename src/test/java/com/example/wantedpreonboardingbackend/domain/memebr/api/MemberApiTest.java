package com.example.wantedpreonboardingbackend.domain.memebr.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testJoinMemberWithValidData() throws Exception {
        // Given
        String requestBody = "{ \"email\": \"test@example.com\", \"password\": \"password123\" }";

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testJoinMemberWithInvalidEmailData() throws Exception {
        // Given
        String requestBody = "{ \"email\": \"invalid-email\", \"password\": \"password123\" }";

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testJoinMemberWithInvalidPasswordData() throws Exception {
        // Given
        String requestBody = "{ \"email\": \"test@example.com\", \"password\": \"1234\" }";

        // When and Then
        mockMvc.perform(MockMvcRequestBuilders.post("/members")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
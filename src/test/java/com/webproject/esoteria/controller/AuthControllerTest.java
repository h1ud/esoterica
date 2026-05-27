package com.webproject.esoteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.esoteria.domain.dto.LoginRequestDTO;
import com.webproject.esoteria.domain.dto.LoginResponseDTO;
import com.webproject.esoteria.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserLoginSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("alice");
        request.setPassword("password1");
        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(Optional.of(new LoginResponseDTO("user-token", "Bearer", 7200, "alice", "USER")));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("user-token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testAdminLoginUsesSameEndpoint() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("admin");
        request.setPassword("admin123");
        when(authService.login(any(LoginRequestDTO.class)))
                .thenReturn(Optional.of(new LoginResponseDTO("admin-token", "Bearer", 7200, "admin", "ADMIN")));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("admin-token"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testLoginUnauthorized() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setUsername("alice");
        request.setPassword("wrong");
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}

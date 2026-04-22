package com.webproject.esoteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.service.userService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@WebMvcTest(userController.class)
public class userControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private userService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private userDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new userDTO();
        userDTO.setUsername("john");
        userDTO.setPassword_hash("hash");
        userDTO.setFirst_name("John");
        userDTO.setLast_name("Doe");
    }

    @Test
    void testSaveUser() throws Exception {
        when(userService.create(any(userDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/username")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/username/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/username/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testListUsers() throws Exception {
        when(userService.listAll()).thenReturn(List.of(userDTO));

        mockMvc.perform(get("/api/username"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].first_name").value("John"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        userDTO updatedUser = new userDTO();
        updatedUser.setUsername("jane");
        updatedUser.setPassword_hash("hash2");
        updatedUser.setFirst_name("Jane");
        updatedUser.setLast_name("Doe");
        when(userService.update(eq(1L), any(userDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/username/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first_name").value("Jane"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userService.update(eq(1L), any(userDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/username/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.delete(1L)).thenReturn(userDTO);

        mockMvc.perform(delete("/api/username/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userService.delete(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/username/1").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListUsers_Empty() throws Exception {
        when(userService.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/username"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testSaveUser_InvalidData() throws Exception {
        mockMvc.perform(post("/api/username")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": null}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser_InvalidData() throws Exception {
        mockMvc.perform(put("/api/username/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": null}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById_InvalidId() throws Exception {
        mockMvc.perform(get("/api/username/abc"))
                .andExpect(status().isBadRequest());
    }
}

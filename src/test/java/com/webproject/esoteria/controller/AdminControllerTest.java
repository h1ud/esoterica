package com.webproject.esoteria.controller;

import com.webproject.esoteria.domain.dto.userDTO;
import com.webproject.esoteria.service.JwtService;
import com.webproject.esoteria.service.userService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private userService userService;

    @Test
    void testListUsersWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListUsersWithToken() throws Exception {
        userDTO user = new userDTO();
        user.setUsername("admin");
        when(jwtService.isValidAuthorizationHeader("Bearer token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer token")).thenReturn(true);
        when(userService.listAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/admin/users").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    void testListUsersWithUserTokenIsForbidden() throws Exception {
        when(jwtService.isValidAuthorizationHeader("Bearer user-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer user-token")).thenReturn(false);

        mockMvc.perform(get("/api/admin/users").header("Authorization", "Bearer user-token"))
                .andExpect(status().isForbidden());
    }
}

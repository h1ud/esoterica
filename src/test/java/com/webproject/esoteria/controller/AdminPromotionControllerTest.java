package com.webproject.esoteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.esoteria.domain.dto.promotionDTO;
import com.webproject.esoteria.service.JwtService;
import com.webproject.esoteria.service.promotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminPromotionController.class)
public class AdminPromotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private promotionService promotionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListPromotionsWithoutTokenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/promotions"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListPromotionsWithUserTokenIsForbidden() throws Exception {
        when(jwtService.isValidAuthorizationHeader("Bearer user-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer user-token")).thenReturn(false);

        mockMvc.perform(get("/api/admin/promotions").header("Authorization", "Bearer user-token"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testListPromotionsWithAdminToken() throws Exception {
        promotionDTO promotion = buildPromotion();
        when(jwtService.isValidAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(promotionService.listAll()).thenReturn(List.of(promotion));

        mockMvc.perform(get("/api/admin/promotions").header("Authorization", "Bearer admin-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Promo signos de tierra"));
    }

    @Test
    void testCreatePromotionWithAdminToken() throws Exception {
        promotionDTO promotion = buildPromotion();
        when(jwtService.isValidAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(promotionService.create(any(promotionDTO.class))).thenReturn(promotion);

        mockMvc.perform(post("/api/admin/promotions")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.day_of_week").value("TUESDAY"));
    }

    @Test
    void testUpdatePromotionWithAdminToken() throws Exception {
        promotionDTO promotion = buildPromotion();
        promotion.setName("Promo martes actualizada");
        when(jwtService.isValidAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(promotionService.update(eq(1L), any(promotionDTO.class))).thenReturn(promotion);

        mockMvc.perform(put("/api/admin/promotions/1")
                        .header("Authorization", "Bearer admin-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promotion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Promo martes actualizada"));
    }

    @Test
    void testDeletePromotionWithAdminToken() throws Exception {
        when(jwtService.isValidAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(jwtService.isValidAdminAuthorizationHeader("Bearer admin-token")).thenReturn(true);
        when(promotionService.delete(1L)).thenReturn(buildPromotion());

        mockMvc.perform(delete("/api/admin/promotions/1").header("Authorization", "Bearer admin-token"))
                .andExpect(status().isNoContent());
    }

    private promotionDTO buildPromotion() {
        promotionDTO promotion = new promotionDTO();
        promotion.setId(1L);
        promotion.setName("Promo signos de tierra");
        promotion.setDay_of_week("TUESDAY");
        promotion.setZodiac_signs(List.of("TAURO", "VIRGO", "CAPRICORNIO"));
        promotion.setDescription("2 crepas + 2 frutas + 1 topping a eleccion");
        promotion.setPromo_price(30.0);
        promotion.setRequired_crepes(2);
        promotion.setRequired_fruits(2);
        promotion.setRequired_toppings(1);
        promotion.setTopping_options(List.of("MANJAR", "MIEL"));
        promotion.setActive(true);
        return promotion;
    }
}

package com.webproject.esoteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.esoteria.domain.dto.promotionDTO;
import com.webproject.esoteria.domain.dto.promotionQuoteRequest;
import com.webproject.esoteria.domain.dto.promotionValidationResponse;
import com.webproject.esoteria.service.promotionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(promotionController.class)
public class promotionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private promotionService promotionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListActivePromotions() throws Exception {
        promotionDTO promotion = new promotionDTO();
        promotion.setName("Promo signos de tierra");
        promotion.setActive(true);
        when(promotionService.listActive()).thenReturn(List.of(promotion));

        mockMvc.perform(get("/api/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void testValidatePromotion() throws Exception {
        promotionValidationResponse response = new promotionValidationResponse();
        response.setValid(true);
        response.setZodiac_sign("TAURO");
        response.setTotal(30.0);
        when(promotionService.validate(eq("TAURO"), any(LocalDate.class))).thenReturn(response);

        mockMvc.perform(get("/api/promotions/validate")
                        .param("sign", "TAURO")
                        .param("date", "2026-05-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.total").value(30.0));
    }

    @Test
    void testQuotePromotion() throws Exception {
        promotionQuoteRequest request = new promotionQuoteRequest();
        request.setZodiac_sign("TAURO");
        request.setTopping("MIEL");
        request.setDate("2026-05-26");

        promotionValidationResponse response = new promotionValidationResponse();
        response.setValid(true);
        response.setSelected_topping("MIEL");
        response.setTotal(30.0);
        when(promotionService.quote(any(promotionQuoteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/promotions/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.selected_topping").value("MIEL"))
                .andExpect(jsonPath("$.total").value(30.0));
    }

    @Test
    void testListTodayPromotions() throws Exception {
        promotionDTO promotion = new promotionDTO();
        promotion.setName("Promo signos de tierra");
        promotion.setPromo_price(30.0);
        when(promotionService.listForDate(any(LocalDate.class))).thenReturn(List.of(promotion));

        mockMvc.perform(get("/api/promotions/today").param("date", "2026-05-26"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Promo signos de tierra"));
    }
}

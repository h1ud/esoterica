package com.webproject.esoteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.esoteria.domain.dto.productDTO;
import com.webproject.esoteria.service.productService;
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
@WebMvcTest(productController.class)
public class productControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private productService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private productDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new productDTO();
        productDTO.setProduct_name("Test Product");
        productDTO.setPrice(100.0);
        productDTO.setActivation("2024-01-01");
        productDTO.setExpiration("2025-01-01");
    }

    @Test
    void testMakeProduct() throws Exception {
        when(productService.make(any(productDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Test Product"));
    }

    @Test
    void testListProduct() throws Exception {
        when(productService.listAll()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_name").value("Test Product"));
    }

    @Test
    void testSearchProduct_Success() throws Exception {
        when(productService.search(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Test Product"));
    }

    @Test
    void testSearchProduct_NotFound() throws Exception {
        when(productService.search(1L)).thenReturn(null);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        when(productService.delete(1L)).thenReturn(productDTO);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Test Product"));
    }

    @Test
    void testDeleteProduct_NotFound() throws Exception {
        when(productService.delete(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/products/1").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        productDTO updatedProduct = new productDTO();
        updatedProduct.setProduct_name("Updated Product");
        updatedProduct.setPrice(200.0);
        updatedProduct.setActivation("2024-02-01");
        updatedProduct.setExpiration("2025-02-01");
        when(productService.update(eq(1L), any(productDTO.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Updated Product"));
    }

    @Test
    void testUpdateProduct_NotFound() throws Exception {
        when(productService.update(eq(1L), any(productDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProductByName_Success() throws Exception {
        when(productService.searchByName("Test")).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/api/products/search").param("name", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product_name").value("Test Product"));
    }

    @Test
    void testSearchProductByName_EmptyResult() throws Exception {
        when(productService.searchByName("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products/search").param("name", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testListProduct_Empty() throws Exception {
        when(productService.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void testMakeProduct_InvalidInput() throws Exception {
        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"product_name\": null, \"price\": 100.0}"))
                .andExpect(status().isOk());
    }
}

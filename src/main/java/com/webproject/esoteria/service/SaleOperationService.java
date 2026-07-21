package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.SaleFinalyResponse;
import com.webproject.esoteria.domain.dto.SaleItemRequestDTO;
import com.webproject.esoteria.domain.dto.SaleRequestDTO;
import com.webproject.esoteria.domain.dto.SaleResponseDTO;
import com.webproject.esoteria.domain.entity.*;
import com.webproject.esoteria.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SaleOperationService {

    private final SaleOperationRepository saleOperationRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public SaleOperationService(SaleOperationRepository saleOperationRepository,
                                SaleDetailRepository saleDetailRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository) {
        this.saleOperationRepository = saleOperationRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    // ... dentro de executeSaleOperation ...
    @Transactional
    public SaleFinalyResponse executeSaleOperation(SaleRequestDTO request) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Normalizar paymentMethod a minúsculas para que cumpla con el CHECK constraint de la BD
        String paymentMethod = request.paymentMethod() != null ? request.paymentMethod().toLowerCase() : "efectivo";
        SaleOperation sale = new SaleOperation(user, paymentMethod);
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (SaleItemRequestDTO itemDto : request.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            totalCalculado = totalCalculado.add(itemSubtotal);

            // Crear detalle y asignarlo a la venta
            SaleDetail detail = new SaleDetail();
            detail.setProduct(product);
            detail.setSaleOperation(sale); // Vinculamos el objeto padre
            detail.setQuantity(itemDto.quantity());
            detail.setUnitPrice(product.getPrice());
            detail.setSubtotal(itemSubtotal);

            sale.getDetails().add(detail); // Agregamos a la lista
        }

        sale.setSubtotal(totalCalculado);
        sale.setTotal(totalCalculado);

        // Al guardar la venta, JPA guarda automáticamente los detalles en la lista
        SaleOperation savedSale = saleOperationRepository.save(sale);

        return new SaleFinalyResponse(
                savedSale.getId(),
                "PAGADO",
                savedSale.getTotal(),
                savedSale.getIssueDate()
        );
    }

    public List<SaleResponseDTO> getRecentSales() {
        List<SaleOperation> sales = saleOperationRepository.findAllByOrderByIssueDateDesc();
        return sales.stream()
                .map(this::toSaleResponseDTO)
                .collect(Collectors.toList());
    }

    private SaleResponseDTO toSaleResponseDTO(SaleOperation sale) {
        List<SaleResponseDTO.SaleItemResponseDTO> itemDTOs = sale.getDetails().stream()
                .map(detail -> new SaleResponseDTO.SaleItemResponseDTO(
                        detail.getId(),
                        detail.getProduct().getProductName(),
                        detail.getQuantity(),
                        detail.getUnitPrice(),
                        detail.getSubtotal()
                ))
                .collect(Collectors.toList());

        return new SaleResponseDTO(
                sale.getId(),
                sale.getUser().getUsername(),
                sale.getPaymentMethod(),
                sale.getPaymentStatus(),
                sale.getSubtotal(),
                sale.getTotal(),
                sale.getIssueDate(),
                itemDTOs
        );
    }}
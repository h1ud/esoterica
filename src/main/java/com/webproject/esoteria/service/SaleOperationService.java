package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.SaleFinalyResponse;
import com.webproject.esoteria.domain.dto.SaleItemRequestDTO;
import com.webproject.esoteria.domain.dto.SaleRequestDTO;
import com.webproject.esoteria.domain.dto.SaleResponseDTO;
import com.webproject.esoteria.domain.entity.*;
import com.webproject.esoteria.domain.entity.Promotion;
import com.webproject.esoteria.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SaleOperationService {

    private final SaleOperationRepository saleOperationRepository;
    private final SaleDetailRepository saleDetailRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PromotionRepository promotionRepository;

    public SaleOperationService(SaleOperationRepository saleOperationRepository,
                                SaleDetailRepository saleDetailRepository,
                                ProductRepository productRepository,
                                UserRepository userRepository,
                                PromotionRepository promotionRepository) {
        this.saleOperationRepository = saleOperationRepository;
        this.saleDetailRepository = saleDetailRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.promotionRepository = promotionRepository;
    }


    // ... dentro de executeSaleOperation ...
    @Transactional
    public SaleFinalyResponse executeSaleOperation(SaleRequestDTO request) {
        User user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        String paymentMethod = request.paymentMethod() != null ? request.paymentMethod().toLowerCase() : "efectivo";
        SaleOperation sale = new SaleOperation(user, paymentMethod);
        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleItemRequestDTO itemDto : request.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.quantity()));
            subtotal = subtotal.add(itemSubtotal);

            SaleDetail detail = new SaleDetail();
            detail.setProduct(product);
            detail.setSaleOperation(sale);
            detail.setQuantity(itemDto.quantity());
            detail.setUnitPrice(product.getPrice());
            detail.setSubtotal(itemSubtotal);

            sale.getDetails().add(detail);
        }


        BigDecimal discountAmount = BigDecimal.ZERO;
        if (request.promoCode() != null && !request.promoCode().isBlank()) {
            Promotion promotion = promotionRepository.findByCode(request.promoCode().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Código promocional no válido: " + request.promoCode()));

            if (!promotion.isActive()) {
                throw new RuntimeException("La promoción " + request.promoCode() + " no está activa");
            }

            if (promotion.getEndDate() != null && promotion.getEndDate().isBefore(java.time.LocalDateTime.now())) {
                throw new RuntimeException("La promoción " + request.promoCode() + " ha expirado");
            }


            BigDecimal discountRate = promotion.getDiscount().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            discountAmount = subtotal.multiply(discountRate);
        }

        BigDecimal total = subtotal.subtract(discountAmount);

        sale.setSubtotal(subtotal);
        sale.setDiscountAmount(discountAmount);
        sale.setTotal(total);
        sale.setPaymentStatus("pagado");

        // Documentos: asignar tipo y datos del cliente
        String docType = request.documentType() != null ? request.documentType() : "boleta_simple";
        sale.setDocumentType(docType);

        if ("boleta_dni".equals(docType) || "factura".equals(docType)) {
            sale.setClientDni(request.clientDni());
            sale.setClientName(request.clientName());
            if ("factura".equals(docType)) {
                sale.setClientBusinessName(request.clientBusinessName());
                sale.setClientAddress(request.clientAddress());
            }
        }

        SaleOperation savedSale = saleOperationRepository.save(sale);

        return new SaleFinalyResponse(
                savedSale.getId(),
                savedSale.getPaymentStatus(),
                savedSale.getSubtotal(),
                savedSale.getDiscountAmount(),
                savedSale.getTotal(),
                savedSale.getIssueDate(),
                savedSale.getDocumentType()
        );
    }

    @Transactional
    public void deleteSale(Long saleId) {
        SaleOperation sale = saleOperationRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (!"pendiente".equals(sale.getPaymentStatus())) {
            throw new IllegalStateException("Solo se pueden eliminar ventas en estado pendiente");
        }

        // Obtener el usuario actual y verificar permisos
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !sale.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("No tienes permiso para eliminar esta venta");
        }

        saleOperationRepository.delete(sale);
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
package com.webproject.esoteria.service;

import com.webproject.esoteria.domain.dto.promotionDTO;
import com.webproject.esoteria.domain.dto.promotionQuoteRequest;
import com.webproject.esoteria.domain.dto.promotionValidationResponse;
import com.webproject.esoteria.domain.entity.Promotion;
import com.webproject.esoteria.repository.promotionRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class promotionService {
    private final promotionRepository promotionRepository;

    public promotionService(promotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<promotionDTO> listActive() {
        return promotionRepository.findByActiveTrue().stream().map(this::toDto).toList();
    }

    public List<promotionDTO> listAll() {
        return promotionRepository.findAll().stream().map(this::toDto).toList();
    }

    public promotionDTO getById(Long id) {
        return promotionRepository.findById(id).map(this::toDto).orElse(null);
    }

    public promotionDTO create(promotionDTO dto) {
        Promotion promotion = new Promotion();
        applyPromotionFields(dto, promotion, true);
        return toDto(promotionRepository.save(promotion));
    }

    public promotionDTO update(Long id, promotionDTO dto) {
        return promotionRepository.findById(id)
                .map(existing -> {
                    applyPromotionFields(dto, existing, false);
                    return toDto(promotionRepository.save(existing));
                })
                .orElse(null);
    }

    public promotionDTO delete(Long id) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotionDTO deleted = toDto(promotion);
                    promotionRepository.delete(promotion);
                    return deleted;
                })
                .orElse(null);
    }

    public List<promotionDTO> listForDate(LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return promotionRepository.findByActiveTrueAndDayOfWeek(toStorageDay(targetDate.getDayOfWeek()))
                .stream()
                .map(this::toDto)
                .toList();
    }

    public promotionValidationResponse validate(String zodiacSign, LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        String normalizedSign = normalizeSign(zodiacSign);

        return promotionRepository.findByActiveTrueAndDayOfWeek(toStorageDay(targetDate.getDayOfWeek()))
                .stream()
                .filter(promotion -> containsValue(promotion.getZodiacSigns(), normalizedSign))
                .findFirst()
                .map(promotion -> validResponse(normalizedSign, null, promotion))
                .orElseGet(() -> invalidResponse(normalizedSign, "No hay promocion activa para este signo en la fecha indicada"));
    }

    public promotionValidationResponse quote(promotionQuoteRequest request) {
        if (request == null) {
            return invalidResponse(null, "La solicitud de promocion esta vacia");
        }

        LocalDate date = parseDate(request.getDate());
        promotionValidationResponse validation = validate(request.getZodiac_sign(), date);
        if (!validation.isValid()) {
            return validation;
        }

        String selectedTopping = normalizeValue(request.getTopping());
        Promotion promotion = toEntityReference(validation.getPromotion());

        // El topping se valida aqui, pero el descuento no se registra como venta todavia.
        if (!containsValue(promotion.getToppingOptions(), selectedTopping)) {
            return invalidResponse(
                    validation.getZodiac_sign(),
                    "El topping seleccionado no esta permitido para esta promocion"
            );
        }

        validation.setSelected_topping(selectedTopping);
        validation.setMessage("Promocion aplicada: combo fijo 2x30");
        validation.setTotal(validation.getPromotion().getPromo_price());
        return validation;
    }

    private void applyPromotionFields(promotionDTO dto, Promotion promotion, boolean creating) {
        if (dto == null) {
            throw new IllegalArgumentException("La promocion no puede estar vacia");
        }

        if (creating || dto.getName() != null) {
            promotion.setName(dto.getName());
        }
        if (creating || dto.getDay_of_week() != null) {
            promotion.setDayOfWeek(normalizeDayOfWeek(dto.getDay_of_week()));
        }
        if (creating || dto.getZodiac_signs() != null) {
            promotion.setZodiacSigns(joinNormalizedSigns(dto.getZodiac_signs()));
        }
        if (creating || dto.getDescription() != null) {
            promotion.setDescription(dto.getDescription());
        }
        if (creating || dto.getPromo_price() != null) {
            promotion.setPromoPrice(dto.getPromo_price() != null ? dto.getPromo_price() : 0.0);
        }
        if (creating || dto.getRequired_crepes() != null) {
            promotion.setRequiredCrepes(dto.getRequired_crepes() != null ? dto.getRequired_crepes() : 0);
        }
        if (creating || dto.getRequired_fruits() != null) {
            promotion.setRequiredFruits(dto.getRequired_fruits() != null ? dto.getRequired_fruits() : 0);
        }
        if (creating || dto.getRequired_toppings() != null) {
            promotion.setRequiredToppings(dto.getRequired_toppings() != null ? dto.getRequired_toppings() : 0);
        }
        if (creating || dto.getTopping_options() != null) {
            promotion.setToppingOptions(joinNormalizedValues(dto.getTopping_options()));
        }
        if (creating || dto.getActive() != null) {
            promotion.setActive(dto.getActive() != null ? dto.getActive() : true);
        }
    }

    private promotionValidationResponse validResponse(String zodiacSign, String selectedTopping, Promotion promotion) {
        promotionValidationResponse response = new promotionValidationResponse();
        response.setValid(true);
        response.setMessage("Promocion disponible");
        response.setZodiac_sign(zodiacSign);
        response.setSelected_topping(selectedTopping);
        response.setTotal(promotion.getPromoPrice());
        response.setPromotion(toDto(promotion));
        return response;
    }

    private promotionValidationResponse invalidResponse(String zodiacSign, String message) {
        promotionValidationResponse response = new promotionValidationResponse();
        response.setValid(false);
        response.setMessage(message);
        response.setZodiac_sign(zodiacSign);
        return response;
    }

    private promotionDTO toDto(Promotion promotion) {
        promotionDTO dto = new promotionDTO();
        dto.setId(promotion.getId());
        dto.setName(promotion.getName());
        dto.setDay_of_week(promotion.getDayOfWeek());
        dto.setZodiac_signs(splitValues(promotion.getZodiacSigns()));
        dto.setDescription(promotion.getDescription());
        dto.setPromo_price(promotion.getPromoPrice());
        dto.setRequired_crepes(promotion.getRequiredCrepes());
        dto.setRequired_fruits(promotion.getRequiredFruits());
        dto.setRequired_toppings(promotion.getRequiredToppings());
        dto.setTopping_options(splitValues(promotion.getToppingOptions()));
        dto.setActive(promotion.getActive());
        return dto;
    }

    private Promotion toEntityReference(promotionDTO dto) {
        Promotion promotion = new Promotion();
        promotion.setId(dto.getId() != null ? dto.getId() : 0);
        promotion.setName(dto.getName());
        promotion.setDayOfWeek(dto.getDay_of_week());
        promotion.setZodiacSigns(String.join(",", dto.getZodiac_signs()));
        promotion.setDescription(dto.getDescription());
        promotion.setPromoPrice(dto.getPromo_price());
        promotion.setRequiredCrepes(dto.getRequired_crepes());
        promotion.setRequiredFruits(dto.getRequired_fruits());
        promotion.setRequiredToppings(dto.getRequired_toppings());
        promotion.setToppingOptions(String.join(",", dto.getTopping_options()));
        promotion.setActive(dto.getActive());
        return promotion;
    }

    private String joinNormalizedSigns(List<String> values) {
        return values == null ? "" : String.join(",", values.stream().map(this::normalizeSign).toList());
    }

    private String joinNormalizedValues(List<String> values) {
        return values == null ? "" : String.join(",", values.stream().map(this::normalizeValue).toList());
    }

    private boolean containsValue(String csvValues, String expectedValue) {
        return splitValues(csvValues).stream().anyMatch(value -> value.equals(expectedValue));
    }

    private List<String> splitValues(String csvValues) {
        if (csvValues == null || csvValues.isBlank()) {
            return List.of();
        }

        return Arrays.stream(csvValues.split(","))
                .map(this::normalizeValue)
                .toList();
    }

    private LocalDate parseDate(String date) {
        return date == null || date.isBlank() ? LocalDate.now() : LocalDate.parse(date);
    }

    private String toStorageDay(DayOfWeek dayOfWeek) {
        return dayOfWeek.name();
    }

    private String normalizeDayOfWeek(String value) {
        String normalized = normalizeValue(value);
        return switch (normalized) {
            case "MONDAY", "LUNES" -> "MONDAY";
            case "TUESDAY", "MARTES" -> "TUESDAY";
            case "WEDNESDAY", "MIERCOLES" -> "WEDNESDAY";
            case "THURSDAY", "JUEVES" -> "THURSDAY";
            case "FRIDAY", "VIERNES" -> "FRIDAY";
            case "SATURDAY", "SABADO" -> "SATURDAY";
            case "SUNDAY", "DOMINGO" -> "SUNDAY";
            default -> throw new IllegalArgumentException("Dia de semana invalido: " + value);
        };
    }

    private String normalizeSign(String value) {
        String normalized = normalizeValue(value);
        return "PICIS".equals(normalized) ? "PISCIS" : normalized;
    }

    private String normalizeValue(String value) {
        if (value == null) {
            return "";
        }

        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();
    }
}

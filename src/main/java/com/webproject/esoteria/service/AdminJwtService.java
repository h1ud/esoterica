package com.webproject.esoteria.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminJwtService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String TOKEN_TYPE = "Bearer";
    private static final String ISSUER = "esoterica-admin";
    private static final Duration EXPIRATION = Duration.ofHours(2);

    private final ObjectMapper objectMapper;
    private final byte[] secret;

    public AdminJwtService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.secret = readEnv("ESOTERICA_ADMIN_JWT_SECRET", "esoterica-admin-local-secret-change-me")
                .getBytes(StandardCharsets.UTF_8);
    }

    public String createToken(String username, String roleName) {
        Instant now = Instant.now();
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // Claims minimos para identificar al admin y limitar la vida del token.
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", username);
        payload.put("role", roleName);
        payload.put("iss", ISSUER);
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", now.plus(EXPIRATION).getEpochSecond());

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String unsignedToken = encodedHeader + "." + encodedPayload;

        return unsignedToken + "." + encode(sign(unsignedToken));
    }

    public boolean isValidAuthorizationHeader(String authorizationHeader) {
        return resolveSubject(authorizationHeader).isPresent();
    }

    public Optional<String> resolveSubject(String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        if (token == null) {
            return Optional.empty();
        }

        return readVerifiedPayload(token).map(payload -> payload.path("sub").asText());
    }

    public long getExpirationSeconds() {
        return EXPIRATION.toSeconds();
    }

    public String getTokenType() {
        return TOKEN_TYPE;
    }

    public boolean isAdminRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            return false;
        }

        String normalizedRole = roleName.trim().toUpperCase();
        return "ADMIN".equals(normalizedRole) || "ROLE_ADMIN".equals(normalizedRole);
    }

    private Optional<JsonNode> readVerifiedPayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }

        // Recalcula la firma para rechazar tokens alterados.
        String unsignedToken = parts[0] + "." + parts[1];
        byte[] expectedSignature = sign(unsignedToken);
        byte[] receivedSignature;

        try {
            receivedSignature = Base64.getUrlDecoder().decode(parts[2]);
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }

        if (!MessageDigest.isEqual(expectedSignature, receivedSignature)) {
            return Optional.empty();
        }

        try {
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JsonNode payload = objectMapper.readTree(payloadJson);

            // El token solo es aceptado si fue emitido por este modulo, tiene rol admin y no expiro.
            boolean validIssuer = ISSUER.equals(payload.path("iss").asText());
            boolean validRole = isAdminRoleName(payload.path("role").asText());
            boolean notExpired = payload.path("exp").asLong(0) > Instant.now().getEpochSecond();

            return validIssuer && validRole && notExpired ? Optional.of(payload) : Optional.empty();
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

    private String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            return null;
        }

        String prefix = TOKEN_TYPE + " ";
        return authorizationHeader.startsWith(prefix) ? authorizationHeader.substring(prefix.length()).trim() : null;
    }

    private String encodeJson(Map<String, Object> content) {
        try {
            return encode(objectMapper.writeValueAsBytes(content));
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo crear el token de admin", ex);
        }
    }

    private String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret, HMAC_ALGORITHM));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("No se pudo firmar el token de admin", ex);
        }
    }

    private String readEnv(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}

package com.example.good_lodging_service.service;

import com.example.good_lodging_service.constants.ApiResponseCode;
import com.example.good_lodging_service.constants.CommonStatus;
import com.example.good_lodging_service.dto.request.AuthenticationRequestDTO;
import com.example.good_lodging_service.dto.request.IntrospectRequestDTO;
import com.example.good_lodging_service.dto.response.AuthenticationResponseDTO;
import com.example.good_lodging_service.dto.response.IntrospectResponseDTO;
import com.example.good_lodging_service.entity.Role;
import com.example.good_lodging_service.entity.User;
import com.example.good_lodging_service.exception.AppException;
import com.example.good_lodging_service.repository.InvalidatedTokenRepository;
import com.example.good_lodging_service.repository.RoleRepository;
import com.example.good_lodging_service.repository.UserRepository;
import com.example.good_lodging_service.security.AuthenticationToken;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    Long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${jwt.valid-duration}")
    Long VALID_DURATION;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO requestDTO) {
        User user = userRepository.findByUsernameAndStatus(requestDTO.getUsername(), CommonStatus.ACTIVE.getValue()).orElseThrow(
                () -> new AppException(ApiResponseCode.ENTITY_NOT_FOUND));
        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword()))
            throw new AppException(ApiResponseCode.INVALID_PASSWORD);
        List<String> roles = roleRepository.findAllByUserId(user.getId()).stream().map(Role::getName).toList();
        return AuthenticationResponseDTO.builder()
                .token(generateToken(AuthenticationToken.builder()
                        .userId(user.getId())
                        .roles(roles)
                        .build()))
                .build();
    }

    public IntrospectResponseDTO introspect(IntrospectRequestDTO requestDTO) {
        String token = requestDTO.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        return IntrospectResponseDTO.builder().valid(isValid).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.MINUTES).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!verified && expiration.after(new Date()))
            throw new AppException(ApiResponseCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ApiResponseCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(AuthenticationToken authenticationToken) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(authenticationToken.getUsername())
                .issueTime(new Date())
                .issuer("huce.edu.vn")
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", authenticationToken.getUserId())
                .claim("scope", buildScope(authenticationToken))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create JWT object", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(AuthenticationToken authentication) {
        StringJoiner scope = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(authentication.getRoles())) {
            authentication.getRoles().forEach(role -> {
                scope.add("ROLE_" + role);
            });
        }
        return scope.toString();
    }
}

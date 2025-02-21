package com.example.good_lodging_service.service;

import com.example.good_lodging_service.constants.ApiResponseCode;
import com.example.good_lodging_service.constants.CommonStatus;
import com.example.good_lodging_service.dto.request.AuthenticationRequestDTO;
import com.example.good_lodging_service.dto.request.IntrospectRequestDTO;
import com.example.good_lodging_service.dto.request.LogoutRequestDTO;
import com.example.good_lodging_service.dto.request.RefreshRequestDTO;
import com.example.good_lodging_service.dto.response.AuthenticationResponseDTO;
import com.example.good_lodging_service.dto.response.IntrospectResponseDTO;
import com.example.good_lodging_service.entity.InvalidatedToken;
import com.example.good_lodging_service.entity.User;
import com.example.good_lodging_service.exception.AppException;
import com.example.good_lodging_service.repository.InvalidatedTokenRepository;
import com.example.good_lodging_service.repository.RoleRepository;
import com.example.good_lodging_service.repository.UserRepository;
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
        return AuthenticationResponseDTO.builder()
                .token(generateToken(user))
                .authenticated(true)
                .build();
    }

    public IntrospectResponseDTO introspect(IntrospectRequestDTO requestDTO) throws ParseException, JOSEException {
        String token = requestDTO.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponseDTO.builder().valid(isValid).build();
    }

    public void logout(LogoutRequestDTO requestDTO) throws ParseException, JOSEException {
        try {
            var signedJWT = verifyToken(requestDTO.getToken(), true);
            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            invalidatedTokenRepository.save(new InvalidatedToken(jit, expireTime));
        } catch (AppException e) {
            log.error(ApiResponseCode.INVALID_TOKEN.getMessage());
            throw new AppException(ApiResponseCode.INVALID_TOKEN);
        }
    }

    public AuthenticationResponseDTO refreshToken(RefreshRequestDTO requestDTO) throws ParseException, JOSEException {
        var signedJWT = verifyToken(requestDTO.getToken(), true);
        String jit = signedJWT.getJWTClaimsSet().getJWTID();
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken(jit, expireTime);
        invalidatedTokenRepository.save(invalidatedToken);

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsernameAndStatus(username, CommonStatus.ACTIVE.getValue()).orElseThrow(
                () -> new AppException(ApiResponseCode.ENTITY_NOT_FOUND));
        return AuthenticationResponseDTO.builder().token(generateToken(user)).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiration = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if (!verified && expiration.after(new Date()))
            throw new AppException(ApiResponseCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ApiResponseCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .issuer("huce.edu.vn")
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.getId())
                .claim("scope", buildScope(user))
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

    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scope.add("ROLE_" + role.getName());
            });
        }
        return scope.toString();
    }
}

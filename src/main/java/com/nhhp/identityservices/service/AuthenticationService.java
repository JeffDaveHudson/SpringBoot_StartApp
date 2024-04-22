package com.nhhp.identityservices.service;

import com.nhhp.identityservices.dto.request.AuthenticationRequest;
import com.nhhp.identityservices.dto.request.IntrospectRequest;
import com.nhhp.identityservices.dto.response.AuthenticationResponse;
import com.nhhp.identityservices.dto.response.IntrospectResponse;
import com.nhhp.identityservices.entity.User;
import com.nhhp.identityservices.exception.AppException;
import com.nhhp.identityservices.exception.ErrorCode;
import com.nhhp.identityservices.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor // inject vao constructor voi nhung field Final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;

    // key nay de sign va giai ma trong jwt
    @NonFinal // dung de tranh inject vao constructor khi dung @RequiredArgsConstructor
    //@Value("${jwt.signerKey}") // doc bien tu file .yaml
    protected static final String SIGNER_KEY = "uBWpdcML6YTT4es1PvjR2HQM/TAaHRpVTpbqnAN6TyjqGDZwHsX5S59TOyZbyWOf";
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        // Giai ma token, (ma hoa bang thuat toan nao thi giai ma bang thuat toan day)
        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        // kiem tra xem token con han khong
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var isExpiryTime= expiryTime.after(new Date());

        // tien hanh verify
        var verified = signedJWT.verify(jwsVerifier);

        return IntrospectResponse.builder()
                .valid(verified && isExpiryTime)
                .build();
    }

    private String generateToken(User user){
        // 1. Tao header voi argument la thuat toan can ma hoa
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // 2. Tao payload
        // Truoc khi tao payload, ta can tao claim
        // cac data trong body goi la claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // dai dien cho user dang nhap
                .issuer("nhhp.com") // xac dinh token duoc issue tu dau, thuong la domain cua service
                .issueTime(new Date()) // lay thoi gian hien tai
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // xac dinh thoi han ton tai cua token
                .claim("scope", buildScopeRole(user))
                .build();

        // Tao payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // convert sang json vi payload luu data dang json

        // --- can 2 argument: Header and Payload ---
        JWSObject jwsObject = new JWSObject(header, payload);

        // 3. signature
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes())); // thuat toan de sign
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println("cannot create token");
            throw new RuntimeException(e);
        }
    }

    private String buildScopeRole (User user){
        StringJoiner stringJoiner = new StringJoiner(" ");

        // luu thong tin role va permission vao jwt
        if(!CollectionUtils.isEmpty(user.getRoles()))
           user.getRoles().forEach(role -> {
               stringJoiner.add("ROLE_"+role.getName()); // add role vao list scope

               if(!CollectionUtils.isEmpty(role.getPermissions()))
                   role.getPermissions().forEach(permission -> {
                       stringJoiner.add(permission.getName()); // add permission vao list scope
               });
           });

        return stringJoiner.toString();
    }
}

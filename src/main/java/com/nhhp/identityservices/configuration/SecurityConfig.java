package com.nhhp.identityservices.configuration;

import com.nhhp.identityservices.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //enable kha nang kiem tra role cua 1 user truoc khi truy cap 1 method trong service. O tren method, ta se su dung @PreAuthorize hoac @PostAuthorize
public class SecurityConfig {

    @Value("${jwt.signerKey}")
    private String signerKey;

    // cau hinh xem domain nao duoc nguoi dung voi vai tro tuong ung su dung
    // vd: chi admin moi vao duoc domain voi chuc nang delete user,
    //     chi user moi vao domain de xem thong tin, nguoi la khong truy cap duoc
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, "/users").permitAll() // cho phep bat ky nguoi dung nao cung co the truy cap duoc "/users"
                        .requestMatchers(HttpMethod.POST, "/auth/token", "/auth/introspect").permitAll() // tuong tu nhu tren
                        // command line dong duoi vi da dung @@EnableMethodSecurity
                        //.requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())// chi cho phep role la admin su dung phuong thuc nay
                        .anyRequest().authenticated()); // secure nhung domain con lai (khong cho nguoi bat ky su dung)

        //
        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())
                                                         .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // set up exception cua loi Unauthenicated

        );

        // khi cau hinh Security, mac dinh csrf() duoc enable de tranh attack tu ben ngoai
        // Trong app nay, ta khong can thiet => disable no di
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    // custome lai prefix
    // vd: neu khong custome  se in ra SCOPE_ADMIN
    //     custome se in ra ROLE_ADMIN
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}

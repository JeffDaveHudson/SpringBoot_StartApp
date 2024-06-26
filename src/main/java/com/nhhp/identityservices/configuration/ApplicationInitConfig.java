package com.nhhp.identityservices.configuration;

import com.nhhp.identityservices.entity.User;
import com.nhhp.identityservices.enums.Role;
import com.nhhp.identityservices.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    // trong class nay, ta se tao 1 chuc nang giup tao 1 user co role la ADMIN
    // khi init app

    @Bean

    // dieu kien khoi tao bean dua tren property nao do. trong truong hop nay, ta muon unit test va connect h2database nhung
    // khong muon khoi tao bean nay, chi khi start ung dung va connect toi mysql thi moi khoi tao bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                var role = new HashSet<String>();
                role.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
// --                        .roles(role)
                        .build();

                userRepository.save(user);
                log.warn("admin init");
            }
        };
    }
}

package com.company.go.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "userProvider")
public class ModelAuditorConfig {
    @Bean
    @Lazy
    public AuditorAware<String> userProvider(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication == null ? "System" : authentication.getName();
        return () -> Optional.ofNullable(principal);
    }
}

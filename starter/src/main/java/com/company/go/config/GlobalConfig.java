package com.company.go.config;

import com.company.go.application.port.in.global.LoginUserUseCase;
import com.company.go.application.service.global.UserService;
import com.company.go.domain.global.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class GlobalConfig {
    @Bean
    @SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    User getUser(UserService userService){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LoginUserUseCase.LoginUserModel userModel =
                new LoginUserUseCase.LoginUserModel(userDetails.getUsername(), userDetails.getPassword());
        return userService.loginUser(userModel);
    }
}

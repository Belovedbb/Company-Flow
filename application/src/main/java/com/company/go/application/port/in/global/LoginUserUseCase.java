package com.company.go.application.port.in.global;

import com.company.go.domain.global.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public interface LoginUserUseCase {

    User loginUser(LoginUserModel login);

    @Getter
    @Setter
    @AllArgsConstructor
    class LoginUserModel{
        @NotEmpty
        @Email
        String email;
        @NotEmpty
        String password;

    }


}

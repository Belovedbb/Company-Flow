package com.company.go.application.service.global;


import com.company.go.application.port.Mapper;
import com.company.go.application.port.in.global.LoginUserUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.domain.global.Constants;
import com.company.go.domain.global.User;
import lombok.SneakyThrows;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.go.application.port.Mapper.convert;

@Service
public class UserService implements RegisterUserUseCase, LoginUserUseCase, UserDetailsService {

    private UpdateUserPort storeAdapter;

    public UserService(UpdateUserPort storeAdapter) {
        this.storeAdapter = storeAdapter;
    }

    @SneakyThrows({SQLException.class, IOException.class})
    @Override
    public boolean storeUser(RegisterUserModel model) {
        storeAdapter.saveUser(model.toUser());
        return false;
    }

    @Override
    public List<RegisterUserModel> getAllUsers() {
        return storeAdapter.getAllUsers().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public RegisterUserModel getUser(Long id) {
        User user = storeAdapter.getUser(id);
        return convert(user);
    }

    @Override
    public User loginUser(LoginUserModel login) {
        return storeAdapter.findUserByEmail(login.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = storeAdapter.findUserByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("user does not exist");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection<Constants.Roles> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }


}

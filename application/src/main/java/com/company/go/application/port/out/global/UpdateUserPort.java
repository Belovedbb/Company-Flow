package com.company.go.application.port.out.global;

import com.company.go.domain.global.User;

import java.util.List;

public interface UpdateUserPort {

    User findUserByEmail(String email);

    void saveUser(User user);

    List<User> getAllUsers();

    User getUser(Long id);

    boolean updateUser(Long id, User currentUser);

}

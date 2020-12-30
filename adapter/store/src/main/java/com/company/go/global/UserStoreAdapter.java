package com.company.go.global;

import com.company.go.StoreUtilities;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.archive.mapper.StaffMapper;
import com.company.go.archive.staff.StaffEntity;
import com.company.go.domain.global.User;
import com.company.go.global.mapper.UserMapper;
import com.company.go.global.repo.UserRepository;
import com.company.go.inventory.mapper.ProductMapper;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class UserStoreAdapter implements UpdateUserPort {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepo;

    @Autowired
    public UserStoreAdapter(BCryptPasswordEncoder passwordEncoder, UserRepository userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    @Override
    public User findUserByEmail(String email) {
        UserEntity userEntity = userRepo.findUserByEmail(email);
        if(SecurityContextHolder.getContext().getAuthentication() != null &&
                email.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
            StoreUtilities.setUser(userEntity);
        }
        return UserMapper.mapUserEntityToUserDomain(userEntity);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.saveUser(UserMapper.mapUserDomainToUserEntity(user));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getAllUsers().stream()
                .map(UserMapper::mapUserEntityToUserDomain).collect(Collectors.toList());
    }

    @Override
    public User getUser(Long id) {
        return UserMapper.mapUserEntityToUserDomain(userRepo.getUser(id));
    }

    @Override
    public boolean updateUser(Long id, User currentUser) {
        try {
           UserEntity user = UserMapper.mapUserDomainToUserEntity(currentUser);
            userRepo.updateUser(user);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }


}

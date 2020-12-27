package com.company.go.global.mapper;

import com.company.go.Utilities;
import com.company.go.domain.global.User;
import com.company.go.global.Constants;
import com.company.go.global.RoleEntity;
import com.company.go.global.UserEntity;

import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

public class UserMapper {
    private static final Map<Constants.Status, com.company.go.domain.global.Constants.Status> statusMapping ;
    private static final Map<Constants.Access, com.company.go.domain.global.Constants.Access> accessMapping ;
    private static final Map<Constants.Roles, com.company.go.domain.global.Constants.Roles> rolesMapping ;

    static {
        statusMapping = new EnumMap<>(Constants.Status.class);
        accessMapping = new EnumMap<>(Constants.Access.class);
        rolesMapping = new EnumMap<>(Constants.Roles.class);

        statusMapping.put(Constants.Status.ACTIVE, com.company.go.domain.global.Constants.Status.ACTIVE);
        statusMapping.put(Constants.Status.INACTIVE, com.company.go.domain.global.Constants.Status.INACTIVE);

        accessMapping.put(Constants.Access.LOCKED, com.company.go.domain.global.Constants.Access.LOCKED);
        accessMapping.put(Constants.Access.NOT_LOCKED, com.company.go.domain.global.Constants.Access.NOT_LOCKED);

        rolesMapping.put(Constants.Roles.ROLE_ADMIN, com.company.go.domain.global.Constants.Roles.ROLE_ADMIN);
    }

    static public User mapUserEntityToUserDomain(@NotNull UserEntity entity){
        User createdUser = new User();
        createdUser.setId(entity.getId());
        createdUser.setRoles(new HashSet<>());
        createdUser.setFirstName(entity.getFirstName());
        createdUser.setLastName(entity.getLastName());
        createdUser.setPassword(entity.getPassword());
        createdUser.setProfilePicture(entity.getProfilePicture());
        createdUser.setAlias(entity.getAlias());
        createdUser.setPictureType(entity.getPictureType());
        createdUser.setEmail(entity.getEmail());
        createdUser.setAddress(entity.getAddress());
        createdUser.setPhoneNumber(entity.getPhoneNumber());
        createdUser.setStatus(UserMapper.statusMapping.get(entity.getStatus()));
        createdUser.setAccess(UserMapper.accessMapping.get(entity.getAccess()));
        for(RoleEntity role : entity.getRoles()){
            createdUser.getRoles().add(rolesMapping.get(role.getName()));
        }
        return createdUser;
    }

    static public UserEntity mapUserDomainToUserEntity(@NotNull User user){
        UserEntity entityUser = new UserEntity();
        entityUser.setId(user.getId());
        entityUser.setRoles(new HashSet<>());
        entityUser.setFirstName(user.getFirstName());
        entityUser.setEmail(user.getEmail());
        entityUser.setLastName(user.getLastName());
        entityUser.setPassword(user.getPassword());
        entityUser.setProfilePicture(user.getProfilePicture());
        entityUser.setAlias(user.getAlias());
        entityUser.setAddress(user.getAddress());
        entityUser.setPictureType(user.getPictureType());
        entityUser.setPhoneNumber(user.getPhoneNumber());
        entityUser.setStatus(Utilities.getKeyByValue(statusMapping,user.getStatus()));
        entityUser.setAccess(Utilities.getKeyByValue(accessMapping, user.getAccess()));
        for(com.company.go.domain.global.Constants.Roles role : user.getRoles()){
            entityUser.getRoles().add(RoleEntity.toRoleEntity(Utilities.getKeyByValue(rolesMapping, role)));
        }
        return entityUser;
    }

}

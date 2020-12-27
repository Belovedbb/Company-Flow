package com.company.go.global;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "company_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @Column(name = "name")
    Constants.Roles name;

    @Transient
    public static RoleEntity toRoleEntity(Constants.Roles role){
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.name = role;
        return  roleEntity;
    }
}


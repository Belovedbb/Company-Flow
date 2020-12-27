package com.company.go.global;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Blob;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "company_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotEmpty
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @Column(name = "alias")
    private String alias;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "profile_picture")
    @Lob
    private Blob profilePicture;

    @Column(name = "picture_type")
    private String pictureType;

    @Column(name = "status")
    private Constants.Status status;

    @Column(name = "access")
    private Constants.Access access;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "company_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "Id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "Id"))
    private Set<RoleEntity> roles;
}

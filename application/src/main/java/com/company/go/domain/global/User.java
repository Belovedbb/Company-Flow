package com.company.go.domain.global;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Blob;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    @NonNull
    private String firstName, lastName, email,
            password,  alias, phoneNumber, address;

    @NotNull
    private Blob profilePicture;

    @NotNull
    private String pictureType;

    @NonNull
    private Constants.Status status;

    @NonNull
    private Constants.Access access;

    @NonNull
    private Set<Constants.Roles> roles;



}

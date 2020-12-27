package com.company.go;

import com.company.go.global.UserEntity;

public class StoreUtilities {

    static private UserEntity user;

    public static UserEntity getUser() {
        return user;
    }

    public static void setUser(UserEntity user) {
        StoreUtilities.user = user;
    }
}

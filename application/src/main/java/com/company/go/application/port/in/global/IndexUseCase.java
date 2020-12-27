package com.company.go.application.port.in.global;

import java.io.IOException;
import java.sql.SQLException;

public interface IndexUseCase {

    String getProfilePictureData() throws SQLException, IOException;
    String getProfilePictureType();
    String getAlias();
    String getFullName();
    String getRoles();
}

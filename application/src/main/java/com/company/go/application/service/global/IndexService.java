package com.company.go.application.service.global;

import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.domain.global.Constants;
import com.company.go.domain.global.User;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.StringJoiner;

@Service
public class IndexService implements IndexUseCase {
    private User user;

    public IndexService(User user) {
        this.user = user;
    }

    @Override
    public String getProfilePictureData() throws SQLException, IOException {
        Blob pictureBlob = user.getProfilePicture();
        byte[] rawData = pictureBlob.getBinaryStream().readAllBytes();
        return Base64Utils.encodeToString(rawData);
    }

    @Override
    public String getProfilePictureType() {
        return user.getPictureType();
    }

    @Override
    public String getAlias() {
        return user.getAlias();
    }

    @Override
    public String getFullName() {
        StringJoiner patcher = new StringJoiner(" ", " ", " ");
        patcher.add(user.getFirstName());
        patcher.add(user.getLastName());
        return patcher.toString();
    }

    @Override
    public String getRoles() {
        StringJoiner roles = new StringJoiner("\n");
        for(Constants.Roles role : user.getRoles()){
            roles.add( role.toString().toUpperCase().replaceAll("_", " -> ") );
        }
        return roles.toString();
    }
}

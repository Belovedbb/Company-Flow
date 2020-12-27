package com.company.go.application.port.in.global;

import com.company.go.domain.global.User;
import com.company.go.domain.global.Constants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface RegisterUserUseCase {

    boolean storeUser(RegisterUserModel model);

    List<RegisterUserModel> getAllUsers();

    RegisterUserModel getUser(Long id);

    @Getter
    @Setter
    @CompareFields(args = {"password,confirmPassword"})
    class RegisterUserModel{
        List<String> getSupportedPictureType(){
            List<String> picTypes = new ArrayList<>();
            picTypes.add("image/png");
            picTypes.add("image/bmp");
            picTypes.add("image/cis-cod");
            picTypes.add("image/gif");
            picTypes.add("image/ief");
            picTypes.add("image/jpeg");
            picTypes.add("image/pipeg");
            picTypes.add("image/svg+xml");
            picTypes.add("image/tiff");
            picTypes.add("image/x-cmu-raster");
            picTypes.add("image/x-cmx");
            picTypes.add("image/x-portable-anymap");
            picTypes.add("image/x-portable-bitmap");
            picTypes.add("image/x-portable-graymap");
            picTypes.add("image/x-portable-pixmap");
            picTypes.add("image/x-xbitmap");
            picTypes.add("image/x-rgb");
            picTypes.add("image/x-xwindowdump");
            return picTypes;
        }

        private String pictureData;

        private Long id;

        @NotEmpty(message = "field must not be empty")
        private String firstName, lastName,
                password, confirmPassword, alias, phoneNumber, address;

        @NotEmpty(message = "field must not be empty")
        @Email(message = "must be a valid email")
        private String email;

        private String pictureType;

        @NotNull
        private MultipartFile profilePicture;

        private Blob validatedProfilePicture() throws IOException, SQLException {
            //let's make the modelling happy else, npe
            if(profilePicture == null && !StringUtils.isEmpty(pictureData)){
                byte[] data = pictureData.getBytes();
                profilePicture = new MultipartFile() {
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public String getOriginalFilename() {
                        return null;
                    }

                    @Override
                    public String getContentType() {
                        return StringUtils.isEmpty(pictureType) ? "" : pictureType;
                    }

                    @Override
                    public boolean isEmpty() {
                        return data.length == 0;
                    }

                    @Override
                    public long getSize() {
                        return data.length;
                    }

                    @Override
                    public byte[] getBytes() throws IOException {
                        return data;
                    }

                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ByteArrayInputStream(data);
                    }

                    @Override
                    public void transferTo(File file) throws IOException, IllegalStateException {
                        new FileOutputStream(file).write(data);
                    }
                };
            }
            List<String> hasResult = getSupportedPictureType().stream().filter(e ->
                    e.equalsIgnoreCase(profilePicture.getContentType()))
                    .collect(Collectors.toList());
            if(hasResult.isEmpty())
                throw new IOException();

            pictureType = hasResult.get(0);

            return new SerialBlob(profilePicture.getBytes());
        }

        Set<Constants.Roles> roles = new HashSet<>();

        public User toUser() throws IOException, SQLException {
            roles.add(Constants.Roles.ROLE_ADMIN);
            return new User(
                    id,
                    firstName,
                    lastName,
                    email,
                    password,
                    alias,
                    phoneNumber,
                    address,
                    validatedProfilePicture(),
                    pictureType,
                    Constants.Status.ACTIVE,
                    Constants.Access.NOT_LOCKED,
                    roles
            );
        }
    }
}

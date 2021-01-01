package com.company.go.application.service.global;

import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.domain.global.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UpdateUserPort userStoreAdapter;

    @InjectMocks
    private UserService userService;

    private RegisterUserUseCase.RegisterUserModel userViewModel;

    @BeforeEach
    public void initUser(){
        userViewModel = new RegisterUserUseCase.RegisterUserModel();
        userViewModel.setFirstName("");
        userViewModel.setLastName("");
        userViewModel.setEmail("");
        userViewModel.setPassword("");
        userViewModel.setAlias("");
        userViewModel.setPhoneNumber("");
        userViewModel.setAddress("");
        userViewModel.setPictureData("Picture");
        userViewModel.setPictureType("image/png");
    }

    @AfterEach
    public void destroyUser(){
        userViewModel = null;
        Mockito.reset(userStoreAdapter);
    }

    @Test
    @DisplayName("Transform To User Domain And Store")
    public void transformAndStoreUser(){
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        boolean notStored = userService.storeUser(userViewModel);
        assertFalse(notStored);
        verify(userStoreAdapter, times(1)).saveUser(userCaptor.capture());
        String expectedEmail = userViewModel.getEmail();
        String resultEmail = userCaptor.getValue().getEmail();
        assertEquals(expectedEmail, resultEmail);
        assertNull(userCaptor.getValue().getId());
        assertEquals(userCaptor.getValue().getPhoneNumber(), userViewModel.getPhoneNumber());
    }

    @Test
    @DisplayName("Transform To User Domain And Edit")
    public void transformAndEditUser() throws IOException, SQLException {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        boolean notEdited = userService.updateUser(0L, userViewModel);
        assertFalse(notEdited);
        verify(userStoreAdapter, times(1)).updateUser(eq(0L), userCaptor.capture());
        String expectedEmail = userViewModel.getEmail();
        String resultEmail = userCaptor.getValue().getEmail();
        assertEquals(expectedEmail, resultEmail);
        assertNull(userCaptor.getValue().getId());
        assertEquals(userCaptor.getValue().getPhoneNumber(), userViewModel.getPhoneNumber());
    }

    @DisplayName("Transform To User Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewUser")
    public void transformAndViewUser(User user){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(userStoreAdapter.getUser(anyLong())).thenReturn(user);
        RegisterUserUseCase.RegisterUserModel model = userService.getUser(user.getId());
        assertNotNull(model);
        verify(userStoreAdapter, times(1)).getUser(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To User Domain And Get All Users")
    @ParameterizedTest
    @MethodSource("provideNewUser")
    public void transformAndGetTotalUser(User user){
        when(userStoreAdapter.getAllUsers()).thenReturn(List.of(user));
        List<RegisterUserUseCase.RegisterUserModel> totalModels = userService.getAllUsers();
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), user.getId());
    }



    private static Stream<Arguments> provideNewUser() throws SQLException {
        User user = new User();
        user.setId(1L);
        user.setProfilePicture(new SerialBlob("".getBytes(StandardCharsets.UTF_8)));
        return Stream.of(
                Arguments.of(user)
        );
    }

}

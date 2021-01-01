package com.company.go.application.service.archive;

import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.out.archive.UpdateStaffPort;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.global.User;
import com.company.go.domain.inventory.Money;
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
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffServiceUnitTest {

    @Mock
    private UpdateStaffPort staffStoreAdapter;

    @Mock
    private UpdateUserPort updateUserPort;

    @Mock
    RegisterUserUseCase registerUserUseCase;

    @InjectMocks
    private StaffService staffService;

    private StaffUseCase.StaffViewModel staffViewModel;

    @BeforeEach
    public void initStaff(){
        staffViewModel = new StaffUseCase.StaffViewModel();
        staffViewModel.setStatus(Staff.Constants.Status.ACTIVE.name());
        RegisterUserUseCase.RegisterUserModel registerUserModel = new RegisterUserUseCase.RegisterUserModel();
        registerUserModel.setFirstName("");
        registerUserModel.setLastName("");
        registerUserModel.setEmail("");
        registerUserModel.setPassword("");
        registerUserModel.setAlias("");
        registerUserModel.setPhoneNumber("");
        registerUserModel.setAddress("");
        registerUserModel.setPictureData("Picture");
        registerUserModel.setPictureType("image/png");
        staffViewModel.setUserModel(registerUserModel);
        staffViewModel.setPayment(0.0);
    }

    @AfterEach
    public void destroyStaff(){
        staffViewModel = null;
        Mockito.reset(staffStoreAdapter, updateUserPort, registerUserUseCase);
    }

    @Test
    @DisplayName("Transform To Staff Domain And Store")
    public void transformAndStoreStaff(){
        ArgumentCaptor<Staff> staffCaptor = ArgumentCaptor.forClass(Staff.class);
        boolean notStored = staffService.storeStaff(staffViewModel);
        assertFalse(notStored);
        verify(staffStoreAdapter, times(1)).storeStaff(staffCaptor.capture());
        String expectedStatus = staffViewModel.getStatus();
        String resultStatus = staffCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(staffCaptor.getValue().getId());
        assertEquals(staffCaptor.getValue().getStatus().name(), staffViewModel.getStatus());
    }

    @Test
    @DisplayName("Transform To Staff Domain And Edit")
    public void transformAndEditStaff(){
        ArgumentCaptor<Staff> staffCaptor = ArgumentCaptor.forClass(Staff.class);
        boolean notEdited = staffService.editStaff(0L, staffViewModel);
        assertFalse(notEdited);
        verify(staffStoreAdapter, times(1)).updateStaff(eq(0L), staffCaptor.capture());
        String expectedStatus = staffViewModel.getStatus();
        String resultStatus = staffCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(staffCaptor.getValue().getId());
        assertEquals(staffCaptor.getValue().getStatus().name(), staffViewModel.getStatus());
    }

    @DisplayName("Transform To Staff Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewStaff")
    public void transformAndViewStaff(Staff staff){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(staffStoreAdapter.getStaff(anyLong())).thenReturn(staff);
        StaffUseCase.StaffViewModel model = staffService.viewStaff(staff.getId());
        assertNotNull(model);
        verify(staffStoreAdapter, times(1)).getStaff(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Staff Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewStaff")
    public void transformAndGetTotalStaff(Staff staff){
        when(staffStoreAdapter.getTotalStaff()).thenReturn(List.of(staff));
        List<StaffUseCase.StaffViewModel> totalModels = staffService.getAllStaffs();
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), staff.getId());
    }

    @Test
    @DisplayName("Remove Staff And Get Max")
    public void removeStaffAndGetMax(){
        boolean notDeleted = staffService.deleteStaff(0L);
        assertFalse(notDeleted);
        Long noMax = staffService.getStaffMaxId();
        assertEquals(noMax, 0L);
    }

    private static Stream<Arguments> provideNewStaff() throws SQLException {
        Staff staff = new Staff();
        staff.setId(1L);
        staff.setPayment(new Money(Money.doubleToBigDecimal(0.0), Currency.getInstance("USD")));
        staff.setStatus(Staff.Constants.Status.INACTIVE);
        User user = new User();
        user.setProfilePicture(new SerialBlob("".getBytes(StandardCharsets.UTF_8)));
        staff.setUser(user);
        return Stream.of(
                Arguments.of(staff)
        );
    }

}

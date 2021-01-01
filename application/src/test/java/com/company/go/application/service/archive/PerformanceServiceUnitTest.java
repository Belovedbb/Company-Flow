package com.company.go.application.service.archive;

import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.out.archive.UpdatePerformancePort;
import com.company.go.domain.archive.performance.Performance;
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
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerformanceServiceUnitTest {

    @Mock
    private UpdatePerformancePort performanceStoreAdapter;
    
    @InjectMocks
    private PerformanceService performanceService;

    private PerformanceUseCase.PerformanceViewModel performanceViewModel;

    @BeforeEach
    public void initPerformance(){
        performanceViewModel = new PerformanceUseCase.PerformanceViewModel();
        performanceViewModel.setStatus(Performance.Constants.Status.POOR.name());
        performanceViewModel.setDate(LocalDate.now());
        performanceViewModel.setAverageMonthlyPerformance(0.0);
        performanceViewModel.setStaff(new StaffUseCase.StaffViewModel());
        performanceViewModel.setBonusPoint(0.0);
    }

    @AfterEach
    public void destroyPerformance(){
        performanceViewModel = null;
        Mockito.reset(performanceStoreAdapter);
    }

    @Test
    @DisplayName("Transform To Performance Domain And Store")
    public void transformAndStorePerformance(){
        ArgumentCaptor<Performance> performanceCaptor = ArgumentCaptor.forClass(Performance.class);
        boolean notStored = performanceService.storePerformance(performanceViewModel);
        assertFalse(notStored);
        verify(performanceStoreAdapter, times(1)).storePerformance(performanceCaptor.capture());
        String expectedStatus = performanceViewModel.getStatus();
        String resultStatus = performanceCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(performanceCaptor.getValue().getId());
        assertEquals(performanceCaptor.getValue().getDate(), performanceViewModel.getDate());
    }

    @Test
    @DisplayName("Transform To Performance Domain And Edit")
    public void transformAndEditPerformance(){
        ArgumentCaptor<Performance> performanceCaptor = ArgumentCaptor.forClass(Performance.class);
        boolean notEdited = performanceService.editPerformance(0L, performanceViewModel);
        assertFalse(notEdited);
        verify(performanceStoreAdapter, times(1)).updatePerformance(eq(0L), performanceCaptor.capture());
        String expectedStatus = performanceViewModel.getStatus();
        String resultStatus = performanceCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(performanceCaptor.getValue().getId());
        assertEquals(performanceCaptor.getValue().getDate(), performanceViewModel.getDate());
    }

    @DisplayName("Transform To Performance Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewPerformance")
    public void transformAndViewPerformance(Performance performance){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(performanceStoreAdapter.getPerformance(anyLong())).thenReturn(performance);
        PerformanceUseCase.PerformanceViewModel model = performanceService.viewPerformance(performance.getId());
        assertNotNull(model);
        verify(performanceStoreAdapter, times(1)).getPerformance(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Performance Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewPerformance")
    public void transformAndGetTotalPerformance(Performance performance){
        when(performanceStoreAdapter.getTotalPerformance()).thenReturn(List.of(performance));
        List<PerformanceUseCase.PerformanceViewModel> totalModels = performanceService.getAllPerformances();
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), performance.getId());
    }

    @Test
    @DisplayName("Remove Performance And Get Max")
    public void removePerformanceAndGetMax(){
        boolean notDeleted = performanceService.deletePerformance(0L);
        assertFalse(notDeleted);
        Long noMax = performanceService.getPerformanceMaxId();
        assertEquals(noMax, 0L);
    }

    private static Stream<Arguments> provideNewPerformance() throws SQLException {

        Staff staff = new Staff();
        staff.setPayment(new Money(Money.doubleToBigDecimal(0.0), Currency.getInstance("USD")));
        staff.setStatus(Staff.Constants.Status.INACTIVE);
        User user = new User();
        user.setProfilePicture(new SerialBlob("".getBytes(StandardCharsets.UTF_8)));
        staff.setUser(user);
        Performance performance = new Performance();
        performance.setId(1L);
        performance.setAverageMonthlyPerformance(0.0);
        performance.setDate(LocalDate.now());
        performance.setStatus(Performance.Constants.Status.POOR);
        performance.setBonusPoint(0.0);
        performance.setStaff(staff);

        return Stream.of(
                Arguments.of(performance)
        );
    }

}
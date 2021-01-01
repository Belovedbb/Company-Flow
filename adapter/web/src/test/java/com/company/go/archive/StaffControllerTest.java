package com.company.go.archive;

import com.company.go.ExcludePortConfig;
import com.company.go.TestErrorHandler;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.properties.CompanyProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = StaffController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class StaffControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private StaffUseCase staffUseCase;

    @Test
    @DisplayName("Setup staff")
    public void setUpStaff() throws Exception {
        mockMvc.perform(get("/archive/staff")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Company Flow | Archive")));
    }

    @Test
    @DisplayName("Setup staff listing")
    public void setUpStaffListing() throws Exception {
        mockMvc.perform(get("/archive/staff/listing")
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }

    @ParameterizedTest
    @MethodSource("provideStaffViewModel")
    @DisplayName("Test staff filtered listing")
    public void staffFilteredListing(StaffUseCase.StaffViewModel staff) throws Exception {
        mockMvc.perform(
                post("/archive/staff/listing/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(staff))
        ).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideStaffViewModel")
    @DisplayName("Setup new staff ")
    public void setUpNewStaff(StaffUseCase.StaffViewModel staff) throws Exception {
        mockMvc.perform(
                post("/archive/staff")
                        .flashAttr("staff", staff)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/archive/staff"));
        ArgumentCaptor<StaffUseCase.StaffViewModel> captor = ArgumentCaptor.forClass(StaffUseCase.StaffViewModel.class);
        verify(staffUseCase, times(1)).storeStaff(captor.capture());
        assertNotNull(captor.getValue());
        assertSame(captor.getValue(), staff);
    }

    @Test
    @DisplayName("Setup new staff using index ")
    public void createNewStaffUsingIndex() throws Exception {
        mockMvc.perform(
                get("/archive/staff/create/{indexes}", "0")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/archive/staff/listing"));
    }

    @Test
    @DisplayName("Setup new staff for validation")
    public void setUpNewStaffForValidation() throws Exception {
        StaffUseCase.StaffViewModel staff = new StaffUseCase.StaffViewModel();
        mockMvc.perform(
                post("/archive/staff")
                        .flashAttr("staff", staff)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("View staff")
    public void viewStaff() throws Exception {
        mockMvc.perform(get("/archive/staff/view/{id}", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Setup  staff report ")
    public void setUpStaffReport() throws Exception {
        mockMvc.perform(get("/archive/staff/report", 1))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/archive/staff"));
    }

    @Test
    @DisplayName("Edit staff")
    public void editStaff() throws Exception {
        mockMvc.perform(get("/archive/staff/edit/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete staff")
    public void deleteStaff() throws Exception {
        MvcResult errorResult = mockMvc.perform(get("/archive/staff/delete/{id}", 1))
                .andExpect(status().isBadRequest()).andReturn();
        MockHttpServletResponse response = errorResult.getResponse();

        TestErrorHandler.TestError errorHandler = new TestErrorHandler.TestError("Exception Occurred");
        String expectedResult = objectMapper.writeValueAsString(errorHandler);
        assertEquals(expectedResult, response.getContentAsString());
    }

    private static Stream<Arguments> provideStaffViewModel() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        StaffUseCase.StaffViewModel staffViewModel = new StaffUseCase.StaffViewModel();
        staffViewModel.setStatus(Staff.Constants.Status.INACTIVE.name());
        staffViewModel.setUserModel(new RegisterUserUseCase.RegisterUserModel());
        staffViewModel.setPayment(0.0);
        return Stream.of(
                Arguments.of(staffViewModel)
        );
    }
}
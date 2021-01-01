package com.company.go.archive;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.domain.archive.performance.Performance;
import com.company.go.properties.CompanyProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = PerformanceController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class PerformanceControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private PerformanceUseCase performanceUseCase;


    @Test
    @DisplayName("Setup performance listing")
    public void setUpPerformanceListing() throws Exception {
        mockMvc.perform(get("/archive/performance/listing")
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }

    @ParameterizedTest
    @MethodSource("providePerformanceViewModel")
    @DisplayName("Test performance filtered listing")
    public void performanceFilteredListing(PerformanceUseCase.PerformanceViewModel performance) throws Exception {
        mockMvc.perform(
                post("/archive/performance/listing/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(performance))
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("View performance")
    public void viewPerformance() throws Exception {
        mockMvc.perform(get("/archive/performance/view/{id}", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Setup  performance report ")
    public void setUpPerformanceReport() throws Exception {
        mockMvc.perform(get("/archive/performance/report", 1))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/archive/performance"));
    }

    

    private static Stream<Arguments> providePerformanceViewModel() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTime = formatter.format(LocalDateTime.now());
        PerformanceUseCase.PerformanceViewModel performanceViewModel = new PerformanceUseCase.PerformanceViewModel();
        performanceViewModel.setAverageMonthlyPerformance(0.0);
        performanceViewModel.setStaff(new StaffUseCase.StaffViewModel());
        performanceViewModel.setDate(LocalDate.now());
        performanceViewModel.setStatus(Performance.Constants.Status.POOR.name());
        return Stream.of(
                Arguments.of(performanceViewModel)
        );
    }
}
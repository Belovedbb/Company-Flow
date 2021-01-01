package com.company.go.report;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.domain.global.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = ArchiveReportController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class ArchiveReportControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffUseCase staffUseCase;

    @MockBean
    private PerformanceUseCase performanceUseCase;

    @MockBean
    private User user;


    @Test
    @DisplayName("Get Archive Report")
    public void getArchiveReport() throws Exception {
        mockMvc.perform(get("/report/archive"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Staff Report")
    public void getStaffReport() throws Exception {
        mockMvc.perform(get("/report/archive/staff"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Performance Report")
    public void getPerformanceReport() throws Exception {
        mockMvc.perform(get("/report/archive/performance"))
                .andExpect(status().isOk());
    }


}

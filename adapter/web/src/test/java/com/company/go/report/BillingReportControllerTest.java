package com.company.go.report;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.billing.AccountUseCase;
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
@WebMvcTest(controllers = BillingReportController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class BillingReportControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountUseCase accountUseCase;

    @MockBean
    User user;

    @Test
    @DisplayName("Get Billing Report")
    public void getBillingReport() throws Exception {
        mockMvc.perform(get("/report/billing/account"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Product Account Report")
    public void getProductAccountReport() throws Exception {
        mockMvc.perform(get("/report/billing/account/product"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Performance Account Report")
    public void getPerformanceAccountReport() throws Exception {
        mockMvc.perform(get("/report/billing/account/performance"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Purchase Order Account Report")
    public void getPurchaseOrderAccountReport() throws Exception {
        mockMvc.perform(get("/report/billing/account/purchaseorder"))
                .andExpect(status().isOk());
    }

}

package com.company.go.billing;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.PerformanceAccount;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = AccountController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class AccountControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private AccountUseCase accountUseCase;


    @Test
    @DisplayName("Setup account listing")
    public void setUpAccountListing() throws Exception {
        mockMvc.perform(get("/billing/account/listing")
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @DisplayName("Setup account listing type")
    public void setUpAccountListingType() throws Exception {
        mockMvc.perform(get("/billing/account/listing/{type}", Account.Constants.Type.PRODUCT.name())
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }


    @Test
    @DisplayName("View account")
    public void viewAccount() throws Exception {
        mockMvc.perform(get("/billing/account/view/{id}/{type}", 1, Account.Constants.Type.PRODUCT.name()))
                .andExpect(status().isOk());
    }

    @DisplayName("Setup  account report ")
    public void setUpAccountReport() throws Exception {
        mockMvc.perform(get("/billing/account/report"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/billing/account"));
    }

    @DisplayName("Setup  account report type")
    public void setUpAccountReportType() throws Exception {
        mockMvc.perform(get("/billing/account/report/{type}",  Account.Constants.Type.PRODUCT.name()))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/billing/account"));
    }

    private static Stream<Arguments> provideAccountViewModel() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTime = formatter.format(LocalDateTime.now());
        AccountUseCase.ProductAccountViewModel accountViewModel = new AccountUseCase.ProductAccountViewModel();
        return Stream.of(
                Arguments.of(accountViewModel)
        );
    }
}

package com.company.go.report;

import com.company.go.ExcludePortConfig;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
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
@WebMvcTest(controllers = InventoryReportController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class InventoryReportControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductUseCase productUseCase;

    @MockBean
    private PurchaseOrderUseCase purchaseOrderUseCase;

    @MockBean
    private User user;


    @Test
    @DisplayName("Get Inventory Report")
    public void getInventoryReport() throws Exception {
        mockMvc.perform(get("/report/inventory"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Product Report")
    public void getProductReport() throws Exception {
        mockMvc.perform(get("/report/inventory/product"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get Purchase Order Report")
    public void getPurchaseOrderReport() throws Exception {
        mockMvc.perform(get("/report/inventory/purchase/order"))
                .andExpect(status().isOk());
    }


}

package com.company.go.inventory;

import com.company.go.ExcludePortConfig;
import com.company.go.TestErrorHandler;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.inventory.order.Order;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = PurchaseOrderController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class PurchaseOrderControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private PurchaseOrderUseCase purchaseOrderUseCase;


    @Test
    @DisplayName("Setup Purchase Order")
    public void setUpPurchaseOrder() throws Exception {
        mockMvc.perform(get("/inventory/purchase/order")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Company Flow | Inventory")));
    }

    @Test
    @DisplayName("Setup Purchase Order listing")
    public void setUpPurchaseOrderListing() throws Exception {
        mockMvc.perform(get("/inventory/purchase/order/listing")
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }

    @ParameterizedTest
    @MethodSource("providePurchaseOrderViewModel")
    @DisplayName("Test purchaseOrder filtered listing")
    public void purchaseOrderFilteredListing(PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder) throws Exception {
        mockMvc.perform(
                post("/inventory/purchase/order/listing/filter")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(purchaseOrder))
        ).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("providePurchaseOrderViewModel")
    @DisplayName("Setup new purchaseOrder ")
    public void setUpNewPurchaseOrder(PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder) throws Exception {
        mockMvc.perform(
                post("/inventory/purchase/order")
                        .flashAttr("purchaseOrder", purchaseOrder)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/purchase/order"));
        ArgumentCaptor<PurchaseOrderUseCase.PurchaseOrderViewModel> captor = ArgumentCaptor.forClass(PurchaseOrderUseCase.PurchaseOrderViewModel.class);
        verify(purchaseOrderUseCase, times(1)).storePurchaseOrder(captor.capture());
        assertNotNull(captor.getValue());
        assertSame(captor.getValue(), purchaseOrder);
    }

    @Test
    @DisplayName("Setup new Purchase Order for validation")
    public void setUpNewPurchaseOrderForValidation() throws Exception {
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrder = new PurchaseOrderUseCase.PurchaseOrderViewModel();
        mockMvc.perform(
                post("/inventory/purchase/order")
                        .flashAttr("purchaseOrder", purchaseOrder)
        ).andExpect(status().isOk());
        verify(purchaseOrderUseCase, times(1)).getAvailableProducts();
    }

    @Test
    @DisplayName("View purchaseOrder")
    public void viewPurchaseOrder() throws Exception {
        when(purchaseOrderUseCase.viewPurchaseOrder(anyLong())).thenReturn(new PurchaseOrderUseCase.PurchaseOrderViewModel());
        mockMvc.perform(get("/inventory/purchase/order/view/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Setup  purchaseOrder report ")
    public void setUpPurchaseOrderReport() throws Exception {
        mockMvc.perform(get("/inventory/purchase/order/report", 1))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/inventory/purchase/order"));
    }

    @Test
    @DisplayName("Edit Purchase Order")
    public void editPurchaseOrder() throws Exception {
        when(purchaseOrderUseCase.viewPurchaseOrder(anyLong())).thenReturn(new PurchaseOrderUseCase.PurchaseOrderViewModel());
        mockMvc.perform(get("/inventory/purchase/order/edit/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete Purchase Order")
    public void deletePurchaseOrder() throws Exception {
        MvcResult errorResult = mockMvc.perform(get("/inventory/purchase/order/delete/{id}", 1))
                .andExpect(status().isBadRequest()).andReturn();
        MockHttpServletResponse response = errorResult.getResponse();

        TestErrorHandler.TestError errorHandler = new TestErrorHandler.TestError("Exception Occurred");
        String expectedResult = objectMapper.writeValueAsString(errorHandler);
        assertEquals(expectedResult, response.getContentAsString());
    }

    private static Stream<Arguments> providePurchaseOrderViewModel() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTime = formatter.format(LocalDateTime.now());
        PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrderViewModel = new PurchaseOrderUseCase.PurchaseOrderViewModel();
        purchaseOrderViewModel.setStatus(Order.Constants.Status.CLOSED.name());
        purchaseOrderViewModel.setDescription("Test");
        purchaseOrderViewModel.setAmount(0.0);
        purchaseOrderViewModel.setPurchasedDate(dateTime);
        purchaseOrderViewModel.setLastChangedDate(dateTime);
        return Stream.of(
                Arguments.of(purchaseOrderViewModel)
        );
    }
}

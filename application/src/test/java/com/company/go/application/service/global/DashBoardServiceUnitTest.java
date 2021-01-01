package com.company.go.application.service.global;

import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.global.DashBoardUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashBoardServiceUnitTest {

    @Mock
    ProductUseCase productUseCase;

    @Mock
    PurchaseOrderUseCase purchaseOrderUseCase;

    @Mock
    PerformanceUseCase performanceUseCase;

    @InjectMocks
    DashBoardService dashBoardService;

    @Test
    @DisplayName("Test all Summaries")
    public void testAllSummaries(){
        when(productUseCase.getAllProducts()).thenReturn(List.of());
        when(purchaseOrderUseCase.getAllPurchaseOrders()).thenReturn(List.of());
        when(performanceUseCase.getAllPerformances()).thenReturn(List.of());
        List<DashBoardUseCase.SummaryWidgetViewModel> summaries = dashBoardService.getAllSummaries();
        assertEquals(summaries.size(), 6);
        DashBoardUseCase.SummaryWidgetViewModel summary = summaries.get(0);
        assertNotNull(summary);
        assertEquals(summary.getCount(), 0);
        assertTrue(summary.getPeriodText().contains("days"));
    }

    @Test
    @DisplayName("Test detailed product chart data")
    public void testDetailedProductChartData(){
        when(productUseCase.getAllProducts()).thenReturn(List.of());
        Map<String, Long>[] chartData = dashBoardService.detailedProductChartData();
        assertNotNull(chartData);
        assertEquals(chartData.length, 2);
        Map<String, Long> chartYear = chartData[0];
        Map<String, Long> chartStatus = chartData[1];

        assertEquals(chartYear.size(), 12);
        Long yearValue = chartYear.get("1");
        assertNotNull(yearValue);
        assertEquals(yearValue, 0);

        assertEquals(chartStatus.size(), 2);
        Long statusValue = chartStatus.get(Product.Constants.Status.ACTIVE.name());
        assertNotNull(statusValue);
        assertEquals(statusValue, 0);
    }


    @Test
    @DisplayName("Test detailed order chart data")
    public void testDetailedOrderChartData(){
        when(purchaseOrderUseCase.getAllPurchaseOrders()).thenReturn(List.of());
        Map<String, Long>[] chartData = dashBoardService.detailedOrderChartData();
        assertNotNull(chartData);
        assertEquals(chartData.length, 2);
        Map<String, Long> chartYear = chartData[0];
        Map<String, Long> chartStatus = chartData[1];

        assertEquals(chartYear.size(), 12);
        Long yearValue = chartYear.get("1");
        assertNotNull(yearValue);
        assertEquals(yearValue, 0);

        assertEquals(chartStatus.size(), 2);
        Long statusValue = chartStatus.get(Order.Constants.Status.CLOSED.name());
        assertNotNull(statusValue);
        assertEquals(statusValue, 0);
    }

}

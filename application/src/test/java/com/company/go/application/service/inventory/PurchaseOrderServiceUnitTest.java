package com.company.go.application.service.inventory;

import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.application.port.out.inventory.UpdatePurchaseOrderPort;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.order.OrderEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceUnitTest {

    @Mock
    private UpdatePurchaseOrderPort purchaseOrderStoreAdapter;

    @Mock
    private ProductUseCase productUseCase;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    private PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrderViewModel;

    private MockedStatic<Currency> currencyStatic ;

    @BeforeEach
    public void initPurchaseOrder(){
        PurchaseOrderUseCase.PurchaseOrderEntryViewModel entryViewModel = new PurchaseOrderUseCase.PurchaseOrderEntryViewModel();
        entryViewModel.setOrderAmount(0.0);
        entryViewModel.setProduct(new ProductUseCase.ProductViewModel());
        entryViewModel.setStored(false);
        entryViewModel.setOrderQuantity(2L);

        purchaseOrderViewModel = new PurchaseOrderUseCase.PurchaseOrderViewModel();
        purchaseOrderViewModel.setStatus(Order.Constants.Status.OPEN.name());
        purchaseOrderViewModel.setDescription("Test");
        purchaseOrderViewModel.setPurchaseOrderEntries(List.of(entryViewModel));
        purchaseOrderViewModel.setAmount(0.0);
        purchaseOrderViewModel.setQuantity(2L);
        currencyStatic = mockStatic(Currency.class);
        currencyStatic.when(() -> Currency.getInstance(anyString())).thenReturn(null);
    }

    @AfterEach
    public void destroyPurchaseOrder(){
        purchaseOrderViewModel = null;
        currencyStatic.close();
        Mockito.reset(purchaseOrderStoreAdapter, productUseCase);
    }

    @Test
    @DisplayName("Transform To PurchaseOrder Domain And Store")
    public void transformAndStorePurchaseOrder(){
        ArgumentCaptor<Order> purchaseOrderCaptor = ArgumentCaptor.forClass(Order.class);
        boolean notStored = purchaseOrderService.storePurchaseOrder(purchaseOrderViewModel);
        assertFalse(notStored);
        verify(purchaseOrderStoreAdapter, times(1)).storePurchaseOrder(purchaseOrderCaptor.capture());
        String expectedStatus = purchaseOrderViewModel.getStatus();
        String resultStatus = purchaseOrderCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(purchaseOrderCaptor.getValue().getId());
        assertEquals(purchaseOrderCaptor.getValue().getDescription(), purchaseOrderViewModel.getDescription());
    }

    @Test
    @DisplayName("Transform To PurchaseOrder Domain And Edit")
    public void transformAndEditPurchaseOrder(){
        ArgumentCaptor<Order> purchaseOrderCaptor = ArgumentCaptor.forClass(Order.class);
        boolean notEdited = purchaseOrderService.editPurchaseOrder(0L, purchaseOrderViewModel);
        assertFalse(notEdited);
        verify(purchaseOrderStoreAdapter, times(1)).updatePurchaseOrder(eq(0L), purchaseOrderCaptor.capture());
        String expectedStatus = purchaseOrderViewModel.getStatus();
        String resultStatus = purchaseOrderCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(purchaseOrderCaptor.getValue().getId());
        assertEquals(purchaseOrderCaptor.getValue().getDescription(), purchaseOrderViewModel.getDescription());
    }

    @DisplayName("Transform To PurchaseOrder Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewPurchaseOrder")
    public void transformAndViewPurchaseOrder(Order purchaseOrder){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(purchaseOrderStoreAdapter.getOrder(anyLong())).thenReturn(purchaseOrder);
        when(productUseCase.viewProduct(anyLong())).thenReturn(new ProductUseCase.ProductViewModel());
        PurchaseOrderUseCase.PurchaseOrderViewModel model = purchaseOrderService.viewPurchaseOrder(purchaseOrder.getId());
        assertNotNull(model);
        verify(purchaseOrderStoreAdapter, times(1)).getOrder(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To PurchaseOrder Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewPurchaseOrder")
    public void transformAndGetTotalPurchaseOrder(Order purchaseOrder){
        when(purchaseOrderStoreAdapter.getTotalPurchaseOrder()).thenReturn(List.of(purchaseOrder));
        when(productUseCase.viewProduct(anyLong())).thenReturn(new ProductUseCase.ProductViewModel());
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> totalModels = purchaseOrderService.getAllPurchaseOrders();
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), purchaseOrder.getId());
    }

    @Test
    @DisplayName("Remove PurchaseOrder And Get Max")
    public void removePurchaseOrderAndGetMax(){
        boolean notDeleted = purchaseOrderService.deletePurchaseOrder(0L);
        assertFalse(notDeleted);
        Long noMax = purchaseOrderService.getPurchaseOrderMaxId();
        assertEquals(noMax, 0L);
    }

    private static Stream<Arguments> provideNewPurchaseOrder() {
        OrderEntry entry = new OrderEntry();
        entry.setProductId(1L);
        entry.setQuantity(2L);
        entry.setStored(false);

        ArrayList<OrderEntry> orderEntries = new java.util.ArrayList<>();
        orderEntries.add(entry);

        Order purchaseOrder = new Order();
        purchaseOrder.setId(1L);
        purchaseOrder.setHasVat(false);
        purchaseOrder.setLastChangedDate(LocalDateTime.now());
        purchaseOrder.setOrderEntries(orderEntries);
        purchaseOrder.setPurchasedDate(LocalDateTime.now());
        purchaseOrder.setStatus(Order.Constants.Status.OPEN);
        purchaseOrder.setDescription("test");
        return Stream.of(
                Arguments.of(purchaseOrder)
        );
    }

}

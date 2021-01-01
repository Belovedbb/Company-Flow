package com.company.go.application.service.billing;

import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.out.billing.UpdatePerformanceAccountPort;
import com.company.go.application.port.out.billing.UpdateProductAccountPort;
import com.company.go.application.port.out.billing.UpdatePurchaseOrderAccountPort;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.PerformanceAccount;
import com.company.go.domain.billing.ProductAccount;
import com.company.go.domain.billing.PurchaseOrderAccount;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceUnitTest {

    @Mock
    private UpdatePerformanceAccountPort performanceAccountStore;

    @Mock
    UpdatePurchaseOrderAccountPort purchaseOrderAccountStore;

    @Mock
    UpdateProductAccountPort productAccountStore;

    @InjectMocks
    private AccountService accountService;

    private AccountUseCase.PerformanceAccountViewModel performanceAccountViewModel;

    private AccountUseCase.ProductAccountViewModel productAccountViewModel;

    private AccountUseCase.PurchaseOrderAccountViewModel purchaseOrderAccountViewModel;

    private void initPerformanceAccount(){
        performanceAccountViewModel = new AccountUseCase.PerformanceAccountViewModel();
        performanceAccountViewModel.setTypeCount(0L);
        performanceAccountViewModel.setDate(LocalDate.now());
        performanceAccountViewModel.setKind(PerformanceAccount.Constants.Kind.DEBIT.name());
        performanceAccountViewModel.setType(Account.Constants.Type.PERFORMANCE.name());
        performanceAccountViewModel.setAggregateAmount(0d);
    }

    private void initPurchaseOrderAccount(){
        purchaseOrderAccountViewModel = new AccountUseCase.PurchaseOrderAccountViewModel();
        purchaseOrderAccountViewModel.setTypeCount(0L);
        purchaseOrderAccountViewModel.setDate(LocalDate.now());
        purchaseOrderAccountViewModel.setKind(PerformanceAccount.Constants.Kind.CREDIT.name());
        purchaseOrderAccountViewModel.setType(Account.Constants.Type.PURCHASEORDER.name());
        purchaseOrderAccountViewModel.setAggregateAmount(0d);
    }

    private void initProductAccount(){
        productAccountViewModel = new AccountUseCase.ProductAccountViewModel();
        productAccountViewModel.setTypeCount(0L);
        productAccountViewModel.setDate(LocalDate.now());
        productAccountViewModel.setKind(PerformanceAccount.Constants.Kind.DEBIT.name());
        productAccountViewModel.setType(Account.Constants.Type.PRODUCT.name());
        productAccountViewModel.setAggregateAmount(0d);
    }

    @BeforeEach
    public void initAccount(){
        initPerformanceAccount();
        initProductAccount();
        initPurchaseOrderAccount();
    }

    @AfterEach
    public void destroyAccount(){
        performanceAccountViewModel = null;
        productAccountViewModel = null;
        purchaseOrderAccountViewModel = null;
        Mockito.reset(performanceAccountStore, productAccountStore, purchaseOrderAccountStore);
    }

    @Test
    @DisplayName("Transform To Performance Account Domain And Store")
    public void transformAndStorePerformanceAccount(){
        ArgumentCaptor<PerformanceAccount> accountCaptor = ArgumentCaptor.forClass(PerformanceAccount.class);
        boolean notStored = accountService.storeAccount(performanceAccountViewModel);
        assertFalse(notStored);
        verify(performanceAccountStore, times(1)).storePerformanceAccount(accountCaptor.capture());
        String expectedKind = performanceAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), performanceAccountViewModel.getType());
    }

    @Test
    @DisplayName("Transform To Product Account Domain And Store")
    public void transformAndStoreProductAccount(){
        ArgumentCaptor<ProductAccount> accountCaptor = ArgumentCaptor.forClass(ProductAccount.class);
        boolean notStored = accountService.storeAccount(productAccountViewModel);
        assertFalse(notStored);
        verify(productAccountStore, times(1)).storeProductAccount(accountCaptor.capture());
        String expectedKind = productAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), productAccountViewModel.getType());
    }

    @Test
    @DisplayName("Transform To Purchase Order Account Domain And Store")
    public void transformAndStorePurchaseOrderAccount(){
        ArgumentCaptor<PurchaseOrderAccount> accountCaptor = ArgumentCaptor.forClass(PurchaseOrderAccount.class);
        boolean notStored = accountService.storeAccount(purchaseOrderAccountViewModel);
        assertFalse(notStored);
        verify(purchaseOrderAccountStore, times(1)).storePurchaseOrderAccount(accountCaptor.capture());
        String expectedKind = purchaseOrderAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), purchaseOrderAccountViewModel.getType());
    }


    @Test
    @DisplayName("Transform To Performance Account Domain And Edit")
    public void transformAndEditPerformanceAccount(){
        ArgumentCaptor<PerformanceAccount> accountCaptor = ArgumentCaptor.forClass(PerformanceAccount.class);
        boolean notEdited = accountService.editAccount(0L, performanceAccountViewModel);
        assertFalse(notEdited);
        verify(performanceAccountStore, times(1)).updatePerformanceAccount(eq(0L), accountCaptor.capture());
        String expectedKind = performanceAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), performanceAccountViewModel.getType());
    }

    @Test
    @DisplayName("Transform To Product Account Domain And Edit")
    public void transformAndEditProductAccount(){
        ArgumentCaptor<ProductAccount> accountCaptor = ArgumentCaptor.forClass(ProductAccount.class);
        boolean notEdited = accountService.editAccount(0L, productAccountViewModel);
        assertFalse(notEdited);
        verify(productAccountStore, times(1)).updateProductAccount(eq(0L), accountCaptor.capture());
        String expectedKind = productAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), productAccountViewModel.getType());
    }

    @Test
    @DisplayName("Transform To Purchase Order Account Domain And Edit")
    public void transformAndEditPurchaseOrderAccount(){
        ArgumentCaptor<PurchaseOrderAccount> accountCaptor = ArgumentCaptor.forClass(PurchaseOrderAccount.class);
        boolean notEdited = accountService.editAccount(0L, purchaseOrderAccountViewModel);
        assertFalse(notEdited);
        verify(purchaseOrderAccountStore, times(1)).updatePurchaseOrderAccount(eq(0L), accountCaptor.capture());
        String expectedKind = purchaseOrderAccountViewModel.getKind();
        String resultKind = accountCaptor.getValue().getKind().name();
        assertEquals(expectedKind, resultKind);
        assertNull(accountCaptor.getValue().getId());
        assertEquals(accountCaptor.getValue().getType(), purchaseOrderAccountViewModel.getType());
    }

    @DisplayName("Transform To Performance Account Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndViewPerformanceAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(performanceAccountStore.getPerformanceAccount(anyLong())).thenReturn(performanceAccount);
        AccountUseCase.AccountViewModel model = accountService.viewAccount(performanceAccount.getId(),performanceAccount.getType());
        assertNotNull(model);
        verify(performanceAccountStore, times(1)).getPerformanceAccount(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Product Account Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndViewProductAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(productAccountStore.getProductAccount(anyLong())).thenReturn(productAccount);
        AccountUseCase.AccountViewModel model = accountService.viewAccount(productAccount.getId(),productAccount.getType());
        assertNotNull(model);
        verify(productAccountStore, times(1)).getProductAccount(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Purchase Order Account Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndViewPurchaseOrderAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(purchaseOrderAccountStore.getPurchaseOrderAccount(anyLong())).thenReturn(purchaseOrderAccount);
        AccountUseCase.AccountViewModel model = accountService.viewAccount(purchaseOrderAccount.getId(),purchaseOrderAccount.getType());
        assertNotNull(model);
        verify(purchaseOrderAccountStore, times(1)).getPurchaseOrderAccount(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Performance Account Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndGetTotalPerformanceAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        when(performanceAccountStore.getTotalPerformanceAccount()).thenReturn(List.of(performanceAccount));
        List<AccountUseCase.AccountViewModel> totalModels = accountService.getAllAccounts(performanceAccount.getType());
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), performanceAccount.getId());
    }

    @DisplayName("Transform To Product Account Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndGetTotalProductAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        when(productAccountStore.getTotalProductAccount()).thenReturn(List.of(productAccount));
        List<AccountUseCase.AccountViewModel> totalModels = accountService.getAllAccounts(productAccount.getType());
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), productAccount.getId());
    }

    @DisplayName("Transform To Purchase Order Account Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewAccount")
    public void transformAndGetTotalPurchaseOrderAccount(PerformanceAccount performanceAccount, ProductAccount productAccount, PurchaseOrderAccount purchaseOrderAccount){
        when(purchaseOrderAccountStore.getTotalPurchaseOrderAccount()).thenReturn(List.of(purchaseOrderAccount));
        List<AccountUseCase.AccountViewModel> totalModels = accountService.getAllAccounts(purchaseOrderAccount.getType());
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), purchaseOrderAccount.getId());
    }

    @Test
    @DisplayName("Remove Performance Account And Get Max")
    public void removePerformanceAccountAndGetMax(){
        boolean notDeleted = accountService.deleteAccount(0L, Account.Constants.Type.PERFORMANCE.name());
        assertFalse(notDeleted);
        Long noMax = accountService.getAccountMaxId(Account.Constants.Type.PERFORMANCE.name());
        assertEquals(noMax, 0L);
    }

    @Test
    @DisplayName("Remove Product Account And Get Max")
    public void removeProductAccountAndGetMax(){
        boolean notDeleted = accountService.deleteAccount(0L, Account.Constants.Type.PRODUCT.name());
        assertFalse(notDeleted);
        Long noMax = accountService.getAccountMaxId(Account.Constants.Type.PRODUCT.name());
        assertEquals(noMax, 0L);
    }

    @Test
    @DisplayName("Remove Purchase order Account And Get Max")
    public void removePurchaseOrderAccountAndGetMax(){
        boolean notDeleted = accountService.deleteAccount(0L, Account.Constants.Type.PURCHASEORDER.name());
        assertFalse(notDeleted);
        Long noMax = accountService.getAccountMaxId(Account.Constants.Type.PURCHASEORDER.name());
        assertEquals(noMax, 0L);
    }

    private static Stream<Arguments> provideNewAccount() {
        PerformanceAccount performanceAccount = new PerformanceAccount();
        performanceAccount.setType(Account.Constants.Type.PERFORMANCE.name());
        performanceAccount.setId(0L);
        PurchaseOrderAccount purchaseOrderAccount = new PurchaseOrderAccount();
        purchaseOrderAccount.setType(Account.Constants.Type.PURCHASEORDER.name());
        purchaseOrderAccount.setId(0L);
        ProductAccount productAccount = new ProductAccount();
        productAccount.setType(Account.Constants.Type.PRODUCT.name());
        productAccount.setId(0L);
        return Stream.of(
                Arguments.of(performanceAccount, productAccount, purchaseOrderAccount)
        );
    }

}

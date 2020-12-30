package com.company.go.application.service.inventory;

import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.out.inventory.UpdateProductPort;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;
import com.company.go.properties.CompanyProperties;
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
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @Mock
    private UpdateProductPort productStoreAdapter;

    @Mock
    private CompanyProperties properties;

    @InjectMocks
    private ProductService productService;

    private ProductUseCase.ProductViewModel productViewModel;

    private MockedStatic<Currency> currencyStatic ;

    @BeforeEach
    public void initProduct(){
        productViewModel = new ProductUseCase.ProductViewModel();
        productViewModel.setStatus(Product.Constants.Status.INACTIVE.name());
        productViewModel.setInactiveSubStatus(Product.Constants.Store.FAULTY.name());
        productViewModel.setDescription("Test");
        productViewModel.setName("Tester");
        productViewModel.setAmount(0.0);
        productViewModel.setQuantity(2L);
        currencyStatic = mockStatic(Currency.class);
        currencyStatic.when(() -> Currency.getInstance(anyString())).thenReturn(null);
    }

    @AfterEach
    public void destroyProduct(){
        productViewModel = null;
        currencyStatic.close();
        Mockito.reset(productStoreAdapter, properties);
    }

    @Test
    @DisplayName("Transform To Product Domain And Store")
    public void transformAndStoreProduct(){
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        boolean notStored = productService.storeProduct(productViewModel);
        assertFalse(notStored);
        verify(productStoreAdapter, times(1)).storeProduct(productCaptor.capture());
        String expectedStatus = productViewModel.getStatus();
        String resultStatus = productCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(productCaptor.getValue().getId());
        assertEquals(productCaptor.getValue().getQuantity(), productViewModel.getQuantity());
        assertEquals(productCaptor.getValue().getDescription(), productViewModel.getDescription());
    }

    @Test
    @DisplayName("Transform To Product Domain And Edit")
    public void transformAndEditProduct(){
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        boolean notEdited = productService.editProduct(0L, productViewModel);
        assertFalse(notEdited);
        verify(productStoreAdapter, times(1)).updateProduct(eq(0L), productCaptor.capture());
        String expectedStatus = productViewModel.getStatus();
        String resultStatus = productCaptor.getValue().getStatus().name();
        assertEquals(expectedStatus, resultStatus);
        assertNull(productCaptor.getValue().getId());
        assertEquals(productCaptor.getValue().getQuantity(), productViewModel.getQuantity());
        assertEquals(productCaptor.getValue().getDescription(), productViewModel.getDescription());
    }

    @DisplayName("Transform To Product Domain And View")
    @ParameterizedTest
    @MethodSource("provideNewProduct")
    public void transformAndViewProduct(Product product){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        when(productStoreAdapter.getProduct(anyLong())).thenReturn(product);
        ProductUseCase.ProductViewModel model = productService.viewProduct(product.getId());
        assertNotNull(model);
        verify(productStoreAdapter, times(1)).getProduct(idCaptor.capture());
        Long expectedId = model.getId();
        Long resultId = idCaptor.getValue();
        assertEquals(expectedId, resultId);
        assertNotNull(idCaptor.getValue());
    }

    @DisplayName("Transform To Product Domain And Get Total")
    @ParameterizedTest
    @MethodSource("provideNewProduct")
    public void transformAndGetTotalProduct(Product product){
        when(productStoreAdapter.getTotalProduct()).thenReturn(List.of(product));
        List<ProductUseCase.ProductViewModel> totalModels = productService.getAllProducts();
        assertNotNull(totalModels);
        assertEquals(totalModels.size(), 1);
        assertEquals(totalModels.get(0).getId(), product.getId());
    }

    @DisplayName("Transform To Product Domain And Get Filtered Product")
    @ParameterizedTest
    @MethodSource("provideNewProduct")
    public void transformAndGetFilteredProduct(Product product){
        ArgumentCaptor<Map<String, Object>> argumentCaptor = ArgumentCaptor.forClass(Map.class);
        when(productStoreAdapter.getTotalFilteredProduct(any())).thenReturn(List.of(product));
        List<ProductUseCase.ProductViewModel> filteredProducts = productService.getFilteredProducts(productViewModel);
        assertNotNull(filteredProducts);
        assertEquals(filteredProducts.size(), 1);
        assertEquals(filteredProducts.get(0).getId(), product.getId());
        verify(productStoreAdapter, times(1)).getTotalFilteredProduct(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue());
        Object capturedNameValue = argumentCaptor.getValue().get("name");
        assertNotNull(capturedNameValue);
        assertEquals(capturedNameValue, productViewModel.getName());
    }

    @Test
    @DisplayName("Remove Product And Get Max")
    public void removeProductAndGetMax(){
        boolean notDeleted = productService.deleteProduct(0L);
        assertFalse(notDeleted);
        Long noMax = productService.getProductMaxId();
        assertEquals(noMax, 0L);
    }

    private static Stream<Arguments> provideNewProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setType(new ProductType());
        product.setLastChangedDate(LocalDateTime.now());
        product.setManufacturedDate(LocalDateTime.now());
        product.setExpiryDate(LocalDateTime.now());
        product.setPurchasedDate(LocalDateTime.now());
        product.setSuppliedDate(LocalDateTime.now());
        product.setAmount(new Money(Money.doubleToBigDecimal(0.0), Currency.getInstance("USD")));
        product.setStatus(Product.Constants.Status.INACTIVE);
        product.setStore(Product.Constants.Store.FAULTY);
        product.setDescription("test");
        return Stream.of(
                Arguments.of(product)
        );
    }

}

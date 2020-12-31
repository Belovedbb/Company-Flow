package com.company.go.inventory;

import com.company.go.ExcludePortConfig;
import com.company.go.TestErrorHandler;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.domain.inventory.product.Product;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringBootConfiguration.class, ExcludePortConfig.class})
@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration  = SecurityAutoConfiguration.class)
public class ProductControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IndexUseCase indexer;

    @MockBean
    private ProductUseCase productUseCase;


    @Test
    @DisplayName("Setup product")
    public void setUpProduct() throws Exception {
        mockMvc.perform(get("/inventory/product")
                .contentType("application/html"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Company Flow | Inventory")));
    }

    @Test
    @DisplayName("Setup product listing")
    public void setUpProductListing() throws Exception {
        mockMvc.perform(get("/inventory/product/listing")
                .contentType("application/html"))
                .andExpect(status().isOk()).andReturn();
    }

    @ParameterizedTest
    @MethodSource("provideProductViewModel")
    @DisplayName("Test product filtered listing")
    public void productFilteredListing(ProductUseCase.ProductViewModel product) throws Exception {
        mockMvc.perform(
                post("/inventory/product/listing/filter")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(product))
        ).andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideProductViewModel")
    @DisplayName("Setup new product ")
    public void setUpNewProduct(ProductUseCase.ProductViewModel product) throws Exception {
        mockMvc.perform(
                post("/inventory/product")
                .flashAttr("product", product)
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/product"));
        ArgumentCaptor<ProductUseCase.ProductViewModel> captor = ArgumentCaptor.forClass(ProductUseCase.ProductViewModel.class);
        verify(productUseCase, times(1)).storeProduct(captor.capture());
        assertNotNull(captor.getValue());
        assertSame(captor.getValue(), product);
    }

    @Test
    @DisplayName("Setup new product for validation")
    public void setUpNewProductForValidation() throws Exception {
        ProductUseCase.ProductViewModel product = new ProductUseCase.ProductViewModel();
        mockMvc.perform(
                post("/inventory/product")
                        .flashAttr("product", product)
        ).andExpect(status().isOk());
        ArgumentCaptor<ProductUseCase.ProductViewModel> captor = ArgumentCaptor.forClass(ProductUseCase.ProductViewModel.class);
        verify(productUseCase, times(1)).getProductCategory();
    }

    @Test
    @DisplayName("View product")
    public void viewProduct() throws Exception {
        mockMvc.perform(get("/inventory/product/view/{id}", 1))
                .andExpect(status().isOk());
    }

    @DisplayName("Setup  product report ")
    public void setUpProductReport() throws Exception {
        mockMvc.perform(get("/inventory/product/report", 1))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/report/inventory/product"));
    }

    @Test
    @DisplayName("Edit product")
    public void editProduct() throws Exception {
        mockMvc.perform(get("/inventory/product/edit/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete product")
    public void deleteProduct() throws Exception {
        MvcResult errorResult = mockMvc.perform(get("/inventory/product/delete/{id}", 1))
                .andExpect(status().isBadRequest()).andReturn();
        MockHttpServletResponse response = errorResult.getResponse();

        TestErrorHandler.TestError errorHandler = new TestErrorHandler.TestError("Exception Occurred");
        String expectedResult = objectMapper.writeValueAsString(errorHandler);
        assertEquals(expectedResult, response.getContentAsString());
    }

    private static Stream<Arguments> provideProductViewModel() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String dateTime = formatter.format(LocalDateTime.now());
        ProductUseCase.ProductViewModel productViewModel = new ProductUseCase.ProductViewModel();
        productViewModel.setStatus(Product.Constants.Status.INACTIVE.name());
        productViewModel.setInactiveSubStatus(Product.Constants.Store.FAULTY.name());
        productViewModel.setDescription("Test");
        productViewModel.setName("Tester");
        productViewModel.setAmount(0.0);
        productViewModel.setQuantity(2L);
        productViewModel.setManufacturedDate(dateTime);
        productViewModel.setExpiryDate(dateTime);
        productViewModel.setPurchasedDate(dateTime);
        productViewModel.setCategory("Test");
        productViewModel.setSuppliedDate(dateTime);
        return Stream.of(
                Arguments.of(productViewModel)
        );
    }
}
package com.company.go.inventory.product.repo;

import com.company.go.inventory.order.OrderEntity;
import com.company.go.inventory.product.Constants;
import com.company.go.inventory.product.ProductEntity;
import com.company.go.inventory.product.ProductTypeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {SpringBootConfiguration.class})
@EntityScan(value = "com.company.go")
@DataJpaTest
@Import({ProductRepository.class})
public class ProductRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetProductWithMaxId(){
        ProductEntity productEntity  = repository.getProduct(0L);
        assertNull(productEntity);
        Long id = repository.getMaxId(ProductEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get product")
    public void  testInsertAndGetProduct(ProductEntity entity){
        repository.createProduct(entity);
        List<ProductEntity> productEntities = repository.getAllRows(ProductEntity.class);
        Long expectedId = productEntities.get(0).getId();
        ProductEntity productEntity  = repository.getProduct(1L);
        assertNotNull(productEntity);
        assertEquals(productEntity.getName(), entity.getName());
        Long id = repository.getMaxId(ProductEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(ProductEntity entity){
        List<ProductEntity> productEntitiesFirst  = repository.getAllRows(ProductEntity.class);
        assertNotNull(productEntitiesFirst);
        assertEquals(0, productEntitiesFirst.size());
        repository.createProduct(entity);
        List<ProductEntity> productEntitiesSecond  = repository.getAllRows(ProductEntity.class);
        assertNotNull(productEntitiesSecond);
        assertEquals(1, productEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete product")
    public void testDeleteProduct(ProductEntity entity){
        repository.createProduct(entity);
        List<ProductEntity> productEntities  = repository.getAllRows(ProductEntity.class);
        assertNotNull(productEntities);
        assertEquals(1, productEntities.size());
        repository.deleteProduct(productEntities.get(0).getId());
        productEntities = repository.getAllRows(ProductEntity.class);
        assertNotNull(productEntities);
        assertEquals(0, productEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        ProductTypeEntity typeEntity = new ProductTypeEntity();
        typeEntity.setDateCreated(legacyDate);
        typeEntity.setCategory(Constants.ProductCategory.CONSUMABLE);
        typeEntity.setLastChanged(legacyDate);
        typeEntity.setValue("Test value");
        typeEntity.setRegistererName("Tester");
        typeEntity.setCategoryStatus(Constants.ProductCategoryStatus.ACTIVE);


        ProductEntity entity = new ProductEntity();
        entity.setAmount(0.0);
        entity.setCurrency(Currency.getInstance("USD"));
        entity.setDescription("Test");
        entity.setManufacturedDate(dateTime);
        entity.setSuppliedDate(dateTime);
        entity.setExpiryDate(dateTime);
        entity.setPurchasedDate(dateTime);
        entity.setLastChanged(legacyDate);
        entity.setName("test");
        entity.setDateCreated(legacyDate);
        entity.setQuantity(0L);
        entity.setStatus(Constants.ProductStatus.INACTIVE);
        entity.setSubStatus(Constants.ProductStore.READY_FOR_DISPOSAL);
        entity.setRegistererName("Tester");

        return Stream.of(
                Arguments.of(entity, typeEntity)
        );
    }


}

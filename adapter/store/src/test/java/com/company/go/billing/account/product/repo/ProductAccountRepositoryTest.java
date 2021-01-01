package com.company.go.billing.account.product.repo;

import com.company.go.billing.account.Constants;
import com.company.go.billing.account.product.ProductAccountEntity;
import com.company.go.billing.account.product.repo.ProductAccountRepository;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes = {SpringBootConfiguration.class})
@EntityScan(value = "com.company.go")
@DataJpaTest
@Import({ProductAccountRepository.class})
public class ProductAccountRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ProductAccountRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetProductAccountWithMaxId(){
        ProductAccountEntity productAccountEntity  = repository.getProductAccount(0L);
        assertNull(productAccountEntity);
        Long id = repository.getMaxId(ProductAccountEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get productAccount")
    public void  testInsertAndGetProductAccount(ProductAccountEntity entity){
        repository.createProductAccount(entity);
        List<ProductAccountEntity> productAccountEntities = repository.getAllRows(ProductAccountEntity.class);
        Long expectedId = productAccountEntities.get(0).getId();
        ProductAccountEntity productAccountEntity  = repository.getProductAccount(expectedId);
        assertNotNull(productAccountEntity);
        assertEquals(productAccountEntity.getAggregateAmount(), entity.getAggregateAmount());
        Long id = repository.getMaxId(ProductAccountEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(ProductAccountEntity entity){
        List<ProductAccountEntity> productAccountEntitiesFirst  = repository.getAllRows(ProductAccountEntity.class);
        assertNotNull(productAccountEntitiesFirst);
        assertEquals(0, productAccountEntitiesFirst.size());
        repository.createProductAccount(entity);
        List<ProductAccountEntity> productAccountEntitiesSecond  = repository.getAllRows(ProductAccountEntity.class);
        assertNotNull(productAccountEntitiesSecond);
        assertEquals(1, productAccountEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete productAccount")
    public void testDeleteProductAccount(ProductAccountEntity entity){
        repository.createProductAccount(entity);
        List<ProductAccountEntity> productAccountEntities  = repository.getAllRows(ProductAccountEntity.class);
        assertNotNull(productAccountEntities);
        assertEquals(1, productAccountEntities.size());
        repository.deleteProductAccount(productAccountEntities.get(0).getId());
        productAccountEntities = repository.getAllRows(ProductAccountEntity.class);
        assertNotNull(productAccountEntities);
        assertEquals(0, productAccountEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        ProductAccountEntity entity = new ProductAccountEntity();
        entity.setLastChanged(legacyDate);
        entity.setAggregateAmount(0.0);
        entity.setTypeCount(0L);
        entity.setType(Constants.Type.AGGREGATE.name());
        entity.setKind(Constants.Kind.CREDIT);
        entity.setDateCreated(legacyDate);
        entity.setRegistererName("Tester");

        return Stream.of(
                Arguments.of(entity)
        );
    }

}

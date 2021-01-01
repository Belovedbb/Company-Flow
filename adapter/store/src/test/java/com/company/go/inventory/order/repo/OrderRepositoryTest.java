package com.company.go.inventory.order.repo;

import com.company.go.inventory.order.Constants;
import com.company.go.inventory.order.OrderEntity;
import com.company.go.inventory.order.OrderEntryEntity;
import com.company.go.inventory.product.ProductEntity;
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
@Import({OrderRepository.class})
public class OrderRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetOrderWithMaxId(){
        OrderEntity orderEntity  = repository.getOrder(0L);
        assertNull(orderEntity);
        Long id = repository.getMaxId(OrderEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get order")
    public void  testInsertAndGetOrder(OrderEntity entity){
        repository.createOrder(entity);
        List<OrderEntity> orderEntities = repository.getAllRows(OrderEntity.class);
        Long expectedId = orderEntities.get(0).getId();
        OrderEntity orderEntity  = repository.getOrder(expectedId);
        assertNotNull(orderEntity);
        assertEquals(orderEntity.getDescription(), entity.getDescription());
        Long id = repository.getMaxId(OrderEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(OrderEntity entity){
        List<OrderEntity> orderEntitiesFirst  = repository.getAllRows(OrderEntity.class);
        assertNotNull(orderEntitiesFirst);
        assertEquals(0, orderEntitiesFirst.size());
        repository.createOrder(entity);
        List<OrderEntity> orderEntitiesSecond  = repository.getAllRows(OrderEntity.class);
        assertNotNull(orderEntitiesSecond);
        assertEquals(1, orderEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete order")
    public void testDeleteOrder(OrderEntity entity){
        repository.createOrder(entity);
        List<OrderEntity> orderEntities  = repository.getAllRows(OrderEntity.class);
        assertNotNull(orderEntities);
        assertEquals(1, orderEntities.size());
        repository.deleteOrder(orderEntities.get(0).getId());
        orderEntities = repository.getAllRows(OrderEntity.class);
        assertNotNull(orderEntities);
        assertEquals(0, orderEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        OrderEntryEntity entryEntity = new OrderEntryEntity();
        entryEntity.setDateCreated(legacyDate);
        entryEntity.setQuantity(0L);
        entryEntity.setProduct(new ProductEntity());
        entryEntity.setLastChanged(legacyDate);
        entryEntity.setRegistererName("Tester");


        OrderEntity entity = new OrderEntity();
        entity.setDescription("Test");
        entity.setPurchasedDate(dateTime);
        entity.setLastChanged(legacyDate);
        entity.setDateCreated(legacyDate);
        entity.setStatus(Constants.OrderStatus.CLOSED);
        entity.setRegistererName("Tester");

        return Stream.of(
                Arguments.of(entity, entryEntity)
        );
    }


}
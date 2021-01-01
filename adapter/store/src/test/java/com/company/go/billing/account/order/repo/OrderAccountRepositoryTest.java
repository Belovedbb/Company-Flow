package com.company.go.billing.account.order.repo;

import com.company.go.billing.account.Constants;
import com.company.go.billing.account.order.OrderAccountEntity;
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
@Import({OrderAccountRepository.class})
public class OrderAccountRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderAccountRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetOrderAccountWithMaxId(){
        OrderAccountEntity orderAccountEntity  = repository.getOrderAccount(0L);
        assertNull(orderAccountEntity);
        Long id = repository.getMaxId(OrderAccountEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get orderAccount")
    public void  testInsertAndGetOrderAccount(OrderAccountEntity entity){
        repository.createOrderAccount(entity);
        List<OrderAccountEntity> orderAccountEntities = repository.getAllRows(OrderAccountEntity.class);
        Long expectedId = orderAccountEntities.get(0).getId();
        OrderAccountEntity orderAccountEntity  = repository.getOrderAccount(expectedId);
        assertNotNull(orderAccountEntity);
        assertEquals(orderAccountEntity.getAggregateAmount(), entity.getAggregateAmount());
        Long id = repository.getMaxId(OrderAccountEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(OrderAccountEntity entity){
        List<OrderAccountEntity> orderAccountEntitiesFirst  = repository.getAllRows(OrderAccountEntity.class);
        assertNotNull(orderAccountEntitiesFirst);
        assertEquals(0, orderAccountEntitiesFirst.size());
        repository.createOrderAccount(entity);
        List<OrderAccountEntity> orderAccountEntitiesSecond  = repository.getAllRows(OrderAccountEntity.class);
        assertNotNull(orderAccountEntitiesSecond);
        assertEquals(1, orderAccountEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete orderAccount")
    public void testDeleteOrderAccount(OrderAccountEntity entity){
        repository.createOrderAccount(entity);
        List<OrderAccountEntity> orderAccountEntities  = repository.getAllRows(OrderAccountEntity.class);
        assertNotNull(orderAccountEntities);
        assertEquals(1, orderAccountEntities.size());
        repository.deleteOrderAccount(orderAccountEntities.get(0).getId());
        orderAccountEntities = repository.getAllRows(OrderAccountEntity.class);
        assertNotNull(orderAccountEntities);
        assertEquals(0, orderAccountEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        OrderAccountEntity entity = new OrderAccountEntity();
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

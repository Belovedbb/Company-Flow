package com.company.go.billing.account.performance.repo;

import com.company.go.billing.account.Constants;
import com.company.go.billing.account.performance.PerformanceAccountEntity;
import com.company.go.billing.account.performance.repo.PerformanceAccountRepository;
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
@Import({PerformanceAccountRepository.class})
public class PerformanceAccountRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PerformanceAccountRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetPerformanceAccountWithMaxId(){
        PerformanceAccountEntity performanceAccountEntity  = repository.getPerformanceAccount(0L);
        assertNull(performanceAccountEntity);
        Long id = repository.getMaxId(PerformanceAccountEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get performanceAccount")
    public void  testInsertAndGetPerformanceAccount(PerformanceAccountEntity entity){
        repository.createPerformanceAccount(entity);
        List<PerformanceAccountEntity> performanceAccountEntities = repository.getAllRows(PerformanceAccountEntity.class);
        Long expectedId = performanceAccountEntities.get(0).getId();
        PerformanceAccountEntity performanceAccountEntity  = repository.getPerformanceAccount(expectedId);
        assertNotNull(performanceAccountEntity);
        assertEquals(performanceAccountEntity.getAggregateAmount(), entity.getAggregateAmount());
        Long id = repository.getMaxId(PerformanceAccountEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(PerformanceAccountEntity entity){
        List<PerformanceAccountEntity> performanceAccountEntitiesFirst  = repository.getAllRows(PerformanceAccountEntity.class);
        assertNotNull(performanceAccountEntitiesFirst);
        assertEquals(0, performanceAccountEntitiesFirst.size());
        repository.createPerformanceAccount(entity);
        List<PerformanceAccountEntity> performanceAccountEntitiesSecond  = repository.getAllRows(PerformanceAccountEntity.class);
        assertNotNull(performanceAccountEntitiesSecond);
        assertEquals(1, performanceAccountEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete performanceAccount")
    public void testDeletePerformanceAccount(PerformanceAccountEntity entity){
        repository.createPerformanceAccount(entity);
        List<PerformanceAccountEntity> performanceAccountEntities  = repository.getAllRows(PerformanceAccountEntity.class);
        assertNotNull(performanceAccountEntities);
        assertEquals(1, performanceAccountEntities.size());
        repository.deletePerformanceAccount(performanceAccountEntities.get(0).getId());
        performanceAccountEntities = repository.getAllRows(PerformanceAccountEntity.class);
        assertNotNull(performanceAccountEntities);
        assertEquals(0, performanceAccountEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        PerformanceAccountEntity entity = new PerformanceAccountEntity();
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

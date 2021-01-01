package com.company.go.archive.performance.repo;

import com.company.go.archive.performance.Constants;
import com.company.go.archive.performance.PerformanceEntity;
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
@Import({PerformanceRepository.class})
public class PerformanceRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PerformanceRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetPerformanceWithMaxId(){
        PerformanceEntity performanceEntity  = repository.getPerformance(0L);
        assertNull(performanceEntity);
        Long id = repository.getMaxId(PerformanceEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get performance")
    public void  testInsertAndGetPerformance(PerformanceEntity entity){
        repository.createPerformance(entity);
        List<PerformanceEntity> performanceEntities = repository.getAllRows(PerformanceEntity.class);
        Long expectedId = performanceEntities.get(0).getId();
        PerformanceEntity performanceEntity  = repository.getPerformance(expectedId);
        assertNotNull(performanceEntity);
        assertEquals(performanceEntity.getBonusPoint(), entity.getBonusPoint());
        Long id = repository.getMaxId(PerformanceEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(PerformanceEntity entity){
        List<PerformanceEntity> performanceEntitiesFirst  = repository.getAllRows(PerformanceEntity.class);
        assertNotNull(performanceEntitiesFirst);
        assertEquals(0, performanceEntitiesFirst.size());
        repository.createPerformance(entity);
        List<PerformanceEntity> performanceEntitiesSecond  = repository.getAllRows(PerformanceEntity.class);
        assertNotNull(performanceEntitiesSecond);
        assertEquals(1, performanceEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete performance")
    public void testDeletePerformance(PerformanceEntity entity){
        repository.createPerformance(entity);
        List<PerformanceEntity> performanceEntities  = repository.getAllRows(PerformanceEntity.class);
        assertNotNull(performanceEntities);
        assertEquals(1, performanceEntities.size());
        repository.deletePerformance(performanceEntities.get(0).getId());
        performanceEntities = repository.getAllRows(PerformanceEntity.class);
        assertNotNull(performanceEntities);
        assertEquals(0, performanceEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();


        PerformanceEntity entity = new PerformanceEntity();
        entity.setLastChanged(legacyDate);
        entity.setBonusPoint(0.0);
        entity.setAverageMonthlyPerformance(0.0);
        entity.setDateCreated(legacyDate);
        entity.setStatus(Constants.PerformanceStatus.POOR);
        entity.setRegistererName("Tester");

        return Stream.of(
                Arguments.of(entity)
        );
    }

}

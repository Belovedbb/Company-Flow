package com.company.go.archive.staff.repo;

import com.company.go.archive.staff.Constants;
import com.company.go.archive.staff.StaffEntity;
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
@Import({StaffRepository.class})
public class StaffRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private StaffRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies and max Id")
    public void testRepoGetStaffWithMaxId(){
        StaffEntity staffEntity  = repository.getStaff(0L);
        assertNull(staffEntity);
        Long id = repository.getMaxId(StaffEntity.class);
        assertNull(id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get staff")
    public void  testInsertAndGetStaff(StaffEntity entity){
        repository.createStaff(entity);
        List<StaffEntity> staffEntities = repository.getAllRows(StaffEntity.class);
        Long expectedId = staffEntities.get(0).getId();
        StaffEntity staffEntity  = repository.getStaff(expectedId);
        assertNotNull(staffEntity);
        assertEquals(staffEntity.getPayment(), entity.getPayment());
        Long id = repository.getMaxId(StaffEntity.class);
        assertNotNull(id);
        assertEquals(expectedId, id);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(StaffEntity entity){
        List<StaffEntity> staffEntitiesFirst  = repository.getAllRows(StaffEntity.class);
        assertNotNull(staffEntitiesFirst);
        assertEquals(0, staffEntitiesFirst.size());
        repository.createStaff(entity);
        List<StaffEntity> staffEntitiesSecond  = repository.getAllRows(StaffEntity.class);
        assertNotNull(staffEntitiesSecond);
        assertEquals(1, staffEntitiesSecond.size());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Delete staff")
    public void testDeleteStaff(StaffEntity entity){
        repository.createStaff(entity);
        List<StaffEntity> staffEntities  = repository.getAllRows(StaffEntity.class);
        assertNotNull(staffEntities);
        assertEquals(1, staffEntities.size());
        repository.deleteStaff(staffEntities.get(0).getId());
        staffEntities = repository.getAllRows(StaffEntity.class);
        assertNotNull(staffEntities);
        assertEquals(0, staffEntities.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        StaffEntity entity = new StaffEntity();
        entity.setLastChanged(legacyDate);
        entity.setPayment(0.0);
        entity.setStatus(Constants.StaffStatus.INACTIVE);
        entity.setDateCreated(legacyDate);
        entity.setRegistererName("Tester");

        return Stream.of(
                Arguments.of(entity)
        );
    }

}

package com.company.go.global.repo;

import com.company.go.global.Constants;
import com.company.go.global.UserEntity;
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
@Import({UserRepository.class})
public class UserRepositoryTest {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("Check all injected dependencies")
    void checkInjections(){
        assertNotNull(dataSource);
        assertNotNull(jdbcTemplate);
        assertNotNull(entityManager);
    }

    @Test
    @DisplayName("Check all injected dependencies")
    public void testRepoGetUserWithMaxId(){
        UserEntity userEntity  = repository.getUser(0L);
        assertNull(userEntity);
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Insert and get user")
    public void  testInsertAndGetUser(UserEntity entity){
        repository.saveUser(entity);
        List<UserEntity> userEntities = repository.getAllUsers();
        Long expectedId = userEntities.get(0).getId();
        UserEntity userEntity  = repository.getUser(expectedId);
        assertNotNull(userEntity);
        assertEquals(userEntity.getEmail(), entity.getEmail());
    }

    @ParameterizedTest
    @MethodSource("getEntityFactory")
    @DisplayName("Get all rows")
    public void testRepoGetAllRows(UserEntity entity){
        List<UserEntity> userEntitiesFirst  = repository.getAllUsers();
        assertNotNull(userEntitiesFirst);
        assertEquals(0, userEntitiesFirst.size());
        repository.saveUser(entity);
        List<UserEntity> userEntitiesSecond  = repository.getAllUsers();
        assertNotNull(userEntitiesSecond);
        assertEquals(1, userEntitiesSecond.size());
    }

    private static Stream<Arguments> getEntityFactory(){
        Date legacyDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        LocalDateTime dateTime = LocalDateTime.now();

        UserEntity entity = new UserEntity();
        entity.setStatus(Constants.Status.INACTIVE);
        entity.setEmail("tester@test.com");
        entity.setPassword("*******");

        return Stream.of(
                Arguments.of(entity)
        );
    }

}

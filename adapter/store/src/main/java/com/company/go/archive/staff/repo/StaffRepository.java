package com.company.go.archive.staff.repo;

import com.company.go.Utilities;
import com.company.go.archive.staff.StaffEntity;
import com.company.go.global.UserEntity;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StaffRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateStaff(StaffEntity staff) throws HibernateException {
        if(staff.getUserType() != null && staff.getUserType().getId() != null) {
            UserEntity userEntity = entityManager.find(UserEntity.class, staff.getUserType().getId());
            staff.setUserType(userEntity);
        }
        entityManager.merge(staff);
    }

    public void createStaff(StaffEntity staff) throws HibernateException{
        entityManager.persist(staff);
    }


    public StaffEntity getStaff(Long id) throws HibernateException{
        return entityManager.find(StaffEntity.class, id);
    }

    public void deleteStaff(Long id) throws HibernateException{
        StaffEntity staff = getStaff(id);
        entityManager.remove(staff);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public StaffEntity findStaffById(BigInteger staffId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StaffEntity> queryCriteria = criteriaBuilder.createQuery(StaffEntity.class);
        Root<StaffEntity> staffType = queryCriteria.from(StaffEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(staffType.get("staffId"), staffId);
        queryCriteria.where(valueProductType);
        TypedQuery<StaffEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<StaffEntity> filterStaff(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StaffEntity> queryCriteria = criteriaBuilder.createQuery(StaffEntity.class);
        Root<StaffEntity> staff = queryCriteria.from(StaffEntity.class);
        List<Predicate> staffPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(staff.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(staffPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<StaffEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }


    public List<StaffEntity> filterStaff(List<Utilities.Filter> filters, Utilities.FilterCondition condition){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<StaffEntity> queryCriteria = criteriaBuilder.createQuery(StaffEntity.class);
        Root<StaffEntity> staff = queryCriteria.from(StaffEntity.class);
        List<Predicate> predicateResponse = new ArrayList<>();
        List<List<Predicate> > mainPredicates = new ArrayList<>();
        for(Utilities.Filter filter: filters){
            if(filter.getKlass().equals(StaffEntity.class) ){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterStaffEntity(param, criteriaBuilder, staff);
                mainPredicates.add(predicates);
            }
            if(filter.getKlass().equals(UserEntity.class)){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterUserEntity(param, criteriaBuilder, staff);
                mainPredicates.add(predicates);
            }
        }
        if(condition != null){
            if(condition == Utilities.FilterCondition.AND){
                List<Predicate> betweenPropertiesAnd = mainPredicates.stream()
                        .reduce(List.of(), (accumulatedPredicates, predicates) -> Collections.singletonList(criteriaBuilder.and(predicates.toArray(Predicate[]::new))));
                Predicate pred = betweenPropertiesAnd.stream().reduce(null, (accumulatedPredicate, predicate) -> criteriaBuilder.and(List.of(predicate).toArray(Predicate[]::new)));
                predicateResponse  = List.of(pred);
            }else if(condition == Utilities.FilterCondition.OR){
                List<Predicate> betweenPropertiesAnd = mainPredicates.stream()
                        .reduce(List.of(), (accumulatedPredicates, predicates) -> Collections.singletonList(criteriaBuilder.or(predicates.toArray(Predicate[]::new))));
                Predicate pred = betweenPropertiesAnd.stream().reduce(null, (accumulatedPredicate, predicate) -> criteriaBuilder.or(List.of(predicate).toArray(Predicate[]::new)));
                predicateResponse = List.of(pred);
            }else{
                if(!CollectionUtils.isEmpty(mainPredicates)){
                    predicateResponse = mainPredicates.get(0);
                }
            }
            queryCriteria.where(predicateResponse.toArray(Predicate[]::new));
        }
        TypedQuery<StaffEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }

    private List<Predicate> buildFilterStaffEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<StaffEntity> staff){
        List<Predicate> productPredicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            productPredicateClauses = properties.keySet().stream()
                    .map(key -> criteriaBuilder.equal(staff.get(key), properties.get(key)))
                    .collect(Collectors.toList());
        }
        return productPredicateClauses;
    }

    private List<Predicate> buildFilterUserEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<StaffEntity> staff){
        List<Predicate> predicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            Join<StaffEntity, UserEntity> joinUser = staff.join("type", JoinType.INNER);
            predicateClauses = properties.keySet().stream()
                    .map(key -> joinUser.on(criteriaBuilder.equal(joinUser.get(key), properties.get(key))).getOn())
                    .collect(Collectors.toList());
        }
        return predicateClauses;
    }

    public Long getMaxId(Class klazz) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> queryCriteria = criteriaBuilder.createQuery(klazz);
        Root<?> root = queryCriteria.from(klazz);
        queryCriteria.select(criteriaBuilder.max(root.get("id")));
        TypedQuery<Object> query = entityManager.createQuery(queryCriteria);
        return (Long) query.getSingleResult();
    }

}


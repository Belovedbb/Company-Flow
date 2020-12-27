package com.company.go.archive.performance.repo;

import com.company.go.archive.performance.PerformanceEntity;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PerformanceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updatePerformance(PerformanceEntity performance) throws HibernateException {
        entityManager.merge(performance);
    }

    public void createPerformance(PerformanceEntity performance) throws HibernateException{
        entityManager.persist(performance);
    }


    public PerformanceEntity getPerformance(Long id) throws HibernateException{
        return entityManager.find(PerformanceEntity.class, id);
    }

    public void deletePerformance(Long id) throws HibernateException{
        PerformanceEntity performance = getPerformance(id);
        entityManager.remove(performance);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public PerformanceEntity findPerformanceById(BigInteger performanceId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PerformanceEntity> queryCriteria = criteriaBuilder.createQuery(PerformanceEntity.class);
        Root<PerformanceEntity> performanceType = queryCriteria.from(PerformanceEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(performanceType.get("performanceId"), performanceId);
        queryCriteria.where(valueProductType);
        TypedQuery<PerformanceEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<PerformanceEntity> filterPerformance(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PerformanceEntity> queryCriteria = criteriaBuilder.createQuery(PerformanceEntity.class);
        Root<PerformanceEntity> performance = queryCriteria.from(PerformanceEntity.class);
        List<Predicate> performancePredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(performance.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(performancePredicateClauses.toArray(Predicate[]::new));
        TypedQuery<PerformanceEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
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
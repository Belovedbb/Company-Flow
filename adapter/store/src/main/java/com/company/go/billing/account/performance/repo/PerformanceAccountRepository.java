package com.company.go.billing.account.performance.repo;


import com.company.go.billing.account.performance.PerformanceAccountEntity;
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
public class PerformanceAccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updatePerformanceAccount(PerformanceAccountEntity performanceAccount) throws HibernateException {
        entityManager.merge(performanceAccount);
    }

    public void createPerformanceAccount(PerformanceAccountEntity performanceAccount) throws HibernateException{
        entityManager.persist(performanceAccount);
    }


    public PerformanceAccountEntity getPerformanceAccount(Long id) throws HibernateException{
        return entityManager.find(PerformanceAccountEntity.class, id);
    }

    public void deletePerformanceAccount(Long id) throws HibernateException{
        PerformanceAccountEntity performanceAccount = getPerformanceAccount(id);
        entityManager.remove(performanceAccount);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public PerformanceAccountEntity findPerformanceAccountById(BigInteger performanceAccountId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PerformanceAccountEntity> queryCriteria = criteriaBuilder.createQuery(PerformanceAccountEntity.class);
        Root<PerformanceAccountEntity> performanceAccountType = queryCriteria.from(PerformanceAccountEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(performanceAccountType.get("performanceAccountId"), performanceAccountId);
        queryCriteria.where(valueProductType);
        TypedQuery<PerformanceAccountEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<PerformanceAccountEntity> filterPerformanceAccount(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PerformanceAccountEntity> queryCriteria = criteriaBuilder.createQuery(PerformanceAccountEntity.class);
        Root<PerformanceAccountEntity> performanceAccount = queryCriteria.from(PerformanceAccountEntity.class);
        List<Predicate> performanceAccountPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(performanceAccount.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(performanceAccountPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<PerformanceAccountEntity> query = entityManager.createQuery(queryCriteria);
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


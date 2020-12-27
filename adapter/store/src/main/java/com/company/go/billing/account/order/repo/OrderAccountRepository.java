package com.company.go.billing.account.order.repo;

import com.company.go.billing.account.order.OrderAccountEntity;
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
public class OrderAccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateOrderAccount(OrderAccountEntity orderAccount) throws HibernateException {
        entityManager.merge(orderAccount);
    }

    public void createOrderAccount(OrderAccountEntity orderAccount) throws HibernateException{
        entityManager.persist(orderAccount);
    }


    public OrderAccountEntity getOrderAccount(Long id) throws HibernateException{
        return entityManager.find(OrderAccountEntity.class, id);
    }

    public void deleteOrderAccount(Long id) throws HibernateException{
        OrderAccountEntity orderAccount = getOrderAccount(id);
        entityManager.remove(orderAccount);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public OrderAccountEntity findOrderAccountById(BigInteger orderAccountId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderAccountEntity> queryCriteria = criteriaBuilder.createQuery(OrderAccountEntity.class);
        Root<OrderAccountEntity> orderAccountType = queryCriteria.from(OrderAccountEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(orderAccountType.get("orderAccountId"), orderAccountId);
        queryCriteria.where(valueProductType);
        TypedQuery<OrderAccountEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<OrderAccountEntity> filterOrderAccount(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderAccountEntity> queryCriteria = criteriaBuilder.createQuery(OrderAccountEntity.class);
        Root<OrderAccountEntity> orderAccount = queryCriteria.from(OrderAccountEntity.class);
        List<Predicate> orderAccountPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(orderAccount.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(orderAccountPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<OrderAccountEntity> query = entityManager.createQuery(queryCriteria);
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


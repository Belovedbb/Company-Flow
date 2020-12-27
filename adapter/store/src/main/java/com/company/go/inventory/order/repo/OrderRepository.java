package com.company.go.inventory.order.repo;

import com.company.go.Utilities;
import com.company.go.global.UserEntity;
import com.company.go.inventory.order.OrderEntity;
import com.company.go.inventory.product.ProductEntity;
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
public class OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateOrder(OrderEntity order) throws HibernateException {
        entityManager.merge(order);
    }

    public void createOrder(OrderEntity order) throws HibernateException{
        entityManager.persist(order);
    }


    public OrderEntity getOrder(Long id) throws HibernateException{
        return entityManager.find(OrderEntity.class, id);
    }

    public void deleteOrder(Long id) throws HibernateException{
        OrderEntity order = getOrder(id);
        entityManager.remove(order);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public OrderEntity findOrderById(BigInteger orderId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> queryCriteria = criteriaBuilder.createQuery(OrderEntity.class);
        Root<OrderEntity> productType = queryCriteria.from(OrderEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(productType.get("orderId"), orderId);
        queryCriteria.where(valueProductType);
        TypedQuery<OrderEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<OrderEntity> filterOrder(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> queryCriteria = criteriaBuilder.createQuery(OrderEntity.class);
        Root<OrderEntity> order = queryCriteria.from(OrderEntity.class);
        List<Predicate> productPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(order.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(productPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<OrderEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }

    public List<OrderEntity> filterOrder(List<Utilities.Filter> filters, Utilities.FilterCondition condition){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> queryCriteria = criteriaBuilder.createQuery(OrderEntity.class);
        Root<OrderEntity> order = queryCriteria.from(OrderEntity.class);
        List<Predicate> predicateResponse = new ArrayList<>();
        List<List<Predicate> > mainPredicates = new ArrayList<>();
        for(Utilities.Filter filter: filters){
            if(filter.getKlass().equals(OrderEntity.class) ){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterOrderEntity(param, criteriaBuilder, order);
                mainPredicates.add(predicates);
            }
            if(filter.getKlass().equals(UserEntity.class)){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterUserEntity(param, criteriaBuilder, order);
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
        TypedQuery<OrderEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }

    private List<Predicate> buildFilterOrderEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<OrderEntity> order){
        List<Predicate> productPredicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            productPredicateClauses = properties.keySet().stream()
                    .map(key -> criteriaBuilder.equal(order.get(key), properties.get(key)))
                    .collect(Collectors.toList());
        }
        return productPredicateClauses;
    }

    private List<Predicate> buildFilterUserEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<OrderEntity> order){
        List<Predicate> predicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            Join<OrderEntity, UserEntity> joinUser = order.join("companyRegisterer", JoinType.INNER);
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

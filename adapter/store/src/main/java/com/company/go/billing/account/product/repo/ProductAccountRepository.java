package com.company.go.billing.account.product.repo;

import com.company.go.billing.account.product.ProductAccountEntity;
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
public class ProductAccountRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateProductAccount(ProductAccountEntity productAccount) throws HibernateException {
        entityManager.merge(productAccount);
    }

    public void createProductAccount(ProductAccountEntity productAccount) throws HibernateException{
        entityManager.persist(productAccount);
    }


    public ProductAccountEntity getProductAccount(Long id) throws HibernateException{
        return entityManager.find(ProductAccountEntity.class, id);
    }

    public void deleteProductAccount(Long id) throws HibernateException{
        ProductAccountEntity productAccount = getProductAccount(id);
        entityManager.remove(productAccount);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public ProductAccountEntity findProductAccountById(BigInteger productAccountId) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductAccountEntity> queryCriteria = criteriaBuilder.createQuery(ProductAccountEntity.class);
        Root<ProductAccountEntity> productAccountType = queryCriteria.from(ProductAccountEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(productAccountType.get("productAccountId"), productAccountId);
        queryCriteria.where(valueProductType);
        TypedQuery<ProductAccountEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public List<ProductAccountEntity> filterProductAccount(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductAccountEntity> queryCriteria = criteriaBuilder.createQuery(ProductAccountEntity.class);
        Root<ProductAccountEntity> productAccount = queryCriteria.from(ProductAccountEntity.class);
        List<Predicate> productAccountPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(productAccount.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(productAccountPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<ProductAccountEntity> query = entityManager.createQuery(queryCriteria);
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

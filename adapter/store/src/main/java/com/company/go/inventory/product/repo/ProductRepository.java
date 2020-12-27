package com.company.go.inventory.product.repo;

import com.company.go.inventory.product.ProductEntity;
import com.company.go.inventory.product.ProductTypeEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void updateProduct(ProductEntity product) throws HibernateException{
        entityManager.merge(product);
    }

    public void createProduct(ProductEntity product) throws HibernateException{
        entityManager.persist(product);
    }

    public void createProductType(ProductTypeEntity productType) throws  HibernateException{
        entityManager.persist(productType);
    }

    public ProductEntity getProduct(Long id) throws HibernateException{
        return entityManager.find(ProductEntity.class, id);
    }

    public void deleteProduct(Long id) throws HibernateException{
        ProductEntity product = getProduct(id);
        entityManager.remove(product);
    }

    public <T> List<T> getAllRows(Class<T> klazz){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(klazz);
        criteria.from(klazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    public List<ProductEntity> filterProduct(Map<String, Object> parameters){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> queryCriteria = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> product = queryCriteria.from(ProductEntity.class);
        List<Predicate> productPredicateClauses = parameters.keySet().stream()
                .map(key -> criteriaBuilder.equal(product.get(key), parameters.get(key)))
                .collect(Collectors.toList());
        queryCriteria.where(productPredicateClauses.toArray(Predicate[]::new));
        TypedQuery<ProductEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }

    public void saveOrUpdateProductCategory(ProductTypeEntity productType){
        Session hibernateSession = entityManager.unwrap(Session.class);
        hibernateSession.saveOrUpdate(productType);
    }

    public ProductTypeEntity findProductTypeByName(String name) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductTypeEntity> queryCriteria = criteriaBuilder.createQuery(ProductTypeEntity.class);
        Root<ProductTypeEntity> productType = queryCriteria.from(ProductTypeEntity.class);
        Predicate valueProductType = criteriaBuilder.equal(productType.get("value"), name);
        queryCriteria.where(valueProductType);
        TypedQuery<ProductTypeEntity> query = entityManager.createQuery(queryCriteria);
        return query.getSingleResult();
    }

    public Long getMaxId(Class klazz) throws NoResultException{
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> queryCriteria = criteriaBuilder.createQuery(klazz);
        Root<?> root = queryCriteria.from(klazz);
        queryCriteria.select(criteriaBuilder.max(root.get("id")));
        TypedQuery<Object> query = entityManager.createQuery(queryCriteria);
        return (Long) query.getSingleResult();
    }

}

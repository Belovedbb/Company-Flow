package com.company.go.archive.staff.repo;

import com.company.go.archive.staff.StaffEntity;
import com.company.go.global.UserEntity;
import com.company.go.global.repo.UserRepository;
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


    public Long getMaxId(Class klazz) throws NoResultException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object> queryCriteria = criteriaBuilder.createQuery(klazz);
        Root<?> root = queryCriteria.from(klazz);
        queryCriteria.select(criteriaBuilder.max(root.get("id")));
        TypedQuery<Object> query = entityManager.createQuery(queryCriteria);
        return (Long) query.getSingleResult();
    }

}


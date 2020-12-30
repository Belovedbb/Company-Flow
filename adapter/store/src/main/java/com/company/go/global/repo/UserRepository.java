package com.company.go.global.repo;

import com.company.go.global.UserEntity;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateUser(UserEntity user) throws HibernateException{
        user.setProfilePicture(findUserByEmail(user.getEmail()).getProfilePicture());
        entityManager.merge(user);
    }

    public UserEntity findUserByEmail(String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> queryCriteria = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> companyUser = queryCriteria.from(UserEntity.class);
        Predicate emailUser = criteriaBuilder.equal(companyUser.get("email"), email);
        queryCriteria.where(emailUser);
        TypedQuery<UserEntity> query = entityManager.createQuery(queryCriteria);
        return CollectionUtils.isEmpty(query.getResultList()) ? null : query.getResultList().get(0);
    }

    public void saveUser(UserEntity user) {
        entityManager.persist(user);
    }

    public List<UserEntity> getAllUsers(){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = builder.createQuery(UserEntity.class);
        criteria.from(UserEntity.class);
        return entityManager.createQuery(criteria).getResultList();
    }

    public UserEntity getUser(Long id) throws HibernateException {
        return entityManager.find(UserEntity.class, id);
    }

}

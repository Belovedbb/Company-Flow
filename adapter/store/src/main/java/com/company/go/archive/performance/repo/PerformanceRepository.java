package com.company.go.archive.performance.repo;

import com.company.go.Utilities;
import com.company.go.archive.performance.PerformanceEntity;
import com.company.go.archive.staff.StaffEntity;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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

    public List<PerformanceEntity> filterPerformance(List<Utilities.Filter> filters, Utilities.FilterCondition condition){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PerformanceEntity> queryCriteria = criteriaBuilder.createQuery(PerformanceEntity.class);
        Root<PerformanceEntity> performance = queryCriteria.from(PerformanceEntity.class);
        List<Predicate> predicateResponse = new ArrayList<>();
        List<List<Predicate> > mainPredicates = new ArrayList<>();
        for(Utilities.Filter filter: filters){
            if(filter.getKlass().equals(PerformanceEntity.class) ){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterPerformanceEntity(param, criteriaBuilder, performance);
                mainPredicates.add(predicates);
            }
            if(filter.getKlass().equals(StaffEntity.class)){
                Map<String, Object> param = filter.getProperties();
                List<Predicate> predicates = buildFilterStaffEntity(param, criteriaBuilder, performance);
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
        TypedQuery<PerformanceEntity> query = entityManager.createQuery(queryCriteria);
        return query.getResultList();
    }

    private List<Predicate> buildFilterPerformanceEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<PerformanceEntity> performance){
        List<Predicate> productPredicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            productPredicateClauses = properties.keySet().stream().map( key -> {
                Predicate predicate = null;
                if(key.equalsIgnoreCase("dateCreated")){
                    Date[] betweenDate = betweenDate((Date) properties.get(key));
                    predicate = criteriaBuilder.between(performance.<Date>get(key), betweenDate[0], betweenDate[1]);
                }else{
                    predicate = criteriaBuilder.equal(performance.get(key), properties.get(key));
                }
                return predicate;
            }).collect(Collectors.toList());
        }
        return productPredicateClauses;
    }

    private List<Predicate> buildFilterStaffEntity(Map<String, Object> properties, CriteriaBuilder criteriaBuilder, Root<PerformanceEntity> performance){
        List<Predicate> predicateClauses = null;
        if(!CollectionUtils.isEmpty(properties)) {
            Join<PerformanceEntity, StaffEntity> joinPerformance = performance.join("staff", JoinType.INNER);
            predicateClauses = properties.keySet().stream()
                    .map(key -> joinPerformance.on(criteriaBuilder.equal(joinPerformance.get(key), properties.get(key))).getOn())
                    .collect(Collectors.toList());
        }
        return predicateClauses;
    }

    private Date[] betweenDate(Date initialDate){
        Date[] dates = new Date[2];
        LocalDate factor = initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        try {
            String dateFormat = String.format("%s-%s-%s", factor.getYear(), factor.getMonthValue(), factor.getDayOfMonth());
            dates[0] =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateFormat + " 00:00:00");
            dates[1] = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateFormat + " 23:59:59");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
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
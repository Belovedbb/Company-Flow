package com.company.go.archive.performance;

import com.company.go.Utilities;
import com.company.go.application.port.out.archive.UpdatePerformancePort;
import com.company.go.archive.mapper.PerformanceMapper;
import com.company.go.archive.performance.repo.PerformanceRepository;
import com.company.go.domain.archive.performance.Performance;
import com.company.go.global.repo.UserRepository;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class PerformanceStoreAdapter implements UpdatePerformancePort {

    private UserRepository userRepo;
    private PerformanceRepository performanceRepo;

    @Autowired
    public PerformanceStoreAdapter(UserRepository userRepo, PerformanceRepository performanceRepo) {
        this.userRepo = userRepo;
        this.performanceRepo = performanceRepo;
    }


    @Override
    public boolean storePerformance(Performance performance) {
        try{
            PerformanceEntity performanceEntity = PerformanceMapper.mapPerformanceDomainToPerformanceEntity(performance);
            performanceRepo.createPerformance(performanceEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePerformance(Long id, Performance currentPerformance) {
        try {
            PerformanceEntity performanceEntity = PerformanceMapper.mapPerformanceDomainToPerformanceEntity(currentPerformance);
            performanceRepo.updatePerformance(performanceEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Performance getPerformance(Long id) {
        return PerformanceMapper.mapPerformanceEntityToPerformanceDomain(performanceRepo.getPerformance(id));
    }

    @Override
    public boolean removePerformance(Long id) {
        try{
            performanceRepo.deletePerformance(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Long getPerformanceMaxId() {
        try{
            return performanceRepo.getMaxId(PerformanceEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<Performance> getTotalPerformance() {
        return performanceRepo.getAllRows(PerformanceEntity.class).stream()
                .map(PerformanceMapper::mapPerformanceEntityToPerformanceDomain).collect(Collectors.toList());
    }

    @Override
    public List<Performance> getTotalFilteredPerformance(Map<String, Object> parameters) {
        return performanceRepo.filterPerformance(parameters).stream()
                .map(PerformanceMapper::mapPerformanceEntityToPerformanceDomain).collect(Collectors.toList());
    }

    @Override
    public List<Performance> getTotalFilteredPerformance(List<Utilities.Filter> filterList, Utilities.FilterCondition condition) {
        return performanceRepo.filterPerformance(PerformanceMapper.mapErasureFilter(filterList), condition).stream().map(PerformanceMapper::mapPerformanceEntityToPerformanceDomain).collect(Collectors.toList());
    }

}
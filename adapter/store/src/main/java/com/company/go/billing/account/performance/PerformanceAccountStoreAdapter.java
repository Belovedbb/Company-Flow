package com.company.go.billing.account.performance;

import com.company.go.application.port.out.billing.UpdatePerformanceAccountPort;
import com.company.go.billing.account.performance.repo.PerformanceAccountRepository;
import com.company.go.billing.mapper.PerformanceAccountMapper;
import com.company.go.domain.billing.PerformanceAccount;
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
public class PerformanceAccountStoreAdapter implements UpdatePerformanceAccountPort {
    
    private PerformanceAccountRepository performanceAccountRepository;

    @Autowired
    PerformanceAccountStoreAdapter(PerformanceAccountRepository performanceAccountRepository){
        this.performanceAccountRepository = performanceAccountRepository;
    }

    @Override
    public boolean storePerformanceAccount(PerformanceAccount performanceAccount) {
        try{
            PerformanceAccountEntity performanceAccountEntity = PerformanceAccountMapper.mapPerformanceAccountDomainToPerformanceAccountEntity(performanceAccount);
            performanceAccountRepository.createPerformanceAccount(performanceAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePerformanceAccount(Long id, PerformanceAccount currentPerformanceAccount) {
        try {
            PerformanceAccountEntity performanceAccountEntity = PerformanceAccountMapper.mapPerformanceAccountDomainToPerformanceAccountEntity(currentPerformanceAccount);
            performanceAccountRepository.updatePerformanceAccount(performanceAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public PerformanceAccount getPerformanceAccount(Long id) {
        return PerformanceAccountMapper.mapPerformanceAccountEntityToPerformanceAccountDomain(performanceAccountRepository.getPerformanceAccount(id));
    }

    @Override
    public boolean removePerformanceAccount(Long id) {
        try{
            performanceAccountRepository.deletePerformanceAccount(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Long getPerformanceAccountMaxId() {
        try{
            return performanceAccountRepository.getMaxId(PerformanceAccountEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<PerformanceAccount> getTotalPerformanceAccount() {
        return performanceAccountRepository.getAllRows(PerformanceAccountEntity.class).stream()
                .map(PerformanceAccountMapper::mapPerformanceAccountEntityToPerformanceAccountDomain).collect(Collectors.toList());
    }

    @Override
    public List<PerformanceAccount> getTotalFilteredPerformanceAccount(Map<String, Object> parameters) {
        return performanceAccountRepository.filterPerformanceAccount(parameters).stream()
                .map(PerformanceAccountMapper::mapPerformanceAccountEntityToPerformanceAccountDomain).collect(Collectors.toList());
    }
}

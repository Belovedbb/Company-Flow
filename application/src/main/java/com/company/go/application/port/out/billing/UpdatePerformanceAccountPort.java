package com.company.go.application.port.out.billing;

import com.company.go.domain.billing.PerformanceAccount;

import java.util.List;
import java.util.Map;

public interface UpdatePerformanceAccountPort {

    boolean storePerformanceAccount(PerformanceAccount performanceAccount);

    boolean updatePerformanceAccount(Long id, PerformanceAccount currentPerformanceAccount);

    PerformanceAccount getPerformanceAccount(Long id);

    boolean removePerformanceAccount(Long id);

    Long getPerformanceAccountMaxId();

    List<PerformanceAccount> getTotalPerformanceAccount();

    List<PerformanceAccount> getTotalFilteredPerformanceAccount(Map<String, Object> parameters);
}

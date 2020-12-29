package com.company.go.application.port.out.archive;

import com.company.go.Utilities;
import com.company.go.domain.archive.performance.Performance;

import java.util.List;
import java.util.Map;

public interface UpdatePerformancePort {
    boolean storePerformance(Performance performance);

    boolean updatePerformance(Long id, Performance currentPerformance);

    Performance getPerformance(Long id);

    boolean removePerformance(Long id);

    Long getPerformanceMaxId();

    List<Performance> getTotalPerformance();

    List<Performance> getTotalFilteredPerformance(Map<String, Object> parameters);

    List<Performance> getTotalFilteredPerformance(List<Utilities.Filter> filterList, Utilities.FilterCondition condition);
}

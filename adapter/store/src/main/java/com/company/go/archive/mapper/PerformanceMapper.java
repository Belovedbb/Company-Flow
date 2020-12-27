package com.company.go.archive.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.archive.performance.Constants;
import com.company.go.archive.performance.PerformanceEntity;
import com.company.go.domain.archive.performance.Performance;
import com.company.go.domain.inventory.Money;
import com.company.go.global.mapper.UserMapper;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class PerformanceMapper {
    private static final Map<Constants.PerformanceStatus, Performance.Constants.Status> statusMapping;

    static {
        statusMapping = new EnumMap<>(Constants.PerformanceStatus.class);

        statusMapping.put(Constants.PerformanceStatus.POOR, Performance.Constants.Status.POOR);
        statusMapping.put(Constants.PerformanceStatus.AVERAGE, Performance.Constants.Status.AVERAGE);
        statusMapping.put(Constants.PerformanceStatus.EXCELLENT, Performance.Constants.Status.EXCELLENT);
    }

    static public Performance mapPerformanceEntityToPerformanceDomain(PerformanceEntity entity){
        Performance performance = new Performance();
        performance.setId(entity.getId());
        performance.setBonusPoint(entity.getBonusPoint());
        performance.setAverageMonthlyPerformance(entity.getAverageMonthlyPerformance());
        performance.setStaff(StaffMapper.mapStaffEntityToStaffDomain(entity.getStaffType()));
        performance.setStatus(statusMapping.get(entity.getStatus()));
        performance.setDate(entity.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        performance.setDate(new java.sql.Date(entity.getDateCreated().getTime()).toLocalDate());
        return performance;
    }

    static public PerformanceEntity mapPerformanceDomainToPerformanceEntity(Performance performance){
        PerformanceEntity entity = new PerformanceEntity();
        entity.setId(performance.getId());
        entity.setBonusPoint(performance.getBonusPoint());
        entity.setAverageMonthlyPerformance(performance.getAverageMonthlyPerformance());
        entity.setStaffType(StaffMapper.mapStaffDomainToStaffEntity(performance.getStaff()));
        entity.setStatus(Utilities.getKeyByValue(statusMapping, performance.getStatus()));
        Date dateCreated = performance.getDate() == null ? null : Date.from(performance.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        entity.setDateCreated(dateCreated);
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }
}

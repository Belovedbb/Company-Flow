package com.company.go.application.service.archive;

import com.company.go.application.port.Mapper;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.out.archive.UpdatePerformancePort;
import com.company.go.domain.archive.performance.Performance;
import com.company.go.domain.archive.staff.Staff;
import com.company.go.domain.global.User;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceService implements PerformanceUseCase{
    UpdatePerformancePort performanceStore;

    @Autowired
    PerformanceService(UpdatePerformancePort performanceStore){
        this.performanceStore = performanceStore;
    }

    @SneakyThrows({SQLException.class, IOException.class})
    @Override
    public boolean storePerformance(PerformanceUseCase.PerformanceViewModel model) {
        Performance performance = model.toPerformance();
        return performanceStore.storePerformance(performance);
    }

    @SneakyThrows({SQLException.class, IOException.class})
    @Override
    public boolean editPerformance(Long id, PerformanceUseCase.PerformanceViewModel currentModel) {
        Performance performance = currentModel.toPerformance();
        return performanceStore.updatePerformance(id, performance);
    }

    @Override
    public PerformanceUseCase.PerformanceViewModel viewPerformance(Long id) {
        return Mapper.convert(performanceStore.getPerformance(id));
    }

    @Override
    public List<PerformanceUseCase.PerformanceViewModel> getAllPerformances() {
        return performanceStore.getTotalPerformance().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<PerformanceUseCase.PerformanceViewModel> getFilteredPerformances(PerformanceUseCase.PerformanceViewModel criteriaModel) {
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(!StringUtils.isEmpty(criteriaModel.getStaff().getUserModel().getEmail())){
                filterMap.put("email", criteriaModel.getStaff().getUserModel().getEmail());
            }
        }
        return performanceStore.getTotalFilteredPerformance(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletePerformance(Long id) {
        return performanceStore.removePerformance(id);
    }

    @Override
    public Long getPerformanceMaxId() {
        return performanceStore.getPerformanceMaxId();
    }



}

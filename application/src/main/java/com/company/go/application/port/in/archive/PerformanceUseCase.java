package com.company.go.application.port.in.archive;

import com.company.go.Utilities;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.domain.archive.performance.Performance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface PerformanceUseCase {
    boolean storePerformance(PerformanceViewModel model);

    boolean editPerformance(Long id, PerformanceViewModel currentModel);

    PerformanceViewModel viewPerformance(Long id);

    List<PerformanceViewModel> getAllPerformances();

    List<PerformanceViewModel> getFilteredPerformances(PerformanceViewModel criteriaModel);

    List<PerformanceViewModel> getFilteredPerformances(PerformanceViewModel criteriaModel, StaffUseCase.StaffViewModel staffCriteriaModel, Utilities.FilterCondition condition) throws IOException, SQLException;


    boolean deletePerformance(Long id);

    Long getPerformanceMaxId();

    @Getter
    @Setter
    class PerformanceViewModel{
        private Long id;

        @NotNull
        private StaffUseCase.StaffViewModel staff;

        @NotNull
        private Double averageMonthlyPerformance;

        Double bonusPoint;

        @NotEmpty(message = "Status must not be empty")
        private String status;

        LocalDate date;

        String dateFilter;

        public List<String> getLoadedStatus(){
            ArrayList<String> stores = new ArrayList<>();
            stores.add(Performance.Constants.Status.POOR.name());
            stores.add(Performance.Constants.Status.AVERAGE.name());
            stores.add(Performance.Constants.Status.EXCELLENT.name());
            return stores;
        }

        private LocalDate convertDateFilterToDate(){
            LocalDate date = null;
            if(!StringUtils.isEmpty(dateFilter)){
                date =  LocalDateTime.parse(dateFilter, Utilities.getDateTimeFormatter()).toLocalDate();
            }
            return date;
        }

        public Performance toPerformance() throws IOException, SQLException {
            date = convertDateFilterToDate();
            return  new Performance(
                    id,
                    staff == null ? null : staff.toStaff(),
                    averageMonthlyPerformance,
                    bonusPoint,
                    date,
                    StringUtils.isEmpty(status) ? null : Performance.Constants.Status.valueOf(status)
            );
        }
    }

}

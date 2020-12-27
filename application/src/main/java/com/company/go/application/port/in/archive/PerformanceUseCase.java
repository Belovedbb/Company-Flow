package com.company.go.application.port.in.archive;

import com.company.go.domain.archive.performance.Performance;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface PerformanceUseCase {
    boolean storePerformance(PerformanceViewModel model);

    boolean editPerformance(Long id, PerformanceViewModel currentModel);

    PerformanceViewModel viewPerformance(Long id);

    List<PerformanceViewModel> getAllPerformances();

    List<PerformanceViewModel> getFilteredPerformances(PerformanceViewModel criteriaModel);

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

        public List<String> getLoadedStatus(){
            ArrayList<String> stores = new ArrayList<>();
            stores.add(Performance.Constants.Status.POOR.name());
            stores.add(Performance.Constants.Status.AVERAGE.name());
            stores.add(Performance.Constants.Status.EXCELLENT.name());
            return stores;
        }

        public Performance toPerformance() throws IOException, SQLException {
            return  new Performance(
                    id,
                    staff.toStaff(),
                    averageMonthlyPerformance,
                    bonusPoint,
                    date,
                    Performance.Constants.Status.valueOf(status)
            );
        }
    }

}

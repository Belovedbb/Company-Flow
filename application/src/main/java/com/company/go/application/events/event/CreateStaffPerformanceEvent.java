package com.company.go.application.events.event;

import com.company.go.Utilities;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.archive.staff.Staff;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreateStaffPerformanceEvent extends ApplicationEvent {

    public CreateStaffPerformanceEvent(Object source) {
        super(source);
    }

    private boolean isWithinCurrentMonth(LocalDate factor){
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(Math.min(initial.lengthOfMonth(),28));
        return Utilities.isWithinRange(factor, start, end);
    }

    public void createMonthlyPerformance(StaffUseCase useCase, PerformanceUseCase performanceUseCase, PurchaseOrderUseCase purchaseOrderUseCase) throws IOException, SQLException {
        Map<StaffUseCase.StaffViewModel, Long> staffPerformance = new HashMap<>();
        List<StaffUseCase.StaffViewModel> activeStaffs = useCase.getAllStaffs().parallelStream().
                filter(e -> e.getStatus().equalsIgnoreCase(Staff.Constants.Status.ACTIVE.name())).collect(Collectors.toList());
        for (StaffUseCase.StaffViewModel staff : activeStaffs) {
            RegisterUserUseCase.RegisterUserModel user = staff.getUserModel();
            List<PurchaseOrderUseCase.PurchaseOrderViewModel> orders = purchaseOrderUseCase.getFilteredPurchaseOrders(null, user, Utilities.FilterCondition.AND);
            long count = orders.stream().filter(order -> {
                LocalDate orderChangedTime = LocalDateTime.parse(String.valueOf(order.getLastChangedDate()), Utilities.getISODateTimeFormatter()).toLocalDate();
                return isWithinCurrentMonth(orderChangedTime);
            }).count();
            staffPerformance.put(staff, count);
        }
        buildPerformanceModel(staffPerformance, performanceUseCase);
    }

    private String getStatus(double percentage){
        PerformanceUseCase.PerformanceViewModel performanceViewModel = new PerformanceUseCase.PerformanceViewModel();
        String poor = performanceViewModel.getLoadedStatus().get(0);
        String average = performanceViewModel.getLoadedStatus().get(1);
        String excellent = performanceViewModel.getLoadedStatus().get(2);

        String result = poor;
        if(percentage > 25d && percentage < 75d){
            result = average;
        }else if(percentage > 75d){
            result = excellent;
        }

        return result;
    }

    private void buildPerformanceModel(Map<StaffUseCase.StaffViewModel, Long> performances, PerformanceUseCase performanceUseCase){
        if(!CollectionUtils.isEmpty(performances)){
            performances.keySet().forEach(staff -> {
                Long count = performances.get(staff);
                PerformanceUseCase.PerformanceViewModel model = new PerformanceUseCase.PerformanceViewModel();
                model.setStaff(staff);
                model.setBonusPoint(count * (staff.getPayment() == null ? 1d : staff.getPayment()));
                //using min-max scaling
                double minimum = performances.values().stream().min(Long::compareTo).orElseThrow();
                double maximum = performances.values().stream().max(Long::compareTo).orElseThrow();
                double performanceValue = (maximum - minimum) == 0d ? 0d : ((count - minimum)/(maximum - minimum));
                double performancePercent = performanceValue * 100;
                model.setAverageMonthlyPerformance(performancePercent);
                model.setStatus(getStatus(performancePercent));
                performanceUseCase.storePerformance(model);
            });
        }
    }

}
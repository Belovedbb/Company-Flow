package com.company.go.application.events.listener;

import com.company.go.application.events.event.CreateStaffPerformanceEvent;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.archive.performance.Performance;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ArchiveListener {

    StaffUseCase staffUseCase;

    PerformanceUseCase performanceUseCase;

    PurchaseOrderUseCase purchaseOrderUseCase;

    ArchiveListener(StaffUseCase staffUseCase, PerformanceUseCase performanceUseCase, PurchaseOrderUseCase purchaseOrderUseCase){
        this.staffUseCase = staffUseCase;
        this.performanceUseCase = performanceUseCase;
        this.purchaseOrderUseCase = purchaseOrderUseCase;
    }

    //@Async
    @EventListener
    void handleCreateStaffPerformanceEvent(CreateStaffPerformanceEvent event){
        LocalDate today = LocalDate.now();
        if(today.getDayOfMonth() == 28) {
            event.createMonthlyPerformance(staffUseCase, performanceUseCase, purchaseOrderUseCase);
        }
    }

}
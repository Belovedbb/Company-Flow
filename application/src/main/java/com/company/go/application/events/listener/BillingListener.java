package com.company.go.application.events.listener;

import com.company.go.application.events.event.CreateAccountEvent;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BillingListener {

    @Autowired
    private PerformanceUseCase performanceUseCase;
    @Autowired
    private PurchaseOrderUseCase purchaseOrderUseCase;
    @Autowired
    private ProductUseCase productUseCase;
    @Autowired
    private AccountUseCase accountUseCase;

    //@Async
    @EventListener
    void handleCreateAccountEvent(CreateAccountEvent event){
        LocalDate today = LocalDate.now();
        if(today.getDayOfMonth() == 28) {
            event.createMonthlyPerformanceAccount(accountUseCase, performanceUseCase);
            event.createMonthlyPurchaseOrderAccount(accountUseCase, purchaseOrderUseCase);
            event.createMonthlyProductAccount(accountUseCase, productUseCase);
        }
    }


}

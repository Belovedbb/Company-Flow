package com.company.go.application.events.event;

import com.company.go.Utilities;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.billing.PerformanceAccount;
import com.company.go.domain.billing.ProductAccount;
import com.company.go.domain.billing.PurchaseOrderAccount;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class CreateAccountEvent extends ApplicationEvent {
    public CreateAccountEvent(Object source) {
        super(source);
    }

    private boolean isWithinCurrentMonth(LocalDate factor){
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(Math.min(initial.lengthOfMonth(),28));
        return Utilities.isWithinRangeInclusive(factor, start, end);
    }

    public void createMonthlyPerformanceAccount(AccountUseCase accountUseCase, PerformanceUseCase performanceUseCase){
        List<PerformanceUseCase.PerformanceViewModel> performances = performanceUseCase.getAllPerformances();
        List<PerformanceUseCase.PerformanceViewModel> filteredPerformances = performances.stream()
                .filter(performance -> isWithinCurrentMonth(performance.getDate())).collect(Collectors.toList());

        Double amount = filteredPerformances.stream().map(PerformanceUseCase.PerformanceViewModel::getBonusPoint)
                .collect(Collectors.toList()).stream().reduce(0.0, Double::sum);
        AccountUseCase.PerformanceAccountViewModel model = new AccountUseCase.PerformanceAccountViewModel();
        model.setType(new PerformanceAccount().getType());
        model.setKind(new PerformanceAccount().getKind().name());
        model.setDate(LocalDate.now());
        model.setAggregateAmount(amount);
        model.setTypeCount((long) filteredPerformances.size());

        accountUseCase.storeAccount(model);
    }

    public void createMonthlyPurchaseOrderAccount(AccountUseCase accountUseCase, PurchaseOrderUseCase purchaseOrderUseCase){
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> purchaseOrders = purchaseOrderUseCase.getAllPurchaseOrders();
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> filteredPurchaseOrders = purchaseOrders.stream().filter(purchaseOrder -> {
            LocalDate currentPurchaseOrder = LocalDateTime.parse(purchaseOrder.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentPurchaseOrder);
        }).collect(Collectors.toList());

        Double amount = filteredPurchaseOrders.stream().map(this::getPurchaseOrderAmount)
                .collect(Collectors.toList()).stream().reduce(0.0, Double::sum);
        AccountUseCase.PurchaseOrderAccountViewModel model = new AccountUseCase.PurchaseOrderAccountViewModel();
        model.setType(new PurchaseOrderAccount().getType());
        model.setKind(new PurchaseOrderAccount().getKind().name());
        model.setDate(LocalDate.now());
        model.setAggregateAmount(amount);
        model.setTypeCount((long) filteredPurchaseOrders.size());

        accountUseCase.storeAccount(model);
    }

    private double getPurchaseOrderAmount(PurchaseOrderUseCase.PurchaseOrderViewModel purchaseOrderViewModel){
        double amount = 0.0;
        List<PurchaseOrderUseCase.PurchaseOrderEntryViewModel> entries = purchaseOrderViewModel.getPurchaseOrderEntries();
        if(!CollectionUtils.isEmpty(entries)){
            amount = entries.stream().flatMapToDouble(currentEntry -> DoubleStream.of(currentEntry.getOrderAmount() * currentEntry.getOrderQuantity())).sum();
        }
        return amount;
    }

    public void createMonthlyProductAccount(AccountUseCase accountUseCase, ProductUseCase productUseCase){
        List<ProductUseCase.ProductViewModel> products = productUseCase.getAllProducts();
        List<ProductUseCase.ProductViewModel> filteredProducts = products.stream().filter(product -> {
            LocalDate currentProduct = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentProduct);
        }).collect(Collectors.toList());

        Double amount = filteredProducts.stream().map(product -> product.getAmount() * product.getQuantity())
                .collect(Collectors.toList()).stream().reduce(0.0, Double::sum);
        AccountUseCase.ProductAccountViewModel model = new AccountUseCase.ProductAccountViewModel();
        model.setType(new ProductAccount().getType());
        model.setKind(new ProductAccount().getKind().name());
        model.setDate(LocalDate.now());
        model.setAggregateAmount(amount);
        model.setTypeCount((long) filteredProducts.size());

        accountUseCase.storeAccount(model);
    }

}

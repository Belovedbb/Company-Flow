package com.company.go.application.service.global;

import com.company.go.Utilities;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.application.port.in.global.DashBoardUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.product.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DashBoardService implements DashBoardUseCase {

    private final static String monthText = "Last " + LocalDate.now().getDayOfMonth() + " days (" + LocalDate.now().getMonth().name() + ") ";
    private final static String weekText = "Last " + LocalDate.now().getDayOfWeek() + " days ";
    private final static String yearText = "Last " + LocalDate.now().getDayOfYear() + " days (" + LocalDate.now().getYear() + ") ";

    private final ProductUseCase productUseCase;

    private final PurchaseOrderUseCase orderUseCase;

    private final PerformanceUseCase performanceUseCase;

    DashBoardService(ProductUseCase productUseCase, PurchaseOrderUseCase orderUseCase, PerformanceUseCase performanceUseCase){
        this.productUseCase = productUseCase;
        this.orderUseCase = orderUseCase;
        this.performanceUseCase = performanceUseCase;
    }

    @Override
    public List<SummaryWidgetViewModel> getAllSummaries() {
        List<SummaryWidgetViewModel> summaries = new ArrayList<>();
        summaries.addAll(productSummary());
        summaries.addAll(orderSummary());
        summaries.addAll(performanceSummary());
        return summaries;
    }

    private List<SummaryWidgetViewModel> productSummary(){
        List<ProductUseCase.ProductViewModel> products = productUseCase.getAllProducts();
        List<ProductUseCase.ProductViewModel> filteredProductsMonth = products.stream().filter(product -> {
            LocalDate currentProduct = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentProduct);
        }).collect(Collectors.toList());

        SummaryWidgetViewModel modelMonth = new SummaryWidgetViewModel();
        modelMonth.setCount((long) filteredProductsMonth.size());
        modelMonth.setPeriodText(monthText);
        modelMonth.setName("Product");


        List<ProductUseCase.ProductViewModel> filteredProductsYear = products.stream().filter(product -> {
            LocalDate currentProduct = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentYear(currentProduct);
        }).collect(Collectors.toList());

        SummaryWidgetViewModel modelYear = new SummaryWidgetViewModel();
        modelYear.setCount((long) filteredProductsYear.size());
        modelYear.setPeriodText(yearText);
        modelYear.setName("Product");

        return List.of(modelMonth, modelYear);
    }

    private List<SummaryWidgetViewModel> orderSummary(){
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> purchaseOrders = orderUseCase.getAllPurchaseOrders();
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> filteredPurchaseOrdersMonth = purchaseOrders.stream().filter(purchaseOrder -> {
            LocalDate currentPurchaseOrder = LocalDateTime.parse(purchaseOrder.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentPurchaseOrder);
        }).collect(Collectors.toList());

        SummaryWidgetViewModel modelMonth = new SummaryWidgetViewModel();
        modelMonth.setCount((long) filteredPurchaseOrdersMonth.size());
        modelMonth.setPeriodText(monthText);
        modelMonth.setName("Order");

        List<PurchaseOrderUseCase.PurchaseOrderViewModel> filteredPurchaseOrdersYear = purchaseOrders.stream().filter(purchaseOrder -> {
            LocalDate currentPurchaseOrder = LocalDateTime.parse(purchaseOrder.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentYear(currentPurchaseOrder);
        }).collect(Collectors.toList());

        SummaryWidgetViewModel modelYear = new SummaryWidgetViewModel();
        modelYear.setCount((long) filteredPurchaseOrdersYear.size());
        modelYear.setPeriodText(yearText);
        modelYear.setName("Order");
        return  List.of(modelMonth, modelYear);
    }

    private List<SummaryWidgetViewModel> performanceSummary(){
        List<PerformanceUseCase.PerformanceViewModel> performances = performanceUseCase.getAllPerformances();
        List<PerformanceUseCase.PerformanceViewModel> filteredPerformancesMonth = performances.stream()
                .filter(performance -> isWithinCurrentMonth(performance.getDate())).collect(Collectors.toList());

        SummaryWidgetViewModel modelMonth = new SummaryWidgetViewModel();
        modelMonth.setCount((long) filteredPerformancesMonth.size());
        modelMonth.setPeriodText(monthText);
        modelMonth.setName("Performance");


        List<PerformanceUseCase.PerformanceViewModel> filteredPerformancesYear = performances.stream()
                .filter(performance -> isWithinCurrentYear(performance.getDate())).collect(Collectors.toList());

        SummaryWidgetViewModel modelYear = new SummaryWidgetViewModel();
        modelYear.setCount((long) filteredPerformancesYear.size());
        modelYear.setPeriodText(yearText);
        modelYear.setName("Performance");
        return  List.of(modelMonth, modelYear);
    }

    private boolean isWithinCurrentMonth(LocalDate factor){
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(Math.min(initial.lengthOfMonth(),31));
        return Utilities.isWithinRangeInclusive(factor, start, end);
    }

    private boolean isWithinCustomMonth(LocalDate factor, LocalDate testDate){
        LocalDate start = testDate.withDayOfMonth(1);
        LocalDate end = testDate.withDayOfMonth(testDate.lengthOfMonth());
        return Utilities.isWithinRangeInclusive(factor, start, end);
    }
    private boolean isWithinCurrentYear(LocalDate factor){
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfYear(1);
        LocalDate end = initial.withDayOfYear(initial.lengthOfYear());
        return Utilities.isWithinRangeInclusive(factor, start, end);
    }

    @Override
    public Map<String, Long>[] summaryChartData(List<SummaryWidgetViewModel> summaries){
        Map<String, Long> containerMonth = new HashMap<>();
        Map<String, Long> containerYear = new HashMap<>();
        for(int i = 0; i < summaries.size(); i++){
            SummaryWidgetViewModel summary = summaries.get(i);
            if(i % 2 == 0) {
                containerMonth.put(summary.getName(), summary.getCount());
            }else{
                containerYear.put(summary.getName(), summary.getCount());
            }
        }
        return new Map[]{containerMonth, containerYear};
    }

    @Override
    public Map<String, Long>[] detailedProductChartData(){
        List<ProductUseCase.ProductViewModel> products = productUseCase.getAllProducts();
        return new Map[]{detailedProductYearRange(products), detailedProductStatus(products)};
    }

    private Map<String, Long> detailedProductYearRange(List<ProductUseCase.ProductViewModel> products){
        Map<String, Long> productContainer = new HashMap<>();
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfYear(1);
        LocalDate endExclusive = initial.withDayOfYear(initial.lengthOfYear()).plusMonths(1);
        Stream<LocalDate> dateInterval = start.datesUntil(endExclusive, Period.ofMonths(1));
        dateInterval.forEachOrdered(interval -> {
            List<ProductUseCase.ProductViewModel> filteredProducts = products.stream().filter(product -> {
                LocalDate currentProduct = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
                return isWithinCustomMonth(currentProduct, interval);
            }).collect(Collectors.toList());
            productContainer.put(String.valueOf(interval.getMonthValue()), (long) filteredProducts.size());
        });
        return productContainer;
    }

    private Map<String, Long> detailedProductStatus(List<ProductUseCase.ProductViewModel> products){
        List<ProductUseCase.ProductViewModel> filteredProductsMonth = products.stream().filter(product -> {
            LocalDate currentProduct = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentProduct);
        }).collect(Collectors.toList());
        long activeCount = filteredProductsMonth.stream().filter(product -> product.getStatus().equalsIgnoreCase(Product.Constants.Status.ACTIVE.name())).count();
        return Map.of(Product.Constants.Status.ACTIVE.name(), activeCount, Product.Constants.Status.INACTIVE.name(), filteredProductsMonth.size() - activeCount);
    }


    @Override
    public Map<String, Long>[] detailedOrderChartData(){
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> orders = orderUseCase.getAllPurchaseOrders();
        return new Map[]{detailedOrderYearRange(orders), detailedOrderStatus(orders)};
    }

    private Map<String, Long> detailedOrderYearRange(List<PurchaseOrderUseCase.PurchaseOrderViewModel> orders){
        Map<String, Long> productContainer = new HashMap<>();
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfYear(1);
        LocalDate endExclusive = initial.withDayOfYear(initial.lengthOfYear()).plusMonths(1);
        Stream<LocalDate> dateInterval = start.datesUntil(endExclusive, Period.ofMonths(1));
        dateInterval.forEachOrdered(interval -> {
            List<PurchaseOrderUseCase.PurchaseOrderViewModel> filteredOrders = orders.stream().filter(product -> {
                LocalDate currentOrder = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
                return isWithinCustomMonth(currentOrder, interval);
            }).collect(Collectors.toList());
            productContainer.put(String.valueOf(interval.getMonthValue()), (long) filteredOrders.size());
        });
        return productContainer;
    }

    private Map<String, Long> detailedOrderStatus(List<PurchaseOrderUseCase.PurchaseOrderViewModel> orders){
        List<PurchaseOrderUseCase.PurchaseOrderViewModel> filteredOrdersMonth = orders.stream().filter(product -> {
            LocalDate currentOrder = LocalDateTime.parse(product.getLastChangedDate(), Utilities.getISODateTimeFormatter()).toLocalDate();
            return isWithinCurrentMonth(currentOrder);
        }).collect(Collectors.toList());
        long openCount = filteredOrdersMonth.stream().filter(product -> product.getStatus().equalsIgnoreCase(Order.Constants.Status.OPEN.name())).count();
        return Map.of(Order.Constants.Status.OPEN.name(), openCount, Order.Constants.Status.CLOSED.name(), filteredOrdersMonth.size() - openCount);
    }

}

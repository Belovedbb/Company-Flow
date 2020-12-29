package com.company.go.application.port.in.inventory;

import com.company.go.Utilities;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.order.OrderEntry;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PurchaseOrderUseCase {

    boolean storePurchaseOrder(PurchaseOrderViewModel model);

    boolean editPurchaseOrder(Long id, PurchaseOrderViewModel currentModel);

    PurchaseOrderViewModel viewPurchaseOrder(Long id);

    List<PurchaseOrderViewModel> getAllPurchaseOrders();

    void saveOrderEntry(PurchaseOrderViewModel order, Long[] ids);

    void removeOrderEntry(PurchaseOrderViewModel order, Long[] ids);

    void updateOrderAmountEntry(PurchaseOrderViewModel order, Long value, Long index);

    PurchaseOrderViewModel removeOrderEntry(Long id, PurchaseOrderViewModel order);

    List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel);

    List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel, RegisterUserUseCase.RegisterUserModel userCriteriaModel, Utilities.FilterCondition condition) throws IOException, SQLException;

    List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel, Map<String, Object> userCriteriaModel);

    List<ProductUseCase.ProductViewModel> getAvailableProducts();

    boolean deletePurchaseOrder(Long id);

    Long getPurchaseOrderMaxId();

    @Getter
    @Setter
    class PurchaseOrderViewModel{
        private Long id;

        private PurchaseOrderUseCase.PurchaseOrderViewModel product;

        private List<PurchaseOrderEntryViewModel> purchaseOrderEntries;

        @PositiveOrZero
        private Long quantity;

        @NotEmpty(message = "Purchased Date must not be empty")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private String purchasedDate;

        @NotEmpty
        private String description;

        @NotNull
        private Double amount;

        private String currency;

        private Boolean hasVat;

        @NotEmpty(message = "Status must not be empty")
        private String status;

        private String lastChangedDate;

        private LocalDateTime convertToDate(String date){
            return LocalDateTime.parse(date, Utilities.getDateTimeFormatter());
        }

        public List<String> getLoadedStatus(){
            ArrayList<String> statuses = new ArrayList<>();
            statuses.add(Order.Constants.Status.OPEN.name());
            statuses.add(Order.Constants.Status.CLOSED.name());
            return statuses;
        }
        
        private ArrayList<OrderEntry> convertToDomainOrderEntries(){
            ArrayList<OrderEntry> orderEntries = new ArrayList<>();
            if(!CollectionUtils.isEmpty(purchaseOrderEntries)) {
                for (PurchaseOrderEntryViewModel purchaseOrderEntry : purchaseOrderEntries) {
                    OrderEntry orderEntry = new OrderEntry();
                    orderEntry.setId(purchaseOrderEntry.getId());
                    orderEntry.setProductId(purchaseOrderEntry.getProduct().getId());
                    orderEntry.setQuantity(purchaseOrderEntry.getOrderQuantity());
                    orderEntries.add(orderEntry);
                }
            }
            return orderEntries;
        }

        public Order toPurchaseOrder(){
            return  new Order(
                    id,
                    convertToDomainOrderEntries(),
                    description,
                    hasVat,
                    StringUtils.isEmpty(purchasedDate) ? null : convertToDate(purchasedDate),
                    Order.Constants.Status.valueOf(status),
                    StringUtils.isEmpty(lastChangedDate) ? null : convertToDate(lastChangedDate)
            );
        }
    }

    @Getter
    @Setter
    class PurchaseOrderEntryViewModel{
        private Long id;
        @NotNull
        @PositiveOrZero
        private Long productId;
        @NotNull
        private ProductUseCase.ProductViewModel product;
        @NotNull
        private Long orderQuantity;
        @NotNull
        private Double orderAmount;
        private boolean isStored;
    }

}

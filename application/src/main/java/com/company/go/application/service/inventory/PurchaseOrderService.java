package com.company.go.application.service.inventory;

import com.company.go.Utilities;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.company.go.application.port.out.inventory.UpdatePurchaseOrderPort;
import com.company.go.domain.global.User;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.order.OrderEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService implements PurchaseOrderUseCase {

    private UpdatePurchaseOrderPort purchaseOrderStore;
    private ProductUseCase productUseCase;

    @Autowired
    public PurchaseOrderService(UpdatePurchaseOrderPort purchaseOrderStore, ProductUseCase productUseCase) {
        this.purchaseOrderStore = purchaseOrderStore;
        this.productUseCase = productUseCase;
    }

    @Override
    public boolean storePurchaseOrder(PurchaseOrderViewModel model) {
        Order order = model.toPurchaseOrder();
        return purchaseOrderStore.storePurchaseOrder(order);
    }

    @Override
    public boolean editPurchaseOrder(Long id, PurchaseOrderViewModel currentModel) {
        Order order = currentModel.toPurchaseOrder();
        return purchaseOrderStore.updatePurchaseOrder(id, order);
    }

    @Override
    public PurchaseOrderViewModel viewPurchaseOrder(Long id) {
        return convert(purchaseOrderStore.getOrder(id));
    }

    @Override
    public List<PurchaseOrderViewModel> getAllPurchaseOrders() {
        return purchaseOrderStore.getTotalPurchaseOrder()
                .stream().map(this::convert)
                .collect(Collectors.toList());
    }

    private boolean hasPurchaseOrderEntryId(List<PurchaseOrderEntryViewModel> entries, Long id){
        if(!CollectionUtils.isEmpty(entries)){
            return entries.stream().anyMatch(e -> e.getProduct().getId().equals(id));
        }
        return false;
    }

    private void  refreshPurchaseOrderProducts(PurchaseOrderViewModel order){
        Long totalQuantity = 0L;
        Double totalAmount = 0.0;
        if(!CollectionUtils.isEmpty(order.getPurchaseOrderEntries())){
            for(PurchaseOrderEntryViewModel entry: order.getPurchaseOrderEntries()){
                if(entry.getProduct() != null && entry.getProduct().getId() != null){
                    entry.setProduct(productUseCase.viewProduct(entry.getProduct().getId()));
                    if(entry.getOrderQuantity() != null){
                        entry.setOrderAmount(entry.getProduct().getAmount() * entry.getOrderQuantity());
                        totalAmount += entry.getOrderAmount();
                        totalQuantity += entry.getOrderQuantity();
                    }
                }
            }
        }
        order.setQuantity(totalQuantity);
        order.setAmount(totalAmount);
    }

    @Override
    public void saveOrderEntry(PurchaseOrderViewModel order, Long[] ids) {
        List<PurchaseOrderEntryViewModel> entries = order.getPurchaseOrderEntries();
        if(CollectionUtils.isEmpty(entries)){
            entries = new ArrayList<>();
            order.setPurchaseOrderEntries(entries);
        }
        for(Long id : ids) {
            if (id != null) {
                refreshPurchaseOrderProducts(order);
                if (!hasPurchaseOrderEntryId(order.getPurchaseOrderEntries(), id)) {
                    var products = getAvailableProducts().stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(products)) {
                        ProductUseCase.ProductViewModel product = products.get(0);
                        PurchaseOrderEntryViewModel entry = new PurchaseOrderEntryViewModel();
                        entry.setProductId(product.getId());
                        entry.setProduct(productUseCase.viewProduct(id));
                        entries.add(entry);
                    }
                }
            }
        }
    }

    @Override
    public void removeOrderEntry(PurchaseOrderViewModel order, Long[] ids) {
        List<PurchaseOrderEntryViewModel> entries = order.getPurchaseOrderEntries();
        if(!CollectionUtils.isEmpty(entries)){
            for(Long id : ids){
                entries.removeIf( e -> e.getProduct().getId().equals(id) && !e.isStored());
            }
        }
        refreshPurchaseOrderProducts(order);
    }

    @Override
    public void updateOrderAmountEntry(PurchaseOrderViewModel order, Long value, Long index) {
        refreshPurchaseOrderProducts(order);
        if(!CollectionUtils.isEmpty(order.getPurchaseOrderEntries()) && index != null
                && index < order.getPurchaseOrderEntries().size()){
            double amount = value == null ? 0.0 :  order.getPurchaseOrderEntries().get(Math.toIntExact(index)).getProduct().getAmount() * value;
            order.getPurchaseOrderEntries().get(Math.toIntExact(index)).setOrderAmount(amount);
        }
    }


    @Override
    public PurchaseOrderViewModel removeOrderEntry(Long id, PurchaseOrderViewModel order) {
        if(!CollectionUtils.isEmpty(order.getPurchaseOrderEntries()) && id < order.getPurchaseOrderEntries().size()){
            order.getPurchaseOrderEntries().remove(id);
        }
        return order;
    }

    @Override
    public List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel) {
        /*Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(!StringUtils.isEmpty(criteriaModel.getName())){
                filterMap.put("name", criteriaModel.getName());
            }
            if(!StringUtils.isEmpty(criteriaModel.getCategory())){
                filterMap.put("category", criteriaModel.getCategory());
            }
        }
        return productStoreAdapter.getTotalFilteredProduct(filterMap)
                .stream().map(this::convert)
                .collect(Collectors.toList());*/
        return null;
    }

    Map<String, Object> getOrderProperties(PurchaseOrderViewModel criteriaModel){
        Order order = criteriaModel.toPurchaseOrder();
        Map<String, Object> filterMap = new HashMap<>();
            if(!StringUtils.isEmpty(criteriaModel.getDescription())){
                filterMap.put("description", order.getDescription());
            }
            if(!StringUtils.isEmpty(criteriaModel.getPurchasedDate())){
                filterMap.put("purchasedDate", order.getPurchasedDate());
            }
            if(!StringUtils.isEmpty(criteriaModel.getStatus())){
                filterMap.put("status", order.getStatus());
            }
        return filterMap;
    }

    Map<String, Object> getUserProperties(RegisterUserUseCase.RegisterUserModel userCriteriaModel) throws IOException, SQLException {
        User user = userCriteriaModel.toUser();
        Map<String, Object> filterMap = new HashMap<>();
            if(userCriteriaModel.getId() != null){
                filterMap.put("Id", user.getId());
            }
        return filterMap;
    }

    @Override
    public List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel, RegisterUserUseCase.RegisterUserModel userCriteriaModel, Utilities.FilterCondition condition) throws IOException, SQLException {
        List<Utilities.Filter> filters = new ArrayList<>();

        if(criteriaModel != null) {
            Utilities.Filter<Order> orderFilter = new Utilities.Filter<>();
            orderFilter.setKlass(Order.class);
            orderFilter.setProperties(getOrderProperties(criteriaModel));
            filters.add(orderFilter);
        }
        if(userCriteriaModel != null) {
            Utilities.Filter<User> userFilter = new Utilities.Filter<>();
            userFilter.setKlass(User.class);
            userFilter.setProperties(getUserProperties(userCriteriaModel));
            filters.add(userFilter);
        }
        return purchaseOrderStore.getTotalFilteredPurchaseOrder(filters, condition).stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderViewModel> getFilteredPurchaseOrders(PurchaseOrderViewModel criteriaModel, Map<String, Object> userCriteriaModel) {
        if(!CollectionUtils.isEmpty(userCriteriaModel)){
            return purchaseOrderStore.getTotalFilteredPurchaseOrder(null, userCriteriaModel)
                    .stream().map(this::convert)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<ProductUseCase.ProductViewModel> getAvailableProducts() {
        return productUseCase.getAllProducts()
                .stream().filter(e -> e.getStatus().equals("ACTIVE")).collect(Collectors.toList());
    }

    @Override
    public boolean deletePurchaseOrder(Long id) {
        return purchaseOrderStore.removeOrder(id);
    }

    @Override
    public Long getPurchaseOrderMaxId() {
        return purchaseOrderStore.getPurchaseOrderMaxId();
    }

    private PurchaseOrderViewModel convert(Order order){
        PurchaseOrderViewModel purchaseOrderViewModel = new PurchaseOrderViewModel();
        purchaseOrderViewModel.setId(order.getId());
        purchaseOrderViewModel.setPurchaseOrderEntries(convertOrderEntriesToViewEntries(order.getOrderEntries()));
        purchaseOrderViewModel.setQuantity(converToEntriesTotalQuantity(purchaseOrderViewModel.getPurchaseOrderEntries()));
        purchaseOrderViewModel.setAmount(converToEntriesTotalAmount(purchaseOrderViewModel.getPurchaseOrderEntries()));
        purchaseOrderViewModel.setPurchasedDate(order.getPurchasedDate().toString());
        purchaseOrderViewModel.setLastChangedDate(order.getLastChangedDate().toString());
        //purchaseOrderViewModel.setCurrency();
        purchaseOrderViewModel.setDescription(order.getDescription());
        purchaseOrderViewModel.setHasVat(order.getHasVat());
        purchaseOrderViewModel.setProduct(null);
        purchaseOrderViewModel.setStatus(order.getStatus().name());
        return purchaseOrderViewModel;
    }

    private Double converToEntriesTotalAmount(List<PurchaseOrderEntryViewModel>  orderEntries){
        double totalAmount = 0.0;
        for(PurchaseOrderEntryViewModel orderEntry: orderEntries){
            totalAmount += orderEntry.getOrderAmount();
        }
        return totalAmount;
    }

    private Long converToEntriesTotalQuantity(List<PurchaseOrderEntryViewModel>  orderEntries){
        long totalQuantity = 0L;
        for(PurchaseOrderEntryViewModel orderEntry: orderEntries){
            totalQuantity += orderEntry.getOrderQuantity();
        }
        return totalQuantity;
    }


    private List<PurchaseOrderEntryViewModel> convertOrderEntriesToViewEntries(List<OrderEntry> orderEntries){
        List<PurchaseOrderEntryViewModel> viewOrderEntries = new ArrayList<>();
        for(OrderEntry orderEntry : orderEntries){
            PurchaseOrderEntryViewModel model = new PurchaseOrderEntryViewModel();
            model.setId(orderEntry.getId());
            model.setOrderQuantity(orderEntry.getQuantity());
            model.setProductId(orderEntry.getProductId());
            model.setStored(orderEntry.isStored());
            ProductUseCase.ProductViewModel product = productUseCase.viewProduct(orderEntry.getProductId());
            model.setProduct(product);
            model.setOrderAmount(product.getAmount() * model.getOrderQuantity());
            viewOrderEntries.add(model);
        }
        return viewOrderEntries;
    }

}

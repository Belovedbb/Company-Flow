package com.company.go.application.port.out.inventory;

import com.company.go.Utilities;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.product.Product;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface UpdatePurchaseOrderPort {

    boolean storePurchaseOrder(Order order);

    boolean updatePurchaseOrder(Long id, Order currentOrder);

    Order getOrder(Long id);

    boolean removeOrder(Long id);

    Order findPurchaseOrder(BigInteger orderId);

    Long getPurchaseOrderMaxId();

    List<Order> getTotalPurchaseOrder();

    List<Order> getTotalFilteredPurchaseOrder(Map<String, Object> parameters);

    List<Order> getTotalFilteredPurchaseOrder(Map<String, Object> parameters, Map<String, Object> users);

    List<Order> getTotalFilteredPurchaseOrder(List<Utilities.Filter> filterList, Utilities.FilterCondition condition);

}

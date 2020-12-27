package com.company.go.inventory.order;

import com.company.go.Utilities;
import com.company.go.application.port.out.inventory.UpdatePurchaseOrderPort;
import com.company.go.domain.global.User;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.product.Product;
import com.company.go.global.UserEntity;
import com.company.go.inventory.mapper.OrderMapper;
import com.company.go.inventory.order.repo.OrderRepository;
import com.company.go.inventory.product.ProductEntity;
import com.company.go.inventory.product.repo.ProductRepository;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class PurchaseOrderStoreAdapter implements UpdatePurchaseOrderPort {

    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    public PurchaseOrderStoreAdapter(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Override
    public boolean storePurchaseOrder(Order order) {
        try{
            OrderEntity orderEntity = OrderMapper.mapOrderDomainToOrderEntity(order);
            Set<OrderEntryEntity> orderEntries = orderEntity
                    .getOrderEntries().stream()
                    .map(this::setOrderEntries)
                    .collect(Collectors.toSet());
            orderEntity.setOrderEntries(orderEntries);
            orderRepo.createOrder(orderEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }


    @Override
    public boolean updatePurchaseOrder(Long id, Order currentOrder) {
        try {
            OrderEntity orderEntity = OrderMapper.mapOrderDomainToOrderEntity(currentOrder);
            Set<OrderEntryEntity> orderEntries = orderEntity
                    .getOrderEntries().stream()
                    .map(this::setOrderEntries)
                    .collect(Collectors.toSet());
            if(orderEntity.getOrderEntries() == null){
                orderEntity.setOrderEntries(new HashSet<>());
            }
            orderEntity.getOrderEntries().clear();
            orderEntity.getOrderEntries().addAll(orderEntries);
            orderRepo.updateOrder(orderEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Order getOrder(Long id) {
        return OrderMapper.mapOrderEntityToOrderDomain(orderRepo.getOrder(id));
    }

    @Override
    public boolean removeOrder(Long id) {
        try{
            orderRepo.deleteOrder(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Order findPurchaseOrder(BigInteger orderId) {
        try{
            return OrderMapper.mapOrderEntityToOrderDomain(orderRepo.findOrderById(orderId));
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public Long getPurchaseOrderMaxId() {
        try{
            return orderRepo.getMaxId(OrderEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<Order> getTotalPurchaseOrder() {
        return orderRepo.getAllRows(OrderEntity.class).stream()
                .map(OrderMapper::mapOrderEntityToOrderDomain).collect(Collectors.toList());
    }

    @Override
    public List<Order> getTotalFilteredPurchaseOrder(Map<String, Object> parameters) {
        return orderRepo.filterOrder(parameters).stream()
                .map(OrderMapper::mapOrderEntityToOrderDomain).collect(Collectors.toList());
    }

    @Override
    public List<Order> getTotalFilteredPurchaseOrder(Map<String, Object> parameters, Map<String, Object> users) {
        //return orderRepo.filterOrderWithJoin(parameters, users).stream()
               // .map(OrderMapper::mapOrderEntityToOrderDomain).collect(Collectors.toList());
        return null;
    }

    @Override
    public List<Order> getTotalFilteredPurchaseOrder(List<Utilities.Filter> filterList, Utilities.FilterCondition condition) {
        return orderRepo.filterOrder(OrderMapper.mapErasureFilter(filterList), condition).stream().map(OrderMapper::mapOrderEntityToOrderDomain).collect(Collectors.toList());
    }

    OrderEntryEntity setOrderEntries(OrderEntryEntity orderEntry){
        if(orderEntry != null && orderEntry.getProductId() != null){
            ProductEntity product = productRepo.getProduct(orderEntry.getProductId());
            orderEntry.setProduct(product);
        }
        return orderEntry;
    }


}

package com.company.go.inventory.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.domain.global.User;
import com.company.go.domain.inventory.order.Order;
import com.company.go.domain.inventory.order.OrderEntry;
import com.company.go.global.UserEntity;
import com.company.go.inventory.order.Constants;
import com.company.go.inventory.order.OrderEntity;
import com.company.go.inventory.order.OrderEntryEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class OrderMapper {

    private static final Map<Constants.OrderStatus, Order.Constants.Status> statusMapping;

    static {
        statusMapping = new EnumMap<>(Constants.OrderStatus.class);

        statusMapping.put(Constants.OrderStatus.OPEN, Order.Constants.Status.OPEN);
        statusMapping.put(Constants.OrderStatus.CLOSED, Order.Constants.Status.CLOSED);
    }

    static public Order mapOrderEntityToOrderDomain(OrderEntity entity){
        Order order = new Order();
        order.setId(entity.getId());
        order.setDescription(entity.getDescription());
        order.setPurchasedDate(entity.getPurchasedDate());
        order.setLastChangedDate(LocalDateTime.ofInstant(entity.getLastChanged().toInstant(), ZoneId.systemDefault()));
        order.setHasVat(entity.isHasVat());
        order.setStatus(statusMapping.get(entity.getStatus()));
        ArrayList<OrderEntry> orderEntries = new ArrayList<>();
        for(OrderEntryEntity orderEntry: entity.getOrderEntries()){
            orderEntries.add(mapOrderEntryEntityToOrderEntryDomain(orderEntry));
        }
        order.setOrderEntries(orderEntries);
        return order;
    }

    static public OrderEntity mapOrderDomainToOrderEntity(Order order){
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setDescription(order.getDescription());
        entity.setHasVat(order.getHasVat());
        entity.setPurchasedDate(order.getPurchasedDate());
        entity.setStatus(Utilities.getKeyByValue(statusMapping, order.getStatus()));
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        Set<OrderEntryEntity> entryEntities = new HashSet<>();
        for(OrderEntry orderEntry : order.getOrderEntries()){
            entryEntities.add(mapOrderEntryDomainToOrderEntryEntity(orderEntry, entity));
        }
        entity.setOrderEntries(entryEntities);
        return entity;
    }

    public static OrderEntry mapOrderEntryEntityToOrderEntryDomain(OrderEntryEntity entryEntity){
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setId(entryEntity.getId());
        orderEntry.setProductId(entryEntity.getProduct().getId());
        orderEntry.setQuantity(entryEntity.getQuantity());
        orderEntry.setStored(entryEntity.isStored());
        return orderEntry;
    }

    public static OrderEntryEntity mapOrderEntryDomainToOrderEntryEntity(OrderEntry orderEntry, OrderEntity parent){
        OrderEntryEntity entity = new OrderEntryEntity();
        entity.setQuantity(orderEntry.getQuantity());
        entity.setProductId(orderEntry.getProductId());
        entity.setOrder(parent);
        entity.setStored(orderEntry.isStored());
        entity.setId(orderEntry.getId());
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }

    //mapper for filter properties
    //map only user defined values, primitives are left out for optimization
    private static Map<String, Object> mapErasureFilterProperties(Map<String, Object> domainProperties){
        Map<String, Object> entityProperties = new HashMap<>();
        domainProperties.forEach((key, value) -> {
            if(value instanceof Order.Constants.Status){
                entityProperties.put(key, Utilities.getKeyByValue(statusMapping, (Order.Constants.Status)value));
            }else{
                entityProperties.put(key, value);
            }
        });
        return entityProperties;
    }

    //mapper for entity class
    public static List<Utilities.Filter> mapErasureFilter(List<Utilities.Filter> filters){
        return filters.stream().map(e -> {
            Utilities.Filter newFilter = new Utilities.Filter();
            newFilter.setProperties(mapErasureFilterProperties(e.getProperties()));
            if(e.getKlass().equals(User.class)){
                newFilter.setKlass(UserEntity.class);
            }
            if(e.getKlass().equals(Order.class)){
                newFilter.setKlass(OrderEntity.class);
            }
            return newFilter;
        }).collect(Collectors.toList());
    }
}

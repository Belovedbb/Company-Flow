package com.company.go.billing.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.billing.account.Constants;
import com.company.go.billing.account.order.OrderAccountEntity;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.PurchaseOrderAccount;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class OrderAccountMapper {
    private static final Map<Constants.Kind, Account.Constants.Kind> kindMapping;
    private static final Map<Constants.Type, Account.Constants.Type> typeMapping;
    
    static {
        kindMapping = new EnumMap<>(Constants.Kind.class);

        kindMapping.put(Constants.Kind.CREDIT, Account.Constants.Kind.CREDIT);
        kindMapping.put(Constants.Kind.DEBIT, Account.Constants.Kind.DEBIT);

        typeMapping = new EnumMap<>(Constants.Type.class);
        typeMapping.put(Constants.Type.PURCHASEORDER, Account.Constants.Type.PURCHASEORDER);
        typeMapping.put(Constants.Type.PERFORMANCE, Account.Constants.Type.PERFORMANCE);
        typeMapping.put(Constants.Type.PRODUCT, Account.Constants.Type.PRODUCT);
    }

    static public PurchaseOrderAccount mapOrderAccountEntityToOrderAccountDomain(OrderAccountEntity entity){
        PurchaseOrderAccount purchaseOrderAccount = new PurchaseOrderAccount();
        purchaseOrderAccount.setId(entity.getId());
        purchaseOrderAccount.setDate(entity.getLastChanged().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        purchaseOrderAccount.setKind(kindMapping.get(entity.getKind()));
        purchaseOrderAccount.setType(entity.getType());
        purchaseOrderAccount.setAggregateAmount(entity.getAggregateAmount());
        purchaseOrderAccount.setTypeCount(entity.getTypeCount());
        return purchaseOrderAccount;
    }

    static public OrderAccountEntity mapOrderAccountDomainToOrderAccountEntity(PurchaseOrderAccount purchaseOrderAccount){
        OrderAccountEntity entity = new OrderAccountEntity();
        entity.setId(purchaseOrderAccount.getId());
        entity.setAggregateAmount(purchaseOrderAccount.getAggregateAmount());
        entity.setKind(Utilities.getKeyByValue(kindMapping, purchaseOrderAccount.getKind()));
        Date dateCreated = purchaseOrderAccount.getDate() == null ? null : Date.from(purchaseOrderAccount.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        entity.setDateCreated(dateCreated);
        entity.setType(purchaseOrderAccount.getType());
        entity.setTypeCount(purchaseOrderAccount.getTypeCount());
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }
}

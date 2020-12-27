package com.company.go.billing.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.billing.account.Constants;
import com.company.go.billing.account.performance.PerformanceAccountEntity;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.PerformanceAccount;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class PerformanceAccountMapper {
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

    static public PerformanceAccount mapPerformanceAccountEntityToPerformanceAccountDomain(PerformanceAccountEntity entity){
        PerformanceAccount purchasePerformanceAccount = new PerformanceAccount();
        purchasePerformanceAccount.setId(entity.getId());
        purchasePerformanceAccount.setDate(entity.getLastChanged().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        purchasePerformanceAccount.setKind(kindMapping.get(entity.getKind()));
        purchasePerformanceAccount.setType(entity.getType());
        purchasePerformanceAccount.setAggregateAmount(entity.getAggregateAmount());
        purchasePerformanceAccount.setTypeCount(entity.getTypeCount());
        return purchasePerformanceAccount;
    }

    static public PerformanceAccountEntity mapPerformanceAccountDomainToPerformanceAccountEntity(PerformanceAccount purchasePerformanceAccount){
        PerformanceAccountEntity entity = new PerformanceAccountEntity();
        entity.setId(purchasePerformanceAccount.getId());
        entity.setAggregateAmount(purchasePerformanceAccount.getAggregateAmount());
        entity.setKind(Utilities.getKeyByValue(kindMapping, purchasePerformanceAccount.getKind()));
        Date dateCreated = purchasePerformanceAccount.getDate() == null ? null : Date.from(purchasePerformanceAccount.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        entity.setDateCreated(dateCreated);
        entity.setType(purchasePerformanceAccount.getType());
        entity.setTypeCount(purchasePerformanceAccount.getTypeCount());
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }
}
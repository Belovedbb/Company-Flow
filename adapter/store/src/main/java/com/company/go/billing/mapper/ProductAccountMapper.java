package com.company.go.billing.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.billing.account.Constants;
import com.company.go.billing.account.product.ProductAccountEntity;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.ProductAccount;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class ProductAccountMapper {
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

    static public ProductAccount mapProductAccountEntityToProductAccountDomain(ProductAccountEntity entity){
        ProductAccount purchaseProductAccount = new ProductAccount();
        purchaseProductAccount.setId(entity.getId());
        purchaseProductAccount.setDate(entity.getLastChanged().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        purchaseProductAccount.setKind(kindMapping.get(entity.getKind()));
        purchaseProductAccount.setType(entity.getType());
        purchaseProductAccount.setAggregateAmount(entity.getAggregateAmount());
        purchaseProductAccount.setTypeCount(entity.getTypeCount());
        return purchaseProductAccount;
    }

    static public ProductAccountEntity mapProductAccountDomainToProductAccountEntity(ProductAccount purchaseProductAccount){
        ProductAccountEntity entity = new ProductAccountEntity();
        entity.setId(purchaseProductAccount.getId());
        entity.setAggregateAmount(purchaseProductAccount.getAggregateAmount());
        entity.setKind(Utilities.getKeyByValue(kindMapping, purchaseProductAccount.getKind()));
        Date dateCreated = purchaseProductAccount.getDate() == null ? null : Date.from(purchaseProductAccount.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        entity.setDateCreated(dateCreated);
        entity.setType(purchaseProductAccount.getType());
        entity.setTypeCount(purchaseProductAccount.getTypeCount());
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }
}
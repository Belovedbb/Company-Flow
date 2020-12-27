package com.company.go.inventory.mapper;

import com.company.go.StoreUtilities;
import com.company.go.Utilities;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;
import com.company.go.inventory.product.Constants;
import com.company.go.inventory.product.ProductEntity;
import com.company.go.inventory.product.ProductTypeEntity;

import java.time.ZoneId;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

public class ProductMapper {

    private static final Map<Constants.ProductStatus, Product.Constants.Status> statusMapping;
    private static final Map<Constants.ProductStore, Product.Constants.Store> storeMapping;
    private static final Map<Constants.ProductCategory, ProductType.Constants.Category> categoryMapping;
    private static final Map<Constants.ProductCategoryStatus, ProductType.Constants.Status> categoryStatusMapping;

    static {
        statusMapping = new EnumMap<>(Constants.ProductStatus.class);
        storeMapping = new EnumMap<>(Constants.ProductStore.class);
        categoryMapping = new EnumMap<>(Constants.ProductCategory.class);
        categoryStatusMapping = new EnumMap<>(Constants.ProductCategoryStatus.class);

        statusMapping.put(Constants.ProductStatus.ACTIVE, Product.Constants.Status.ACTIVE);
        statusMapping.put(Constants.ProductStatus.INACTIVE, Product.Constants.Status.INACTIVE);

        storeMapping.put(Constants.ProductStore.FAULTY, Product.Constants.Store.FAULTY);
        storeMapping.put(Constants.ProductStore.READY_FOR_DISPOSAL, Product.Constants.Store.READY_FOR_DISPOSAL);
        storeMapping.put(Constants.ProductStore.READY_FOR_RECYCLE, Product.Constants.Store.READY_FOR_RECYCLE);
        storeMapping.put(Constants.ProductStore.WAITING, Product.Constants.Store.WAITING);

        categoryMapping.put(Constants.ProductCategory.CONSUMABLE, ProductType.Constants.Category.CONSUMABLE);
        categoryMapping.put(Constants.ProductCategory.NON_CONSUMABLE, ProductType.Constants.Category.NON_CONSUMABLE);

        categoryStatusMapping.put(Constants.ProductCategoryStatus.ACTIVE, ProductType.Constants.Status.ACTIVE);
        categoryStatusMapping.put(Constants.ProductCategoryStatus.INACTIVE, ProductType.Constants.Status.INACTIVE);
    }

    public static ProductType mapProductTypeEntityToProductTypeDomain(ProductTypeEntity entity){
        ProductType productType = new ProductType();
        productType.setId(entity.getId());
        productType.setName(entity.getValue());
        productType.setCategory(categoryMapping.get(entity.getCategory()));
        productType.setStatus(categoryStatusMapping.get(entity.getCategoryStatus()));
        return productType;
    }

    public static ProductTypeEntity mapProductTypeDomainToProductTypeEntity(ProductType productType){
        ProductTypeEntity entity = new ProductTypeEntity();
        entity.setId(productType.getId());
        entity.setValue(productType.getName());
        entity.setCategory(Utilities.getKeyByValue(categoryMapping, productType.getCategory()));
        entity.setCategoryStatus(Utilities.getKeyByValue(categoryStatusMapping, productType.getStatus()));
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }

    static public Product mapProductEntityToProductDomain(ProductEntity entity){
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setAmount(new Money(Money.doubleToBigDecimal(entity.getAmount()), entity.getCurrency()));
        product.setDescription(entity.getDescription());
        product.setExpiryDate(entity.getExpiryDate());
        product.setManufacturedDate(entity.getManufacturedDate());
        product.setQuantity(entity.getQuantity());
        product.setPurchasedDate(entity.getPurchasedDate());
        product.setWarrantyPeriodInMonths(entity.getWarrantyPeriodInMonths());
        product.setSuppliedDate(entity.getSuppliedDate());
        product.setLastChangedDate(entity.getLastChanged().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
        product.setType(mapProductTypeEntityToProductTypeDomain(entity.getProductType()));
        product.setStatus(statusMapping.get(entity.getStatus()));
        product.setStore(storeMapping.get(entity.getSubStatus()));
        return product;
    }

    static public ProductEntity mapProductDomainToProductEntity(Product product){
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setAmount(product.getAmount().getValue().doubleValue());
        entity.setCurrency(product.getAmount().getCurrency());
        entity.setDescription(product.getDescription());
        entity.setExpiryDate(product.getExpiryDate());
        entity.setManufacturedDate(product.getManufacturedDate());
        entity.setQuantity(product.getQuantity());
        entity.setPurchasedDate(product.getPurchasedDate());
        entity.setWarrantyPeriodInMonths(product.getWarrantyPeriodInMonths());
        entity.setSuppliedDate(product.getSuppliedDate());
        entity.setProductType(mapProductTypeDomainToProductTypeEntity(product.getType()));
        Date lastChangedDate = product.getLastChangedDate() == null ? null : Date.from(product.getLastChangedDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        entity.setLastChanged(lastChangedDate);
        entity.setStatus(Utilities.getKeyByValue(statusMapping, product.getStatus()));
        entity.setSubStatus(Utilities.getKeyByValue(storeMapping, product.getStore()));
        entity.setCompanyRegisterer(StoreUtilities.getUser());
        return entity;
    }

}

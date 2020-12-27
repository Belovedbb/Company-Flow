package com.company.go.application.service.inventory;

import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.out.inventory.UpdateProductPort;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;
import com.company.go.properties.CompanyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductUseCase {
    private UpdateProductPort productStoreAdapter;
    private CompanyProperties properties;

    @Autowired
    public ProductService(UpdateProductPort productStoreAdapter, CompanyProperties properties) {
        this.productStoreAdapter = productStoreAdapter;
        this.properties = properties;
    }

    @Override
    public boolean storeProduct(ProductViewModel model) {
        Product product = model.toProduct();
        BigDecimal amount  = product.getAmount().getValue() ;
        product.setAmount(new Money(amount, Currency.getInstance(properties.getCurrency())));
        product.setType(productStoreAdapter.findProductType(product.getType().getName()));
        return productStoreAdapter.storeProduct(product);
    }

    @Override
    public boolean editProduct(Long id, ProductViewModel currentModel) {
        Product product = currentModel.toProduct();
        BigDecimal amount  = product.getAmount().getValue() ;
        product.setAmount(new Money(amount, Currency.getInstance(properties.getCurrency())));
        product.setType(productStoreAdapter.findProductType(product.getType().getName()));
        return productStoreAdapter.updateProduct(id, product);
    }

    @Override
    public ProductViewModel viewProduct(Long id) {
        return convert(productStoreAdapter.getProduct(id));
    }

    @Override
    public List<ProductViewModel> getAllProducts() {
        return productStoreAdapter.getTotalProduct()
                .stream().map(this::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductViewModel> getFilteredProducts(ProductViewModel criteriaModel) {
        Map<String, Object> filterMap = new HashMap<>();
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
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productStoreAdapter.removeProduct(id);
    }

    public List<String> getProductCategory(){
        final List<ProductType> categories = productStoreAdapter.getTotalProductCategory();
        final List<String> configurationProperties = properties.getProductTypes();
        if( configurationProperties.size() > categories.size()){
            for(int i = 0; i < configurationProperties.size(); i++){
                if (CollectionUtils.isEmpty(categories) ||
                        (productStoreAdapter.findProductType(configurationProperties.get(i)) == null)){
                    ProductType productType = new ProductType();
                    productType.setCategory(ProductType.Constants.Category.NON_CONSUMABLE);
                    productType.setName(configurationProperties.get(i));
                    productType.setStatus(ProductType.Constants.Status.INACTIVE);
                    productStoreAdapter.storeProductCategory(productType);
                }
            }
        }
        List<String> productCategories = new ArrayList<>();
        productStoreAdapter.getTotalProductCategory().stream()
                .filter(e -> e.getStatus().equals(ProductType.Constants.Status.INACTIVE))
                .map(ProductType::getName)
                .collect(Collectors.toCollection(() -> productCategories));
        return productCategories;
    }

    @Override
    public Long getProductMaxId() {
        return productStoreAdapter.getProductMaxId();
    }

    private ProductViewModel convert(Product product){
        ProductViewModel productViewModel = new ProductViewModel();
        productViewModel.setId(product.getId());
        productViewModel.setName(product.getName());
        productViewModel.setSuppliedDate(product.getSuppliedDate().toString());
        productViewModel.setAmount(product.getAmount().getValue().doubleValue());
        productViewModel.setCategory(product.getType().getName());
        productViewModel.setCurrency(product.getAmount().getCurrency().getCurrencyCode());
        productViewModel.setDescription(product.getDescription());
        productViewModel.setQuantity(product.getQuantity());
        productViewModel.setExpiryDate(product.getExpiryDate().toString());
        productViewModel.setManufacturedDate(product.getManufacturedDate().toString());
        productViewModel.setPurchasedDate(product.getPurchasedDate().toString());
        productViewModel.setWarrantyPeriodInMonths(product.getWarrantyPeriodInMonths());
        productViewModel.setLastChangedDate(product.getLastChangedDate().toString());
        productViewModel.setStatus(product.getStatus().name());
        productViewModel.setInactiveSubStatus(product.getStore().toString());
        return productViewModel;
    }
}

package com.company.go.application.port.out.billing;

import com.company.go.domain.billing.ProductAccount;

import java.util.List;
import java.util.Map;

public interface UpdateProductAccountPort {

    boolean storeProductAccount(ProductAccount productAccount);

    boolean updateProductAccount(Long id, ProductAccount currentProductAccount);

    ProductAccount getProductAccount(Long id);

    boolean removeProductAccount(Long id);

    Long getProductAccountMaxId();

    List<ProductAccount> getTotalProductAccount();

    List<ProductAccount> getTotalFilteredProductAccount(Map<String, Object> parameters);
    
}

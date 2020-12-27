package com.company.go.application.port.out.inventory;

import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;

import java.util.List;
import java.util.Map;

public interface UpdateProductPort {
    boolean storeProduct(Product product);

    boolean updateProduct(Long id, Product currentProduct);

    Product getProduct(Long id);

    boolean removeProduct(Long id);

    void storeProductCategory(ProductType productCategory);

    ProductType findProductType(String name);

    Long getProductMaxId();

    List<Product> getTotalProduct();

    List<Product> getTotalFilteredProduct(Map<String, Object> parameters);

    List<ProductType> getTotalProductCategory();

}

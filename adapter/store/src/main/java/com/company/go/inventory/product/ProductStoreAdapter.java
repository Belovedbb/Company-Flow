package com.company.go.inventory.product;

import com.company.go.application.port.out.inventory.UpdateProductPort;
import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;
import com.company.go.inventory.mapper.ProductMapper;
import com.company.go.inventory.product.repo.ProductRepository;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
public class ProductStoreAdapter implements UpdateProductPort {

    private ProductRepository productRepo;

    @Autowired
    public ProductStoreAdapter(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public boolean storeProduct(Product product) {
        try{
            productRepo.createProduct(ProductMapper.mapProductDomainToProductEntity(product));
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateProduct(Long id, Product currentProduct) {
        try {
            var product = ProductMapper.mapProductDomainToProductEntity(currentProduct);
            productRepo.updateProduct(product);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Product getProduct(Long id) {
        return ProductMapper.mapProductEntityToProductDomain(productRepo.getProduct(id));
    }

    public List<Product> getTotalProduct(){
        return productRepo.getAllRows(ProductEntity.class).stream()
                .map(ProductMapper::mapProductEntityToProductDomain).collect(Collectors.toList());
    }

    public List<Product> getTotalFilteredProduct(Map<String, Object> parameters){
        return productRepo.filterProduct(parameters).stream()
                .map(ProductMapper::mapProductEntityToProductDomain).collect(Collectors.toList());
    }

    public List<ProductType> getTotalProductCategory(){
        return productRepo.getAllRows(ProductTypeEntity.class).stream()
                .map(ProductMapper::mapProductTypeEntityToProductTypeDomain).collect(Collectors.toList());
    }

    public void storeProductCategory(ProductType productType){
        productRepo.saveOrUpdateProductCategory(ProductMapper.mapProductTypeDomainToProductTypeEntity(productType));
    }

    public ProductType findProductType(String name){
        try{
            return ProductMapper.mapProductTypeEntityToProductTypeDomain(productRepo.findProductTypeByName(name));
        }catch (NoResultException ex){
            return null;
        }
    }
    public  Long getProductMaxId(){
        try{
            return productRepo.getMaxId(ProductEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public boolean removeProduct(Long id){
        try{
            productRepo.deleteProduct(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

}

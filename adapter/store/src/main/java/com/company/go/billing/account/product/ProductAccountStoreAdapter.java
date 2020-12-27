package com.company.go.billing.account.product;

import com.company.go.application.port.out.billing.UpdateProductAccountPort;
import com.company.go.billing.account.product.repo.ProductAccountRepository;
import com.company.go.billing.mapper.ProductAccountMapper;
import com.company.go.domain.billing.ProductAccount;
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
public class ProductAccountStoreAdapter implements UpdateProductAccountPort {

    private ProductAccountRepository productAccountRepository;

    @Autowired
    ProductAccountStoreAdapter(ProductAccountRepository productAccountRepository){
        this.productAccountRepository = productAccountRepository;
    }

    @Override
    public boolean storeProductAccount(ProductAccount productAccount) {
        try{
            ProductAccountEntity productAccountEntity = ProductAccountMapper.mapProductAccountDomainToProductAccountEntity(productAccount);
            productAccountRepository.createProductAccount(productAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateProductAccount(Long id, ProductAccount currentProductAccount) {
        try {
            ProductAccountEntity productAccountEntity = ProductAccountMapper.mapProductAccountDomainToProductAccountEntity(currentProductAccount);
            productAccountRepository.updateProductAccount(productAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public ProductAccount getProductAccount(Long id) {
        return ProductAccountMapper.mapProductAccountEntityToProductAccountDomain(productAccountRepository.getProductAccount(id));
    }

    @Override
    public boolean removeProductAccount(Long id) {
        try{
            productAccountRepository.deleteProductAccount(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Long getProductAccountMaxId() {
        try{
            return productAccountRepository.getMaxId(ProductAccountEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<ProductAccount> getTotalProductAccount() {
        return productAccountRepository.getAllRows(ProductAccountEntity.class).stream()
                .map(ProductAccountMapper::mapProductAccountEntityToProductAccountDomain).collect(Collectors.toList());
    }

    @Override
    public List<ProductAccount> getTotalFilteredProductAccount(Map<String, Object> parameters) {
        return productAccountRepository.filterProductAccount(parameters).stream()
                .map(ProductAccountMapper::mapProductAccountEntityToProductAccountDomain).collect(Collectors.toList());
    }
}

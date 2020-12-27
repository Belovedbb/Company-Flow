package com.company.go.application.service.billing;

import com.company.go.application.port.Mapper;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.out.billing.UpdatePerformanceAccountPort;
import com.company.go.application.port.out.billing.UpdateProductAccountPort;
import com.company.go.application.port.out.billing.UpdatePurchaseOrderAccountPort;
import com.company.go.domain.billing.Account;
import com.company.go.domain.billing.PerformanceAccount;
import com.company.go.domain.billing.ProductAccount;
import com.company.go.domain.billing.PurchaseOrderAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountService implements AccountUseCase {

    @Autowired
    UpdatePerformanceAccountPort performanceAccountStore;

    @Autowired
    UpdatePurchaseOrderAccountPort purchaseOrderAccountStore;

    @Autowired
    UpdateProductAccountPort productAccountStore;

    @Override
    public boolean storeAccount(AccountViewModel model) {
        if(model instanceof PerformanceAccountViewModel){
            PerformanceAccount performanceAccount = ((PerformanceAccountViewModel) model).toPerformanceAccount();
            return performanceAccountStore.storePerformanceAccount(performanceAccount);
        }else if(model instanceof ProductAccountViewModel){
            ProductAccount productAccount = ((ProductAccountViewModel) model).toProductAccount();
            return productAccountStore.storeProductAccount(productAccount);
        }else if(model instanceof PurchaseOrderAccountViewModel){
            PurchaseOrderAccount purchaseOrderAccount = ((PurchaseOrderAccountViewModel) model).toPurchaseOrderAccount();
            return purchaseOrderAccountStore.storePurchaseOrderAccount(purchaseOrderAccount);
        }
        return false;
    }

    @Override
    public boolean editAccount(Long id, AccountViewModel currentModel) {
        if(currentModel instanceof PerformanceAccountViewModel){
            PerformanceAccount performanceAccount = ((PerformanceAccountViewModel) currentModel).toPerformanceAccount();
            return performanceAccountStore.updatePerformanceAccount(id, performanceAccount);
        }else if(currentModel instanceof ProductAccountViewModel){
            ProductAccount productAccount = ((ProductAccountViewModel) currentModel).toProductAccount();
            return productAccountStore.updateProductAccount(id, productAccount);
        }else if(currentModel instanceof PurchaseOrderAccountViewModel){
            PurchaseOrderAccount purchaseOrderAccount = ((PurchaseOrderAccountViewModel) currentModel).toPurchaseOrderAccount();
            return purchaseOrderAccountStore.updatePurchaseOrderAccount(id, purchaseOrderAccount);
        }
        return false;
    }

    @Override
    public AccountViewModel viewAccount(Long id, String type) {
        if(Account.Constants.Type.PERFORMANCE.name().equalsIgnoreCase(type)){
            return Mapper.convert(performanceAccountStore.getPerformanceAccount(id));
        }else if(Account.Constants.Type.PRODUCT.name().equalsIgnoreCase(type)){
            return Mapper.convert(productAccountStore.getProductAccount(id));
        }else if(Account.Constants.Type.PURCHASEORDER.name().equalsIgnoreCase(type)){
            return Mapper.convert(purchaseOrderAccountStore.getPurchaseOrderAccount(id));
        }
        return null;
    }

    @Override
    public List<AccountViewModel> getAllAccounts() {
        return getAllAccounts(Account.Constants.Type.AGGREGATE.name());
    }

    @Override
    public List<AccountViewModel> getAllAccounts(String type) {
        if(Account.Constants.Type.PERFORMANCE.name().equalsIgnoreCase(type)){
            return getPerformanceAccounts();
        }else if(Account.Constants.Type.PRODUCT.name().equalsIgnoreCase(type)){
            return getProductAccounts();
        }else if(Account.Constants.Type.PURCHASEORDER.name().equalsIgnoreCase(type)){
            return getPurchaseOrderAccounts();
        }else if(Account.Constants.Type.AGGREGATE.name().equalsIgnoreCase(type)){
            List<AccountViewModel> allAccounts = new LinkedList<>();
            allAccounts.addAll(getPerformanceAccounts());
            allAccounts.addAll(getProductAccounts());
            allAccounts.addAll(getPurchaseOrderAccounts());
            return allAccounts;
        }
        return List.of();
    }

    private List<AccountViewModel> getPerformanceAccounts(){
        return performanceAccountStore.getTotalPerformanceAccount().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    private List<AccountViewModel> getProductAccounts(){
        return productAccountStore.getTotalProductAccount().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    private List<AccountViewModel> getPurchaseOrderAccounts(){
        return purchaseOrderAccountStore.getTotalPurchaseOrderAccount().stream().map(Mapper::convert).collect(Collectors.toList());
    }

    @Override
    public List<AccountViewModel> getFilteredAccounts(AccountViewModel criteriaModel, String type) {
        if(Account.Constants.Type.PERFORMANCE.name().equalsIgnoreCase(type)){
            return getPerformanceFilteredAccounts(criteriaModel);
        }else if(Account.Constants.Type.PRODUCT.name().equalsIgnoreCase(type)){
            return getProductFilteredAccounts(criteriaModel);
        }else if(Account.Constants.Type.PURCHASEORDER.name().equalsIgnoreCase(type)){
            return getPurchaseOrderFilteredAccounts(criteriaModel);
        }else if(Account.Constants.Type.AGGREGATE.name().equalsIgnoreCase(type)){
            List<AccountViewModel> allAccounts = new LinkedList<>();
            allAccounts.addAll(getPerformanceFilteredAccounts(criteriaModel));
            allAccounts.addAll(getProductFilteredAccounts(criteriaModel));
            allAccounts.addAll(getPurchaseOrderFilteredAccounts(criteriaModel));
            return allAccounts;
        }
        return List.of();
    }

    private List<AccountViewModel> getProductFilteredAccounts(AccountViewModel criteriaModel){
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(criteriaModel.getDate() != null){
                filterMap.put("lastChangedDate", criteriaModel.getDate());
            }
        }
        return productAccountStore.getTotalFilteredProductAccount(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    private List<AccountViewModel> getPurchaseOrderFilteredAccounts(AccountViewModel criteriaModel){
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(criteriaModel.getDate() != null){
                filterMap.put("lastChangedDate", criteriaModel.getDate());
            }
        }
        return purchaseOrderAccountStore.getTotalFilteredPurchaseOrderAccount(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    private List<AccountViewModel> getPerformanceFilteredAccounts(AccountViewModel criteriaModel){
        Map<String, Object> filterMap = new HashMap<>();
        if(criteriaModel != null){
            if(criteriaModel.getDate() != null){
                filterMap.put("lastChangedDate", criteriaModel.getDate());
            }
        }
        return performanceAccountStore.getTotalFilteredPerformanceAccount(filterMap)
                .stream().map(Mapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteAccount(Long id, String type) {
        if(Account.Constants.Type.PERFORMANCE.name().equalsIgnoreCase(type)){
            return performanceAccountStore.removePerformanceAccount(id);
        }else if(Account.Constants.Type.PRODUCT.name().equalsIgnoreCase(type)){
            return productAccountStore.removeProductAccount(id);
        }else if(Account.Constants.Type.PURCHASEORDER.name().equalsIgnoreCase(type)){
            return purchaseOrderAccountStore.removePurchaseOrderAccount(id);
        }else if(Account.Constants.Type.AGGREGATE.name().equalsIgnoreCase(type)){
            performanceAccountStore.removePerformanceAccount(id);
            productAccountStore.removeProductAccount(id);
            purchaseOrderAccountStore.removePurchaseOrderAccount(id);
            return true;
        }
        return false;
    }

    @Override
    public Long getAccountMaxId(String type) {
        if(Account.Constants.Type.PERFORMANCE.name().equalsIgnoreCase(type)){
            return performanceAccountStore.getPerformanceAccountMaxId();
        }else if(Account.Constants.Type.PRODUCT.name().equalsIgnoreCase(type)){
            return productAccountStore.getProductAccountMaxId();
        }else if(Account.Constants.Type.PURCHASEORDER.name().equalsIgnoreCase(type)){
            return purchaseOrderAccountStore.getPurchaseOrderAccountMaxId();
        }
        return -1L;
    }

}

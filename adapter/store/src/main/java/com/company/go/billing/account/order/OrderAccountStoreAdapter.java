package com.company.go.billing.account.order;

import com.company.go.application.port.out.billing.UpdatePurchaseOrderAccountPort;
import com.company.go.billing.account.order.repo.OrderAccountRepository;
import com.company.go.billing.mapper.OrderAccountMapper;
import com.company.go.domain.billing.PurchaseOrderAccount;
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
public class OrderAccountStoreAdapter implements UpdatePurchaseOrderAccountPort {
    private OrderAccountRepository orderAccountRepository;

    @Autowired
    OrderAccountStoreAdapter(OrderAccountRepository orderAccountRepository){
        this.orderAccountRepository = orderAccountRepository;
    }

    @Override
    public boolean storePurchaseOrderAccount(PurchaseOrderAccount purchaseOrderAccount) {
        try{
            OrderAccountEntity orderAccountEntity = OrderAccountMapper.mapOrderAccountDomainToOrderAccountEntity(purchaseOrderAccount);
            orderAccountRepository.createOrderAccount(orderAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePurchaseOrderAccount(Long id, PurchaseOrderAccount currentPurchaseOrderAccount) {
        try {
            OrderAccountEntity orderAccountEntity = OrderAccountMapper.mapOrderAccountDomainToOrderAccountEntity(currentPurchaseOrderAccount);
            orderAccountRepository.updateOrderAccount(orderAccountEntity);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public PurchaseOrderAccount getPurchaseOrderAccount(Long id) {
        return OrderAccountMapper.mapOrderAccountEntityToOrderAccountDomain(orderAccountRepository.getOrderAccount(id));
    }

    @Override
    public boolean removePurchaseOrderAccount(Long id) {
        try{
            orderAccountRepository.deleteOrderAccount(id);
        }catch (HibernateException ex){
            return false;
        }
        return true;
    }

    @Override
    public Long getPurchaseOrderAccountMaxId() {
        try{
            return orderAccountRepository.getMaxId(OrderAccountEntity.class);
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<PurchaseOrderAccount> getTotalPurchaseOrderAccount() {
        return orderAccountRepository.getAllRows(OrderAccountEntity.class).stream()
                .map(OrderAccountMapper::mapOrderAccountEntityToOrderAccountDomain).collect(Collectors.toList());
    }

    @Override
    public List<PurchaseOrderAccount> getTotalFilteredPurchaseOrderAccount(Map<String, Object> parameters) {
        return orderAccountRepository.filterOrderAccount(parameters).stream()
                .map(OrderAccountMapper::mapOrderAccountEntityToOrderAccountDomain).collect(Collectors.toList());
    }
}

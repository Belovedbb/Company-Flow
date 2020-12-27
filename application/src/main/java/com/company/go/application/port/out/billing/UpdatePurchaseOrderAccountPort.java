package com.company.go.application.port.out.billing;

import com.company.go.domain.billing.PurchaseOrderAccount;

import java.util.List;
import java.util.Map;

public interface UpdatePurchaseOrderAccountPort {

    boolean storePurchaseOrderAccount(PurchaseOrderAccount purchaseOrderAccount);

    boolean updatePurchaseOrderAccount(Long id, PurchaseOrderAccount currentPurchaseOrderAccount);

    PurchaseOrderAccount getPurchaseOrderAccount(Long id);

    boolean removePurchaseOrderAccount(Long id);

    Long getPurchaseOrderAccountMaxId();

    List<PurchaseOrderAccount> getTotalPurchaseOrderAccount();

    List<PurchaseOrderAccount> getTotalFilteredPurchaseOrderAccount(Map<String, Object> parameters);
    
}

package com.company.go.application.port.in.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


public class NotExceedProductSizeValidator implements ConstraintValidator<NotExceedProductSize, Object> {

    @Autowired
    ProductUseCase productUseCase;

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        if(object instanceof PurchaseOrderUseCase.PurchaseOrderViewModel) {
            PurchaseOrderUseCase.PurchaseOrderViewModel order = (PurchaseOrderUseCase.PurchaseOrderViewModel) object;
            List<PurchaseOrderUseCase.PurchaseOrderEntryViewModel> productOrders = order.getPurchaseOrderEntries();
            if(!CollectionUtils.isEmpty(productOrders)){
                for(PurchaseOrderUseCase.PurchaseOrderEntryViewModel entry : productOrders){
                    Long inputQuantity = entry.getOrderQuantity();
                    ProductUseCase.ProductViewModel product = productUseCase.viewProduct(entry.getProduct().getId());
                    Long productMaxQuantity = product.getQuantity();
                    valid = inputQuantity <= productMaxQuantity;
                    if(!valid)break;
                }
            }
        }
        return valid;
    }
}

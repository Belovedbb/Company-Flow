package com.company.go.application.port.in.billing;

import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.company.go.domain.billing.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface AccountUseCase {

    boolean storeAccount(AccountUseCase.AccountViewModel model);

    boolean editAccount(Long id, AccountUseCase.AccountViewModel currentModel);

    AccountUseCase.AccountViewModel viewAccount(Long id, String type);

    List<AccountUseCase.AccountViewModel> getAllAccounts();

    List<AccountUseCase.AccountViewModel> getAllAccounts(String type);

    List<AccountUseCase.AccountViewModel> getFilteredAccounts(AccountUseCase.AccountViewModel criteriaModel, String type);

    boolean deleteAccount(Long id, String type);

    Long getAccountMaxId(String type);

    public interface AccountViewModel{
        
        Long getId();
        String getType();
        String getKind();
        LocalDate getDate();
        Double getAggregateAmount();
        Long getTypeCount();

        public default List<String> loadedType(){
            return List.of();
        }

        default List<String> loadedType2(){
            return List.of();
        }
    }

    @Getter
    @Setter
    class PurchaseOrderAccountViewModel implements AccountViewModel{
        private Long id;

        private String type;

        private Double aggregateAmount;

        private LocalDate date;

        private String kind;

        private Long typeCount;

        public PurchaseOrderAccount toPurchaseOrderAccount() {
            return  new PurchaseOrderAccount(
                    id,
                    type,
                    aggregateAmount,
                    date,
                    typeCount,
                    PurchaseOrderAccount.Constants.Kind.valueOf(kind)
            );
        }
    }

    @Getter
    @Setter
    class PerformanceAccountViewModel implements AccountViewModel{
        private Long id;

        private String type;

        Double aggregateAmount;

        LocalDate date;

        private String kind;

        Long typeCount;

        public PerformanceAccount toPerformanceAccount() {
            return  new PerformanceAccount(
                    id,
                    type,
                    aggregateAmount,
                    date,
                    typeCount,
                    PerformanceAccount.Constants.Kind.valueOf(kind)
            );
        }
    }

    @Getter
    @Setter
    class ProductAccountViewModel implements AccountViewModel{
        private Long id;

        private String type;

        Double aggregateAmount;

        LocalDate date;

        private String kind;

        Long typeCount;

        public ProductAccount toProductAccount() {
            return  new ProductAccount(
                    id,
                    type,
                    aggregateAmount,
                    date,
                    typeCount,
                    ProductAccount.Constants.Kind.valueOf(kind)
            );
        }
    }

    @Getter
    @Setter
    class AggregateAccountViewModel implements AccountViewModel{
        private Long id;

        private String type;

        Double aggregateAmount;

        LocalDate date;

        private String kind;

        Long typeCount;

        public List<String> getLoadedKind(){
            return List.of(Account.Constants.Kind.CREDIT.name(),
                    Account.Constants.Kind.DEBIT.name());
        }

        public List<String> getLoadedType(){
            return List.of(Account.Constants.Type.PERFORMANCE.name(),
                    Account.Constants.Type.PURCHASEORDER.name(),
                    Account.Constants.Type.PRODUCT.name());
        }

        public AggregateAccount toAggregateAccount() {
            return  new AggregateAccount(
                    id,
                    type,
                    aggregateAmount,
                    date,
                    typeCount,
                    AggregateAccount.Constants.Kind.valueOf(kind)
            );
        }
    }
}

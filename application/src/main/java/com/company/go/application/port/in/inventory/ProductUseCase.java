package com.company.go.application.port.in.inventory;

import com.company.go.Utilities;
import com.company.go.domain.inventory.Money;
import com.company.go.domain.inventory.product.Product;
import com.company.go.domain.inventory.product.ProductType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public interface ProductUseCase {

    boolean storeProduct(ProductViewModel model);

    boolean editProduct(Long id, ProductViewModel currentModel);

    ProductViewModel viewProduct(Long id);

    List<ProductViewModel> getAllProducts();

    List<ProductViewModel> getFilteredProducts(ProductViewModel criteriaModel);

    boolean deleteProduct(Long id);

    List<String> getProductCategory();

    Long getProductMaxId();

    @Getter
    @Setter
    class ProductViewModel{

        private Long id;
        @NotEmpty(message = "Category cannot be empty")
        private String category;
        @NotEmpty(message = "Input Product Name")
        private String name;
        @NotEmpty(message = "Input Description")
        private String description;
        private String currency;
        @NotNull(message = "Quantity cannot be empty")
        @PositiveOrZero(message = "Quantity must be greater than 0")
        private Long quantity;
        @NotNull
        private double amount;
        @NotEmpty(message = "Supplied Date must not be empty")
        @DateTimeFormat( pattern = "YYYY/MM/dd HH:mm")
        private String suppliedDate;
        @NotEmpty(message = "Manufactured Date must not be empty")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private String manufacturedDate;
        private int warrantyPeriodInMonths;
        @NotEmpty(message = "Purchased Date must not be empty")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private String purchasedDate;
        @NotEmpty(message = "Expiry Date must not be empty")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private String expiryDate;
        @NotEmpty(message = "Status must not be empty")
        private String lastChangedDate;
        private String status;
        private String inactiveSubStatus;

        private LocalDateTime convertToDate(String date){
            return StringUtils.isEmpty(date) ? null : LocalDateTime.parse(date, Utilities.getDateTimeFormatter());
        }

        public List<String> getLoadedStatus(){
            ArrayList<String> statuses = new ArrayList<>();
            statuses.add(Product.Constants.Status.ACTIVE.name());
            statuses.add(Product.Constants.Status.INACTIVE.name());
            return statuses;
        }
        public List<String> getLoadedStore(){
            ArrayList<String> stores = new ArrayList<>();
            stores.add(Product.Constants.Store.WAITING.name());
            stores.add(Product.Constants.Store.FAULTY.name());
            stores.add(Product.Constants.Store.READY_FOR_DISPOSAL.name());
            stores.add(Product.Constants.Store.READY_FOR_RECYCLE.name());
            return stores;
        }

        public List<String> getLoadedCurrency(){
            ArrayList<String> currencies = new ArrayList<>();
            currencies.add(null);
            return currencies;
        }

        public Product toProduct(){
            ProductType type = new ProductType();
            type.setName(category);
            return new Product(
                    id,
                    type,
                    name,
                    description,
                    new Money(Money.doubleToBigDecimal(amount)),
                    quantity,
                    convertToDate(suppliedDate),
                    convertToDate(manufacturedDate),
                    warrantyPeriodInMonths,
                    convertToDate(purchasedDate),
                    convertToDate(expiryDate),
                    convertToDate(lastChangedDate),
                    Product.Constants.Status.valueOf(status),
                    Product.Constants.Store.valueOf(inactiveSubStatus)
            );
        }
    }
}

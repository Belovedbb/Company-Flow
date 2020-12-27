package com.company.go.domain.inventory.product;

import com.company.go.domain.inventory.Money;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    private ProductType type;

    private String name;

    private String description;

    private Money amount;

    private Long quantity;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime suppliedDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime manufacturedDate;

    private int warrantyPeriodInMonths;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime purchasedDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiryDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime lastChangedDate;

    private Constants.Status status;

    private Constants.Store store;

    static public class Constants {

        public enum Store{
            WAITING("WAITING"),
            FAULTY("FAULTY"),
            READY_FOR_DISPOSAL("READY_FOR_DISPOSAL"),
            READY_FOR_RECYCLE("READY_FOR_RECYCLE");

            private final String value;

            Store(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }

        public enum Status {
            ACTIVE("SOLD"),
            INACTIVE("NOT_SOLD");

            private final String value;

            Status(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }

}

package com.company.go.domain.billing;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Account {

    private Long id;

    @NonNull
    private String type;

    Double aggregateAmount;

    LocalDate date;

    Long typeCount;

    @NonNull
    Constants.Kind kind;

    static public class Constants {

        public enum Type{
            PRODUCT("PRODUCT"),
            PURCHASEORDER("PURCHASEORDER"),
            PERFORMANCE("PERFORMANCE"),
            AGGREGATE("AGGREGATE");

            private final String value;

            Type(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }

        public enum Kind {
            DEBIT("DEBIT"), CREDIT("CREDIT"), TOTAL("TOTAL");

            private final String value;

            Kind(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }
        }
    }
}

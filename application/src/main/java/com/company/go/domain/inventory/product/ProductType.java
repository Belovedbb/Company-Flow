package com.company.go.domain.inventory.product;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductType {
    Long id;

    String name;//car

    Constants.Category category;

    Constants.Status status;

    static public class Constants {

        public enum Category{

            CONSUMABLE("CONSUMABLE"), NON_CONSUMABLE("NON_CONSUMABLE");

            private final String value;

            Category(final String value) {
                this.value = value;
            }

            @Override
            public String toString() {
                return value;
            }

        }

         public enum Status {
            ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

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

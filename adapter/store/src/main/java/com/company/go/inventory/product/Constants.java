package com.company.go.inventory.product;

public class Constants {

    public enum ProductCategory{

        CONSUMABLE("CONSUMABLE"), NON_CONSUMABLE("NON_CONSUMABLE");

        private final String value;

        ProductCategory(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    }

    public enum ProductCategoryStatus {
        ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

        private final String value;

        ProductCategoryStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum ProductStore{
        WAITING("WAITING"),
        FAULTY("FAULTY"),
        READY_FOR_DISPOSAL("READY_FOR_DISPOSAL"),
        READY_FOR_RECYCLE("READY_FOR_RECYCLE");

        private final String value;

        ProductStore(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum ProductStatus {
        ACTIVE("SOLD"), INACTIVE("NOT_SOLD");

        private final String value;

        ProductStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

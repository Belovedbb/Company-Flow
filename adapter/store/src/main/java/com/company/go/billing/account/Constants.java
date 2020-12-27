package com.company.go.billing.account;

public class Constants {
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
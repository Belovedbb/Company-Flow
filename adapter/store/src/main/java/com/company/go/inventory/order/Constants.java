package com.company.go.inventory.order;

public class Constants {
    public enum OrderStatus {
        OPEN("OPEN"), CLOSED("CLOSED");

        private final String value;

        OrderStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

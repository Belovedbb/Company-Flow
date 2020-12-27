package com.company.go.archive.staff;

public class Constants {
    public enum StaffStatus {
        ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

        private final String value;

        StaffStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

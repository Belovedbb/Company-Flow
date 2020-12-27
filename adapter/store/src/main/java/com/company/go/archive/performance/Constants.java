package com.company.go.archive.performance;

public class Constants {
    public enum PerformanceStatus {
        POOR("POOR"), AVERAGE("AVERAGE"), EXCELLENT("EXCELLENT");

        private final String value;

        PerformanceStatus(final String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}

package com.company.go.domain.archive.performance;

import com.company.go.domain.archive.staff.Staff;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    private Long id;

    private Staff staff;

    private Double averageMonthlyPerformance;

    private Double bonusPoint;

    private LocalDate date;

    private Performance.Constants.Status status;

    static public class Constants {

        public enum Status {
            POOR("POOR"), AVERAGE("AVERAGE"), EXCELLENT("EXCELLENT");

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

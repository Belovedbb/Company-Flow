package com.company.go.domain.inventory.order;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private Long id;

    private ArrayList<OrderEntry> orderEntries;

    private String description;

    private Boolean hasVat;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime purchasedDate;

    Constants.Status status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime lastChangedDate;

    static public class Constants {

        public enum Status {
            OPEN("OPEN"), CLOSED("CLOSED");

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

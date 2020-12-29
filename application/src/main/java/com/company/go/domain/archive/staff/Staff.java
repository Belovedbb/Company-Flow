package com.company.go.domain.archive.staff;

import com.company.go.domain.global.User;
import com.company.go.domain.inventory.Money;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private Long id;

    private User user;

    private Money payment;


    private Constants.Status status;

    static public class Constants {

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

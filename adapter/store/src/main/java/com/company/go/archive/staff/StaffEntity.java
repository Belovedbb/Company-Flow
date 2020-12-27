package com.company.go.archive.staff;

import com.company.go.BaseEntity;
import com.company.go.global.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Currency;

@Entity
@Getter
@Setter
@Table(name = "staff")
public class StaffEntity extends BaseEntity{

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "type")
    UserEntity userType;

    @Column(name = "payment")
    private Double payment;

    @Column(name = "currency")
    Currency currency;

    @Column(name = "status", nullable = false)
    Constants.StaffStatus status;

}

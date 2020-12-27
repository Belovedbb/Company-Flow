package com.company.go.billing.account.performance;

import com.company.go.BaseEntity;
import com.company.go.billing.account.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "performance_account")
public class PerformanceAccountEntity extends BaseEntity {
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "aggregate_amount")
    Double aggregateAmount;

    @Column(name = "type_count")
    Long typeCount;

    @Column(name = "kind", nullable = false)
    Constants.Kind kind;

}
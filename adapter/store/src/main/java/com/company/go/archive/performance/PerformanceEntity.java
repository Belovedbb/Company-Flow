package com.company.go.archive.performance;

import com.company.go.BaseEntity;
import com.company.go.archive.staff.StaffEntity;
import com.company.go.domain.archive.performance.Performance;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "performance")
public class PerformanceEntity extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "staff")
    StaffEntity staffType;

    @Column(name = "amp", nullable = false)
    @NotNull
    private Double averageMonthlyPerformance;

    @Column(name = "bonus")
    private Double bonusPoint;

    @Column(name = "status", nullable = false)
    Constants.PerformanceStatus status;

}
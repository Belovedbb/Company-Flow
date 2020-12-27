package com.company.go.inventory.order;

import com.company.go.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "purchase_order")
public class OrderEntity extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Set<OrderEntryEntity> orderEntries;

    @Column(name = "description", nullable = false)
    @NotEmpty
    String description;

    @Column(name =  "purchased_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime purchasedDate;

    @Column(name = "has_vat", nullable = false)
    boolean hasVat;

    @Column(name = "status", nullable = false)
    Constants.OrderStatus status;

}

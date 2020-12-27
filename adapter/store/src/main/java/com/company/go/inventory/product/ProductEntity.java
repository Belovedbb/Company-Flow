package com.company.go.inventory.product;

import com.company.go.BaseEntity;
import com.company.go.inventory.order.OrderEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Currency;

@Entity
@Getter
@Setter
@Table(name = "product")
public class ProductEntity extends BaseEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "type")
    ProductTypeEntity productType;

    @Column(name = "name", nullable = false, updatable = false, unique = true)
    @NotEmpty
    String name;

    @Column(name = "description", nullable = false)
    @NotEmpty
    String description;

    @Column(name = "amount")
    double amount;

    @Column(name = "currency", nullable = false)
    Currency currency;

    @Column(name = "quantity", nullable = false)
    Long quantity;

    @Column(name = "supplied_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime suppliedDate;

    @Column(name = "manufactured_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime manufacturedDate;

    @Column(name = "warranty_period")
    int warrantyPeriodInMonths;

    @Column(name =  "purchased_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime purchasedDate;

    @Column(name = "expiry_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime expiryDate;

    @Column(name = "status", nullable = false)
    Constants.ProductStatus status;

    @Column(name = "sub_status", nullable = false)
    Constants.ProductStore subStatus;

}

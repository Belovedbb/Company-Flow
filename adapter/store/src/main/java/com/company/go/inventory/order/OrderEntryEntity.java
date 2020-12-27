package com.company.go.inventory.order;

import com.company.go.BaseEntity;
import com.company.go.inventory.product.ProductEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "purchase_order_entry")
public class OrderEntryEntity extends BaseEntity {

    @Transient
    Long productId;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "persisted", nullable = false, updatable = false, length = 1)
    private boolean isStored = true;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product")
    ProductEntity product;

    @ManyToOne(cascade = CascadeType.MERGE)
    OrderEntity order;

}

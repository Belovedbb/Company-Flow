package com.company.go.inventory.product;

import com.company.go.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "product_type")
public class ProductTypeEntity extends BaseEntity {

    @Column(name = "name", unique = true)
    String value;

    @Column(name = "category", nullable = false)
    Constants.ProductCategory category;

    @Column(name = "category_status")
    Constants.ProductCategoryStatus categoryStatus;
}

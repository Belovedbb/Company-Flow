package com.company.go.domain.inventory.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntry {
    private Long id;
    private Long productId;
    private Long quantity;
    private boolean isStored;
}

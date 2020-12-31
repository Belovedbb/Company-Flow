package com.company.go;

import com.company.go.application.port.out.archive.UpdatePerformancePort;
import com.company.go.application.port.out.archive.UpdateStaffPort;
import com.company.go.application.port.out.billing.UpdatePerformanceAccountPort;
import com.company.go.application.port.out.billing.UpdateProductAccountPort;
import com.company.go.application.port.out.billing.UpdatePurchaseOrderAccountPort;
import com.company.go.application.port.out.global.UpdateUserPort;
import com.company.go.application.port.out.inventory.UpdateProductPort;
import com.company.go.application.port.out.inventory.UpdatePurchaseOrderPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.company.go"})
public class ExcludePortConfig {
    @MockBean
    private UpdatePerformancePort port;
    @MockBean
    private UpdateStaffPort staffPort;
    @MockBean
    UpdateProductPort productPort;
    @MockBean
    UpdateUserPort userPort;
    @MockBean
    UpdatePurchaseOrderPort purchaseOrderPort;
    @MockBean
    UpdatePerformanceAccountPort performanceAccountPort;
    @MockBean
    UpdateProductAccountPort productAccountPort;
    @MockBean
    UpdatePurchaseOrderAccountPort purchaseOrderAccountPort;

}
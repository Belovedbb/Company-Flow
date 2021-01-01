package com.company.go.report;

import com.company.go.ControllerUtilities;
import com.company.go.application.port.in.inventory.ProductUseCase;
import com.company.go.application.port.in.inventory.PurchaseOrderUseCase;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequestMapping("report/inventory")
public class InventoryReportController {

    private final ProductUseCase productUseCase;

    private final PurchaseOrderUseCase purchaseOrderUseCase;

    InventoryReportController(ProductUseCase productUseCase, PurchaseOrderUseCase purchaseOrderUseCase){
        this.productUseCase = productUseCase;
        this.purchaseOrderUseCase = purchaseOrderUseCase;
    }

    @GetMapping(produces = "application/pdf")
    public void getInventoryReport(HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of(
        "products", productUseCase.getAllProducts(),
        "purchaseOrders", purchaseOrderUseCase.getAllPurchaseOrders()
        );
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/inventory/inventory");
        ControllerUtilities.downloadBytes(bytes, response, "Inventory Report.pdf");
    }

    @GetMapping(value = "/product", produces = "application/pdf")
    public void getProductReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("products", productUseCase.getAllProducts());
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/inventory/product");
        ControllerUtilities.downloadBytes(bytes, response, "PurchaseOrders.pdf");
    }

    @GetMapping(value = "/purchase/order", produces = "application/pdf")
    public void getPurchaseOrderReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("purchaseOrders", purchaseOrderUseCase.getAllPurchaseOrders());
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/inventory/purchase-order");
        ControllerUtilities.downloadBytes(bytes, response, "PurchaseOrders.pdf");
    }
}

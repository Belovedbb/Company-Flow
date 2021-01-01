package com.company.go.report;

import com.company.go.ControllerUtilities;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequestMapping("report/billing")
public class BillingReportController {

    private final AccountUseCase accountUseCase;

    BillingReportController(AccountUseCase accountUseCase){
        this.accountUseCase = accountUseCase;
    }

    @GetMapping(value = "/account", produces = "application/pdf")
    public void getBillingReport(HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of(
                "accounts", accountUseCase.getAllAccounts()
        );
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/billing/account");
        ControllerUtilities.downloadBytes(bytes, response, "Billing Report.pdf");
    }

    @GetMapping(value = "/account/product", produces = "application/pdf")
    public void getProductAccountReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("productAccounts", accountUseCase.getAllAccounts("product"));
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/billing/account/product");
        ControllerUtilities.downloadBytes(bytes, response, "Product Accounts.pdf");
    }

    @GetMapping(value = "/account/performance", produces = "application/pdf")
    public void getPerformanceAccountReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("performanceAccounts", accountUseCase.getAllAccounts("performance"));
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/billing/account/performance");
        ControllerUtilities.downloadBytes(bytes, response, "Performance Accounts.pdf");
    }

    @GetMapping(value = "/account/purchaseorder", produces = "application/pdf")
    public void getPurchaseOrderReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("purchaseOrderAccounts", accountUseCase.getAllAccounts("purchaseorder"));
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/billing/account/purchaseorder");
        ControllerUtilities.downloadBytes(bytes, response, "Purchase Order Accounts.pdf");
    }
}

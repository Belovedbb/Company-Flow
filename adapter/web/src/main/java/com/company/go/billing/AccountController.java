package com.company.go.billing;

import com.company.go.ControllerUtilities;
import com.company.go.Utilities;
import com.company.go.application.port.in.billing.AccountUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.SQLException;

@Controller
@RequestMapping("/billing/account")
public class AccountController {

    private AccountUseCase accountUseCase;

    private IndexUseCase indexer;

    @Autowired
    public AccountController(IndexUseCase indexer, AccountUseCase accountUseCase) {
        this.indexer = indexer;
        this.accountUseCase = accountUseCase;
    }

    @GetMapping("/listing")
    public String setUpAccountListing(Model model) throws IOException, SQLException {
        AccountUseCase.AccountViewModel account = new AccountUseCase.AggregateAccountViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accountUseCase.getAllAccounts());
        return "billing/account/listing/account-listing";
    }

    @GetMapping("/listing/{type}")
    public String setUpAccountTypeListing(@PathVariable String type, Model model) throws IOException, SQLException {
        AccountUseCase.AccountViewModel account = new AccountUseCase.AggregateAccountViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("account", account);
        model.addAttribute("accounts", accountUseCase.getAllAccounts(type));
        return "billing/account/listing/account-listing-"+type;
    }

    @PostMapping("/listing/filter/{type}")
    public String getFilteredAccountListing(@PathVariable String type, Model model, AccountUseCase.AccountViewModel account){
        model.addAttribute("accounts", accountUseCase.getFilteredAccounts(account, type));
        return "billing/account/account-listing :: result";
    }

    @GetMapping("/view/{id}/{type}")
    public String viewAccount(@PathVariable Long id, @PathVariable String type, Model model) throws IOException, SQLException {
        AccountUseCase.AccountViewModel account = accountUseCase.viewAccount(id, type);
        model.addAttribute("state", Utilities.State.VIEW.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("loadedType", new AccountUseCase.AggregateAccountViewModel().getLoadedType());
        model.addAttribute("loadedKind", new AccountUseCase.AggregateAccountViewModel().getLoadedKind());
        model.addAttribute("account", account);
        return "billing/account/form/account-form-"+type;
    }

    @GetMapping("/report")
    public String setUpAccountReport(Model model)  {
        return "redirect:/report/billing/account";
    }

    @GetMapping("/report/{type}")
    public String setUpAccountReportType(@PathVariable String type, Model model)  {
        return "redirect:/report/billing/account/" + type;
    }

}

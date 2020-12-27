package com.company.go.global;

import com.company.go.ControllerUtilities;
import com.company.go.application.port.in.global.DashBoardUseCase;
import com.company.go.application.port.in.global.IndexUseCase;
import com.company.go.application.port.in.global.RegisterUserUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
public class GlobalController {

    private RegisterUserUseCase userUseCase;
    private IndexUseCase indexer;
    private DashBoardUseCase dashBoardUseCase;

    public GlobalController(RegisterUserUseCase userUseCase, DashBoardUseCase dashBoardUseCase, IndexUseCase indexer) {
        this.userUseCase = userUseCase;
        this.indexer = indexer;
        this.dashBoardUseCase = dashBoardUseCase;
    }

    @GetMapping("/login")
    public String setUpLogin(){
        return "global/login";
    }

    @GetMapping("/")
    public String index(Model model) throws IOException, SQLException {
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("summaries", dashBoardUseCase.getAllSummaries());
        model.addAttribute("summaryChartData", dashBoardUseCase.summaryChartData((List<DashBoardUseCase.SummaryWidgetViewModel>) model.getAttribute("summaries")));
        model.addAttribute("detailedProductData", dashBoardUseCase.detailedProductChartData());
        model.addAttribute("detailedOrderData", dashBoardUseCase.detailedOrderChartData());
        return "index";
    }

    @GetMapping("/register")
    public String setUpLoginRegister(Model model) {
        RegisterUserUseCase.RegisterUserModel user = new RegisterUserUseCase.RegisterUserModel();
        model.addAttribute("user", user);
        return "global/sign-up";
    }

    @PostMapping("/register")
    public String saveLoginRegister(@ModelAttribute("user") @Valid RegisterUserUseCase.RegisterUserModel user,
                                    BindingResult result){

        if(result.hasErrors()){
            return "global/sign-up";
        }
        userUseCase.storeUser(user);
        return "global/login";
    }
}

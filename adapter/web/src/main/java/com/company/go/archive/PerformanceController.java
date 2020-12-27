package com.company.go.archive;

import com.company.go.ControllerUtilities;
import com.company.go.Utilities;
import com.company.go.application.port.in.archive.PerformanceUseCase;
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
@RequestMapping("/archive/performance")
public class PerformanceController {

    private PerformanceUseCase performanceUseCase;

    private IndexUseCase indexer;


    @Autowired
    public PerformanceController(IndexUseCase indexer, PerformanceUseCase performanceUseCase) {
        this.indexer = indexer;
        this.performanceUseCase = performanceUseCase;
    }

    @GetMapping("/listing")
    public String setUpPerformanceListing(Model model) throws IOException, SQLException {
        PerformanceUseCase.PerformanceViewModel performance = new PerformanceUseCase.PerformanceViewModel();
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("performance", performance);
        model.addAttribute("performances", performanceUseCase.getAllPerformances());
        return "archive/performance/performance-listing";
    }

    @PostMapping("/listing/filter")
    public String getFilteredPerformanceListing(Model model, PerformanceUseCase.PerformanceViewModel performance){
        model.addAttribute("performances", performanceUseCase.getFilteredPerformances(performance));
        return "archive/performance/performance-listing :: result";
    }

    @GetMapping("/view/{id}")
    public String viewPerformance(@PathVariable Long id, Model model) throws IOException, SQLException {
        PerformanceUseCase.PerformanceViewModel performance = performanceUseCase.viewPerformance(id);
        model.addAttribute("state", Utilities.State.VIEW.name());
        ControllerUtilities.storeIndexDetailsInModel(model, indexer);
        model.addAttribute("performance", performance);
        return "archive/performance/performance-form";
    }



    @GetMapping("/report")
    public String setUpPerformanceReport(Model model)  {
        return "redirect:/report/archive/performance";
    }

}

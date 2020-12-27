package com.company.go.report;

import com.company.go.ControllerUtilities;
import com.company.go.application.port.in.archive.StaffUseCase;
import com.company.go.application.port.in.archive.PerformanceUseCase;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequestMapping("report/archive")
public class ArchiveReportController {

    @Autowired
    private StaffUseCase staffUseCase;

    @Autowired
    private PerformanceUseCase performanceUseCase;

    @GetMapping(produces = "application/pdf")
    public void getArchiveReport(HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of(
                "staffs", staffUseCase.getAllStaffs(),
                "performances", performanceUseCase.getAllPerformances()
        );
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/archive/archive");
        ControllerUtilities.downloadBytes(bytes, response, "Archive Report.pdf");
    }

    @GetMapping(value = "/staff", produces = "application/pdf")
    public void getStaffReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("staffs", staffUseCase.getAllStaffs());
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/archive/staff");
        ControllerUtilities.downloadBytes(bytes, response, "Staffs.pdf");
    }

    @GetMapping(value = "/performance", produces = "application/pdf")
    public void getPerformanceReport( HttpServletResponse response) throws IOException, DocumentException {
        Map<String, Object> objects = Map.of("performances", performanceUseCase.getAllPerformances());
        byte[] bytes = ControllerUtilities.getReportDownload(objects, "report/archive/performance");
        ControllerUtilities.downloadBytes(bytes, response, "Performances.pdf");
    }
}

package com.company.go.application.port.in.global;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public interface DashBoardUseCase {

    List<SummaryWidgetViewModel> getAllSummaries();

    Map<String, Long>[] summaryChartData(List<SummaryWidgetViewModel> summaries);

    Map<String, Long>[] detailedProductChartData();

    Map<String, Long>[] detailedOrderChartData();

    @Setter
    @Getter
    class SummaryWidgetViewModel{
        private String name;
        private String periodText;
        private Long count;
    }
}

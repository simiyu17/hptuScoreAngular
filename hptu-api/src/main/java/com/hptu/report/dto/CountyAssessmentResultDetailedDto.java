package com.hptu.report.dto;

import java.util.List;

public record CountyAssessmentResultDetailedDto(List<CountySummaryDto> summary,
                                                List<ReportByPillar.PillarDataPointModel> summaryDataPoints,
                                                String summaryTitle
                                                ) {
}

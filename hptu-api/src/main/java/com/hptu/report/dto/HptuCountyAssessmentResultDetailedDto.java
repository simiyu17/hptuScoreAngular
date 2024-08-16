package com.hptu.report.dto;

import java.util.List;

public record HptuCountyAssessmentResultDetailedDto(List<HptuCountySummaryDto> summary,
                                                    List<ReportByPillar.PillarDataPointModel> summaryDataPoints,
                                                    String summaryTitle
                                                ) {
}

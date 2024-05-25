package com.hptu.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record ReportByPillar(String countyCode, String quarter, String year) implements Serializable {

    public record PillarDataPointModel(String x, BigDecimal y) {
    }
}

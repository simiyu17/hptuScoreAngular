package com.hptu.score.dto;

import java.math.BigDecimal;

public class ReportByPillar {

    private String countyCode;

    private String quarter;

    private String year;

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public static class PillarDataPointModel {
        String x;
        BigDecimal y;

        public PillarDataPointModel(String x, BigDecimal y) {
            this.x = x;
            this.y = y;
        }

        public String getX() {
            return x;
        }
        public void setX(String x) {
            this.x = x;
        }
        public BigDecimal getY() {
            return y;
        }
        public void setY(BigDecimal y) {
            this.y = y;
        }
    }
}

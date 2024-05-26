package com.hptu.hptuassessment.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface CountyAssessmentRepository extends JpaRepository<CountyAssessment, Long> {

    /*
    @Query("SELECT c.year AS yearComment, COUNT(c.year) AS totalComment "
            + "FROM Comment AS c GROUP BY c.year ORDER BY c.year DESC")
    List<Map<String, Object>> getSummaryByYear(String year);

    List<Map<String, Object>> getSummaryByYearAndQuarter(String year, String quarter);

    List<Map<String, Object>> getSummaryByYearAndCountyCode(String year, String countyCode);

    List<Map<String, Object>> getSummaryByYearAndQuarterAndCountyCode(String year, String quarter, String countyCode);
*/

}

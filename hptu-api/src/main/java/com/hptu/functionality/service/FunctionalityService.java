package com.hptu.functionality.service;

import com.hptu.functionality.dto.FunctionalityDto;

import java.util.List;

public interface FunctionalityService {

    void createFunctionality(FunctionalityDto functionalityDto);

    void updateFunctionality(Long functionalityId, FunctionalityDto functionalityDto);

    FunctionalityDto findFunctionalityById(Long functionalityId);

    List<FunctionalityDto> getAllFunctionalities();

    void deleteFunctionality(Long functionalityId);

}

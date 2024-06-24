package com.hptu.functionality.service;


import com.hptu.functionality.domain.Functionality;
import com.hptu.functionality.domain.FunctionalityRepository;
import com.hptu.functionality.dto.FunctionalityDto;
import com.hptu.functionality.exception.FunctionalityNotFoundException;
import com.hptu.functionality.mapper.FunctionalityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionalityServiceImpl implements FunctionalityService {

    private final FunctionalityRepository functionalityRepository;
    private final FunctionalityMapper functionalityMapper;
    private static final String NO_FUNCTIONALITY_FOUND = "No functionality found with Id";


    @Transactional
    @Override
    public void createFunctionality(FunctionalityDto functionalityDto) {
        try {
            this.functionalityRepository.save(Functionality.createFunctionality(functionalityDto));
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }

    @Transactional
    @Override
    public void updateFunctionality(Long functionalityId, FunctionalityDto functionalityDto) {
        var currentfunctionality = this.functionalityRepository.findById(functionalityId)
                .orElseThrow(() -> new FunctionalityNotFoundException(NO_FUNCTIONALITY_FOUND));
        try {
            currentfunctionality.updateFunctionality(functionalityDto);
            this.functionalityRepository.save(currentfunctionality);
        }catch (Exception e){
            throw new IllegalArgumentException(e);
        }

    }

    @Override
    public FunctionalityDto findFunctionalityById(Long userId) {
        return this.functionalityRepository.findById(userId)
                .map(functionalityMapper::toDto)
                .orElseThrow(() -> new FunctionalityNotFoundException(NO_FUNCTIONALITY_FOUND));
    }

    @Override
    public List<FunctionalityDto> getAllFunctionalities() {
        return functionalityMapper.toDto(this.functionalityRepository.findAll());
    }

    @Override
    public void deleteFunctionality(Long functionalityId) {
        var currentfunctionality = this.functionalityRepository.findById(functionalityId)
                .orElseThrow(() -> new FunctionalityNotFoundException(NO_FUNCTIONALITY_FOUND));
        this.functionalityRepository.deleteById(currentfunctionality.getId());

    }

}

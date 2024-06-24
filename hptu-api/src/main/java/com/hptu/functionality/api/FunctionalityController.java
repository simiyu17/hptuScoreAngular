package com.hptu.functionality.api;

import com.hptu.functionality.dto.FunctionalityDto;
import com.hptu.functionality.service.FunctionalityService;
import com.hptu.shared.dto.ApiResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/hptu-functionalities")
@Validated
public class FunctionalityController {

    private final FunctionalityService functionalityService;

    @GetMapping
    public ResponseEntity<List<FunctionalityDto>> getFunctionalities(){
        return new ResponseEntity<>(this.functionalityService.getAllFunctionalities(), HttpStatus.OK);
    }

    @GetMapping("{functionality-id}")
    public ResponseEntity<FunctionalityDto> getById(@PathVariable("functionality-id") Long functionalityId){
        return new ResponseEntity<>(this.functionalityService.findFunctionalityById(functionalityId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDto> createFunctionality(@Valid @RequestBody FunctionalityDto functionalityDto) {
        try {
            this.functionalityService.createFunctionality(functionalityDto);
            return new ResponseEntity<>(new ApiResponseDto(true, "Functionality was submitted!!"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDto(true, "An Error Occurred: "+e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
}

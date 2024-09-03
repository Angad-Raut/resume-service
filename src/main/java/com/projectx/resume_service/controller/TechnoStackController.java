package com.projectx.resume_service.controller;

import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;
import com.projectx.resume_service.services.TechnoStackService;
import com.projectx.resume_service.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/technoStackDetails")
public class TechnoStackController {

    @Autowired
    private TechnoStackService technoStackService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/insertUpdateEmployeeSkills")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdateEmployeeSkills(
            @RequestBody @Valid TechnoStackDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = technoStackService.insertUpdateEmployeeSkills(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null), HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<TechnoStackDto>> getById(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            TechnoStackDto data = technoStackService.getById(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getEmployeeSkills")
    public ResponseEntity<ResponseDto<SkillsPageResponseDto>> getEmployeeSkills(
            @RequestBody @Valid EntityIdWithPageRequestDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            SkillsPageResponseDto data = technoStackService.getEmployeeSkills(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException  e) {
            return errorHandler.handleError(e);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getStackItems")
    public ResponseEntity<ResponseDto<List<ViewStackItemDto>>> getStackItems(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<ViewStackItemDto> data = technoStackService.getStackItems(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }
}

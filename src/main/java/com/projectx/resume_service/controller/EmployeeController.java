package com.projectx.resume_service.controller;

import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;
import com.projectx.resume_service.services.EmployeeService;
import com.projectx.resume_service.utils.ErrorHandlerComponent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/employeeDetails")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/addUpdate")
    public ResponseEntity<ResponseDto<Boolean>> addUpdate(
            @RequestBody @Valid EmployeeDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = employeeService.createEmployee(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.CREATED);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getById")
    public ResponseEntity<ResponseDto<EmployeeDto>> getById(
            @RequestBody @Valid EntityIdDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            EmployeeDto data = employeeService.getById(dto);
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

    @PostMapping(value = "/updateStatus")
    public ResponseEntity<ResponseDto<Boolean>> updateStatus(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = employeeService.updateStatus(dto);
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

    @GetMapping(value = "/getEmployeeDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getEmployeeDropDown() {
        try {
            List<EntityIdAndValueDto> data = employeeService.getEmployeeDropDown();
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getEmployeeCompanyDropDown")
    public ResponseEntity<ResponseDto<List<EntityIdAndValueDto>>> getEmployeeCompanyDropDown(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<EntityIdAndValueDto> data = employeeService.getEmployeeCompanyDropDown(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/getAllEmployees")
    public ResponseEntity<ResponseDto<EmployeePageResponseDto>> getAllEmployees(
            @RequestBody @Valid PageRequestDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            EmployeePageResponseDto data = employeeService.getAllEmployees(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/insertUpdateEmployeeCompany")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdateEmployeeCompany(
            @RequestBody @Valid EmployeeCompanyIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = employeeService.insertUpdateEmployeeCompany(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (ResourceNotFoundException | AlreadyExistException e) {
            return errorHandler.handleError(e);
        } catch (Exception e){
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/getEmployeeCompanies")
    public ResponseEntity<ResponseDto<List<ViewEmployeeCompanyDto>>> getEmployeeCompanies(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<ViewEmployeeCompanyDto> data = employeeService.getEmployeeCompanies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null, null), HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            return errorHandler.handleError(e);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseDto<>(
                    null,e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

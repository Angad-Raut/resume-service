package com.projectx.resume_service.controller;

import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;
import com.projectx.resume_service.services.ProjectService;
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
@RequestMapping(value = "/projectDetails")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ErrorHandlerComponent errorHandler;

    @PostMapping(value = "/insertUpdate")
    public ResponseEntity<ResponseDto<Boolean>> insertUpdate(
            @RequestBody @Valid ProjectDto dto, BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = projectService.insertUpdate(dto);
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
    public ResponseEntity<ResponseDto<ProjectDto>> getById(
            @RequestBody @Valid EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ProjectDto data = projectService.getById(dto);
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

    @PostMapping(value = "/getEmployeeProjects")
    public ResponseEntity<ResponseDto<ProjectPageResponseDto>> getEmployeeProjects(
            @RequestBody @Valid EntityIdWithPageRequestDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            ProjectPageResponseDto data = projectService.getEmployeeProjects(dto);
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

    @PostMapping(value = "/getProjectTechnologies")
    public ResponseEntity<ResponseDto<List<TechDto>>> getProjectTechnologies(
            @Valid @RequestBody EntityIdDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            List<TechDto> data = projectService.getProjectTechnologies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (Exception e) {
            return errorHandler.handleError(e);
        }
    }

    @PostMapping(value = "/addUpdateProjectTechnologies")
    public ResponseEntity<ResponseDto<Boolean>> addUpdateProjectTechnologies(
            @Valid @RequestBody TechnologyListDto dto,BindingResult result) {
        if (result.hasErrors()){
            return errorHandler.handleValidationErrors(result);
        }
        try {
            Boolean data = projectService.addUpdateProjectTechnologies(dto);
            return new ResponseEntity<>(new ResponseDto<>(data,
                    null,null),HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new ResponseDto<>(null,
                    e.getMessage(),null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

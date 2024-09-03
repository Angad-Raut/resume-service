package com.projectx.resume_service.services;


import com.projectx.resume_service.entity.EmployeeDetails;
import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;

import java.util.List;

public interface EmployeeService {
    Boolean createEmployee(EmployeeDto dto)throws ResourceNotFoundException,
            AlreadyExistException;
    EmployeeDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    EmployeePageResponseDto getAllEmployees(PageRequestDto dto);
    EmployeeDetails getEmployeeDetails(Long resumeId)throws ResourceNotFoundException;
    Boolean updateStatus(EntityIdDto dto)throws ResourceNotFoundException;
    Boolean insertUpdateEmployeeCompany(EmployeeCompanyIdDto dto)throws ResourceNotFoundException;
    List<ViewEmployeeCompanyDto> getEmployeeCompanies(EntityIdDto dto)throws ResourceNotFoundException;
    List<EntityIdAndValueDto> getEmployeeDropDown();
    List<EntityIdAndValueDto> getEmployeeCompanyDropDown(EntityIdDto dto);

}

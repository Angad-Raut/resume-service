package com.projectx.resume_service.services;

import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.EducationDto;
import com.projectx.resume_service.payloads.EducationPageResponseDto;
import com.projectx.resume_service.payloads.EntityIdDto;
import com.projectx.resume_service.payloads.EntityIdWithPageRequestDto;

import java.text.ParseException;

public interface EducationService {
    Boolean insertUpdate(EducationDto dto) throws ResourceNotFoundException, AlreadyExistException, ParseException;
    EducationDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    EducationPageResponseDto getEmployeeEducations(EntityIdWithPageRequestDto dto)throws ResourceNotFoundException;
}

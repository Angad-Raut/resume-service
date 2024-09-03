package com.projectx.resume_service.services;

import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;

import java.util.List;

public interface ProjectService {
    Boolean insertUpdate(ProjectDto dto)throws ResourceNotFoundException, AlreadyExistException;
    ProjectDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    ProjectPageResponseDto getEmployeeProjects(EntityIdWithPageRequestDto dto);
    List<TechDto> getProjectTechnologies(EntityIdDto dto);
    Boolean addUpdateProjectTechnologies(TechnologyListDto dto);
}

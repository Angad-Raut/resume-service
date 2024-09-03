package com.projectx.resume_service.services;


import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;

import java.util.List;

public interface TechnoStackService {
    Boolean insertUpdateEmployeeSkills(TechnoStackDto dto)
            throws ResourceNotFoundException;
    TechnoStackDto getById(EntityIdDto dto)throws ResourceNotFoundException;
    SkillsPageResponseDto getEmployeeSkills(EntityIdWithPageRequestDto dto)
            throws ResourceNotFoundException;
    List<ViewStackItemDto> getStackItems(EntityIdDto dto);
}

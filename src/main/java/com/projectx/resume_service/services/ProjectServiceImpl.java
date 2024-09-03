package com.projectx.resume_service.services;

import com.projectx.resume_service.config.CompanyClient;
import com.projectx.resume_service.entity.EmployeeDetails;
import com.projectx.resume_service.entity.ProjectDetails;
import com.projectx.resume_service.exceptions.AlreadyExistException;
import com.projectx.resume_service.exceptions.ResourceNotFoundException;
import com.projectx.resume_service.payloads.*;
import com.projectx.resume_service.repository.ProjectRepository;
import com.projectx.resume_service.utils.ResumeUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProjectServiceImpl implements ProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyClient companyClient;

    @Override
    public Boolean insertUpdate(ProjectDto dto)
            throws ResourceNotFoundException, AlreadyExistException {
        ProjectDetails projectDetails = null;
        EmployeeDetails employeeDetails = employeeService.getEmployeeDetails(dto.getEmployeeId());
        ResponseEntity<ResponseDto<CompanyInfoDto>> response = companyClient.getCompanyInfoById(new EntityIdDto(dto.getCompanyId()));
        if (employeeDetails==null) {
            throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
        }
        if (response.getBody()!=null && response.getBody().getResult()==null
                && response.getBody().getErrorMessage()!=null){
            throw new ResourceNotFoundException(response.getBody().getErrorMessage());
        }
        CompanyInfoDto companyInfoDto = response.getBody().getResult();
        if (dto.getId()==null) {
              projectDetails = ProjectDetails.builder()
                      .companyId(companyInfoDto.getCompanyId())
                      .projectName(dto.getProjectName())
                      .projectDescription(dto.getProjectDescription())
                      .responsibility(dto.getResponsibility())
                      .jobTitle(dto.getJobTitle())
                      .teamSize(dto.getTeamSize())
                      .projectDomain(dto.getProjectDomain())
                      .technologies(dto.getTechnologies())
                      .employeeDetails(employeeDetails)
                      .build();
        } else {
              projectDetails = projectRepository.getById(dto.getId());
              if (projectDetails==null) {
                  throw new ResourceNotFoundException(ResumeUtils.PROJECT_DETAILS_NOT_FOUND);
              }
              if (!dto.getProjectName().equals(projectDetails.getProjectName())) {
                  projectDetails.setProjectName(dto.getProjectName());
              }
              if (!dto.getProjectDescription().equals(projectDetails.getProjectDescription())) {
                  projectDetails.setProjectDescription(dto.getProjectDescription());
              }
              if (!dto.getResponsibility().equals(projectDetails.getResponsibility())) {
                  projectDetails.setResponsibility(dto.getResponsibility());
              }
              if (!dto.getCompanyId().equals(projectDetails.getCompanyId())) {
                  projectDetails.setCompanyId(companyInfoDto.getCompanyId());
              }
              if (!dto.getJobTitle().equals(projectDetails.getJobTitle())) {
                  projectDetails.setJobTitle(dto.getJobTitle());
              }
              if (!dto.getTeamSize().equals(projectDetails.getTeamSize())) {
                  projectDetails.setTeamSize(dto.getTeamSize());
              }
              if (!dto.getEmployeeId().equals(projectDetails.getEmployeeDetails().getId())) {
                  projectDetails.setEmployeeDetails(employeeDetails);
              }
              if (!dto.getProjectDomain().equals(projectDetails.getProjectDomain())) {
                  projectDetails.setProjectDomain(dto.getProjectDomain());
              }
              projectDetails.setUpdatedTime(new Date());
        }
        try {
            return projectRepository.save(projectDetails)!=null?true:false;
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProjectDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
             ProjectDetails details = projectRepository.getById(dto.getEntityId());
             if (details==null) {
                 throw new ResourceNotFoundException(ResumeUtils.PROJECT_DETAILS_NOT_FOUND);
             }
             return ProjectDto.builder()
                     .id(details.getId())
                     .employeeId(details.getEmployeeDetails().getId())
                     .companyId(details.getCompanyId())
                     .projectDescription(details.getProjectDescription())
                     .jobTitle(details.getJobTitle())
                     .projectName(details.getProjectName())
                     .projectDomain(details.getProjectDomain())
                     .teamSize(details.getTeamSize())
                     .technologies(details.getTechnologies())
                     .responsibility(details.getResponsibility())
                     .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public ProjectPageResponseDto getEmployeeProjects(EntityIdWithPageRequestDto dto) {
        String sortParameter ="";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("employeeName")) {
            sortParameter = "employee_id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("companyName")) {
            sortParameter = "company_id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("projectName")) {
            sortParameter = "project_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("teamSize")) {
            sortParameter = "team_size";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("jobTitle")) {
            sortParameter = "job_title";
        } else {
            sortParameter = "updated_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<ProjectDetails> projectDetails = null;
        if (dto.getEntityId()!=null) {
            projectDetails = projectRepository.getAllProjectsByEmployeeId(dto.getEntityId(),pageable);
        } else {
            projectDetails = projectRepository.getAllProjects(pageable);
        }
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<ProjectDetails> listOfProjects = projectDetails.getContent();
        List<ViewProjectDto> projectList = !listOfProjects.isEmpty()?listOfProjects.stream()
                .map(data -> ViewProjectDto.builder()
                        .srNo(index.incrementAndGet())
                        .id(data.getId())
                        .employeeId(data.getId()!=null?data.getId():null)
                        .employeeName(data.getEmployeeDetails()!=null?data.getEmployeeDetails().getEmployeeName(): ResumeUtils.DASH)
                        .projectName(data.getProjectName()!=null?data.getProjectName():ResumeUtils.DASH)
                        .companyName(data.getCompanyId()!=null?setCompanyName(data.getCompanyId()):ResumeUtils.DASH)
                        .teamSize(data.getTeamSize()!=null?data.getTeamSize():0)
                        .jobTitle(data.getJobTitle()!=null?data.getJobTitle():ResumeUtils.DASH)
                        .build()).toList()
                :new ArrayList<>();
        return !projectList.isEmpty()? ProjectPageResponseDto.builder()
                .pageNo(projectDetails.getNumber())
                .pageSize(projectDetails.getSize())
                .totalPages(projectDetails.getTotalPages())
                .totalElements(projectDetails.getTotalElements())
                .content(projectList)
                .build():new ProjectPageResponseDto();
    }

    @Override
    public List<TechDto> getProjectTechnologies(EntityIdDto dto) {
        List<String> fetchList = projectRepository.getProjectTechnologies(dto.getEntityId());
        AtomicInteger index = new AtomicInteger(0);
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> TechDto.builder()
                        .srNo(index.incrementAndGet())
                        .techName(data)
                        .build())
                .toList():new ArrayList<>();
    }

    @Transactional
    @Override
    public Boolean addUpdateProjectTechnologies(TechnologyListDto dto) {
        try {
            ProjectDetails projectDetails = projectRepository.getById(dto.getProjectId());
            if (projectDetails!=null) {
                Boolean isAdded=false;
                List<String> technologyList = projectDetails.getTechnologies();
                if (!technologyList.contains(dto.getTechnology())) {
                    technologyList.add(dto.getTechnology());
                    isAdded = true;
                }
                if (isAdded){
                    projectDetails.setTechnologies(technologyList);
                    isAdded=true;
                }
                return isAdded && projectRepository.save(projectDetails)!=null?true:false;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    private String setCompanyName(Long companyId) {
        ResponseEntity<ResponseDto<String>> response = companyClient.getCompanyName(new EntityIdDto(companyId));
        if (response.getBody()!=null && response.getBody().getResult()!=null && response.getBody().getErrorMessage()==null) {
            return response.getBody().getResult();
        } else {
            return ResumeUtils.DASH;
        }
    }
}

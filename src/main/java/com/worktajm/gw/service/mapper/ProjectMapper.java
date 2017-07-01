package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectMapper extends EntityMapper <ProjectDTO, Project> {
    
    @Mapping(target = "timeEntries", ignore = true)
    Project toEntity(ProjectDTO projectDTO); 
    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}

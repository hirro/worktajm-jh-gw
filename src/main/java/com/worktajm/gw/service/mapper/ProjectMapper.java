package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.ProjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, })
public interface ProjectMapper extends EntityMapper <ProjectDTO, Project> {

    @Mapping(source = "belongsTo.id", target = "belongsToId")
    @Mapping(source = "belongsTo.name", target = "belongsToName")
    ProjectDTO toDto(Project project); 

    @Mapping(source = "belongsToId", target = "belongsTo")
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

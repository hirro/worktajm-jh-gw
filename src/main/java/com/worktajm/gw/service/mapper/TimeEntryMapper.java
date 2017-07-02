package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.TimeEntryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TimeEntry and its DTO TimeEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class, UserMapper.class, })
public interface TimeEntryMapper extends EntityMapper <TimeEntryDTO, TimeEntry> {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")

    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.email", target = "createdByEmail")
    TimeEntryDTO toDto(TimeEntry timeEntry); 

    @Mapping(source = "projectId", target = "project")

    @Mapping(source = "createdById", target = "createdBy")
    TimeEntry toEntity(TimeEntryDTO timeEntryDTO); 
    default TimeEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(id);
        return timeEntry;
    }
}

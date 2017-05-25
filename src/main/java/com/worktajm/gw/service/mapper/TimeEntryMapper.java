package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.TimeEntryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TimeEntry and its DTO TimeEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {WorkerMapper.class, ProjectMapper.class, })
public interface TimeEntryMapper extends EntityMapper <TimeEntryDTO, TimeEntry> {
    @Mapping(source = "worker.id", target = "workerId")
    @Mapping(source = "worker.email", target = "workerEmail")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "project.name", target = "projectName")
    TimeEntryDTO toDto(TimeEntry timeEntry); 
    @Mapping(source = "workerId", target = "worker")
    @Mapping(source = "projectId", target = "project")
    TimeEntry toEntity(TimeEntryDTO timeEntryDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default TimeEntry fromId(Long id) {
        if (id == null) {
            return null;
        }
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(id);
        return timeEntry;
    }
}

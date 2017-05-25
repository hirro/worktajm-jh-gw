package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.WorkerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Worker and its DTO WorkerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkerMapper extends EntityMapper <WorkerDTO, Worker> {
    
    @Mapping(target = "timeEntries", ignore = true)
    Worker toEntity(WorkerDTO workerDTO); 
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Worker fromId(Long id) {
        if (id == null) {
            return null;
        }
        Worker worker = new Worker();
        worker.setId(id);
        return worker;
    }
}

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
    default Worker fromId(Long id) {
        if (id == null) {
            return null;
        }
        Worker worker = new Worker();
        worker.setId(id);
        return worker;
    }
}

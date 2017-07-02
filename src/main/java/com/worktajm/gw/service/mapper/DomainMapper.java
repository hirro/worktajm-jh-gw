package com.worktajm.gw.service.mapper;

import com.worktajm.gw.domain.*;
import com.worktajm.gw.service.dto.DomainDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Domain and its DTO DomainDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, })
public interface DomainMapper extends EntityMapper <DomainDTO, Domain> {
    
    @Mapping(target = "customers", ignore = true)
    Domain toEntity(DomainDTO domainDTO); 
    default Domain fromId(Long id) {
        if (id == null) {
            return null;
        }
        Domain domain = new Domain();
        domain.setId(id);
        return domain;
    }
}

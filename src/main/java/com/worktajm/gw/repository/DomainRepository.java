package com.worktajm.gw.repository;

import com.worktajm.gw.domain.Domain;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Domain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomainRepository extends JpaRepository<Domain,Long> {
    
    @Query("select distinct domain from Domain domain left join fetch domain.members")
    List<Domain> findAllWithEagerRelationships();

    @Query("select domain from Domain domain left join fetch domain.members where domain.id =:id")
    Domain findOneWithEagerRelationships(@Param("id") Long id);
    
}

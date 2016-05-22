package com.arnellconsulting.worktajm.gw.repository;

import com.arnellconsulting.worktajm.gw.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

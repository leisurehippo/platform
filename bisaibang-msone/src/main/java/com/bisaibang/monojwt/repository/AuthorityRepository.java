package com.bisaibang.monojwt.repository;

import com.bisaibang.monojwt.domain.generate.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

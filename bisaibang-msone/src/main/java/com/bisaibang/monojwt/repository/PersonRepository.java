package com.bisaibang.monojwt.repository;

import com.bisaibang.monojwt.domain.people.Person;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Person entity.
 */
@SuppressWarnings("unused")
public interface PersonRepository extends JpaRepository<Person,Long> {

}

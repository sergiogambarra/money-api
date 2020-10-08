package com.gambarra.money.api.repository;

import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.person.PersonRepositoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryQuery {

}

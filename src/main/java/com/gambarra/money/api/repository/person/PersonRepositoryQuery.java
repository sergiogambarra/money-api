package com.gambarra.money.api.repository.person;

import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.filter.PersonFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonRepositoryQuery {

    public Page<Person> filter(PersonFilter personFilter, Pageable pageable);
}

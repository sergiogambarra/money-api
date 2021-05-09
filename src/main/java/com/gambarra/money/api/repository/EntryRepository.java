package com.gambarra.money.api.repository;

import com.gambarra.money.api.repository.entry.EntryRepositoryQuery;
import com.gambarra.money.api.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EntryRepository extends JpaRepository<Entry, Long>, EntryRepositoryQuery {
    List<Entry> findByDueDateLessThanEqualAndPayDateIsNull(LocalDate date);

}

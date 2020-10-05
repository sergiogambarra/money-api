package com.gambarra.money.api.repository;

import com.gambarra.money.api.repository.entry.EntryRepositoryQuery;
import com.gambarra.money.api.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long>, EntryRepositoryQuery {
}

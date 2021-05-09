package com.gambarra.money.api.repository.entry;

import com.gambarra.money.api.dto.EntryStatisticByDay;
import com.gambarra.money.api.dto.EntryStatisticCategory;
import com.gambarra.money.api.dto.EntryStatisticPerson;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.projection.EntryResume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EntryRepositoryQuery {

    public List<EntryStatisticPerson> byPerson(LocalDate start, LocalDate end);
    public List<EntryStatisticCategory> byCategory(LocalDate monthReference);
    public List<EntryStatisticByDay> byDay(LocalDate monthReference);

    public Page<Entry> filter(EntryFilter entryFilter, Pageable pageable);
    public Page<EntryResume> resume(EntryFilter entryFilter, Pageable pageable);
}

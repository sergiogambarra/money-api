package com.gambarra.money.api.repository.entry;

import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.repository.filter.EntryFilter;
import com.gambarra.money.api.repository.projection.EntryResume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EntryRepositoryQuery {

    public Page<Entry> filter(EntryFilter entryFilter, Pageable pageable);
    public Page<EntryResume> resume(EntryFilter entryFilter, Pageable pageable);
}

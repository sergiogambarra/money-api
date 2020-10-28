package com.gambarra.money.api.service;

import com.gambarra.money.api.repository.EntryRepository;
import com.gambarra.money.api.service.exception.PersonNonexistentOrInactiveException;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private PersonRepository personRepository;

    public Entry update(Long id, Entry entry) {
        Entry entrySaved = searchExistingEntry(id);
        if (!entry.getPerson().equals(entrySaved.getPerson())){
            validatePerson(entry);
        }

        BeanUtils.copyProperties(entry, entrySaved, "id");

        return entryRepository.save(entrySaved);
    }

    private Entry searchExistingEntry(Long id) {
        Optional<Entry> entrySaved = entryRepository.findById(id);
        if (!entrySaved.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }
        return entrySaved.get();
    }

    private void validatePerson(Entry entry) {
        Person person = null;
        if (entry.getPerson().getId() != null){
            person = personRepository.getOne(entry.getPerson().getId());
        }

        if (person == null || person.isInactive()){
            throw new PersonNonexistentOrInactiveException();
        }
    }

    public Entry save(Entry entry) {
        Person person = personRepository.findById(entry.getPerson().getId()).orElse(null);
        if (person == null || person.isInactive()){
            throw new PersonNonexistentOrInactiveException();
        }
        return entryRepository.save(entry);
    }
}

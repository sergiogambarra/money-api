package com.gambarra.money.api.service;

import com.gambarra.money.api.repository.EntryRepository;
import com.gambarra.money.api.service.exception.PersonNonexistentOrInactiveException;
import com.gambarra.money.api.model.Entry;
import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private PersonRepository personRepository;

    public Entry save(Entry entry) {
        Person person = personRepository.findById(entry.getPerson().getId()).orElse(null);
        if (person == null || person.isInactive()){
            throw new PersonNonexistentOrInactiveException();
        }
        return entryRepository.save(entry);
    }
}

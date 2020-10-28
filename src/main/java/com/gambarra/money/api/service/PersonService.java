package com.gambarra.money.api.service;

import com.gambarra.money.api.model.Person;
import com.gambarra.money.api.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public Person update(Person person, Long id){

        Person personSaved = getPersonById(id);

        BeanUtils.copyProperties(person, personSaved, "id");
        return personRepository.save(personSaved);
    }

    public void updateActiveProperties(Long id, Boolean active) {
        Person personSaved = getPersonById(id);
        personSaved.setActive(active);
        personRepository.save(personSaved);
    }

    public Person getPersonById(Long id) {
        Optional<Person> personSaved = personRepository.findById(id);
        if (!personSaved.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }
        return personSaved.get();
    }


}

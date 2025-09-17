package com.company.service;

import com.company.model.Person;
import com.company.model.PersonType;
import com.company.repository.PersonRepository;
import com.company.repository.PersonRepositoryImpl;

public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl() {
        this.personRepository = new PersonRepositoryImpl();
    }

    public PersonServiceImpl(String path) {
        this.personRepository = new PersonRepositoryImpl(path);
    }

    @Override
    public Person findPerson(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        type = getDefaultType(type);

        if (personId != null && !personId.trim().isEmpty()
                && firstName != null && !firstName.trim().isEmpty()) {
            return personRepository.findByFileName(personId, firstName, type);
        }

        if (personId != null && !personId.trim().isEmpty()) {
            return personRepository.findById(personId, type);
        }

        return personRepository.findByPersonFromFile(
                firstName, lastName, mobile, email, pesel, type);
    }

    @Override
    public void createPerson(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        type = getDefaultType(type);

        personRepository.create(firstName, lastName, mobile, email, pesel, type);
    }

    @Override
    public boolean removePerson(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        type = getDefaultType(type);

        if (personId != null && !personId.trim().isEmpty()
                && firstName != null && !firstName.trim().isEmpty()) {
            return personRepository.removeByFileName(personId, firstName, type);
        }

        if (personId != null && !personId.trim().isEmpty()) {
            return personRepository.removeById(personId, type);
        }

        return personRepository.removeByPersonFromFile(
                firstName, lastName, mobile, email, pesel, type);
    }

    @Override
    public void modifyPerson(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        type = getDefaultType(type);

        personRepository.modify(
                personId, firstName, lastName, mobile, email, pesel, type);
    }

    @Override
    public void movePerson(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType typeFrom,
            PersonType typeTo) {
        if (typeFrom == null || typeTo == null) {
            return;
        }

        if (personId != null && !personId.trim().isEmpty()
                && firstName != null && !firstName.trim().isEmpty()) {
            personRepository.movePersonByFileName(personId, firstName, typeFrom, typeTo);
            return;
        }

        if (personId != null && !personId.trim().isEmpty()) {
            personRepository.movePersonById(personId, typeFrom, typeTo);
            return;
        }

        personRepository.movePersonByPersonFromFile(
                firstName, lastName, mobile, email, pesel, typeFrom, typeTo);

    }

    private static PersonType getDefaultType(PersonType type) {
        return (type != null) ? type : PersonType.INTERNAL;

    }
}


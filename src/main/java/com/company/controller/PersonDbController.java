package com.company.controller;

import com.company.model.Person;
import com.company.model.PersonType;
import com.company.service.PersonService;
import com.company.service.PersonServiceImpl;

public class PersonDbController {
    private final PersonService personService;

    public PersonDbController() {
        this.personService = new PersonServiceImpl();
    }

    public PersonDbController(PersonService personService) {
        this.personService = personService;
    }

    public Person find(String personId,
                       String firstName,
                       String lastName,
                       String mobile,
                       String email,
                       String pesel,
                       PersonType type) {
        return personService.findPerson(
                personId, firstName, lastName, mobile, email, pesel, type);
    }

    public void create(String firstName,
                       String lastName,
                       String mobile,
                       String email,
                       String pesel,
                       PersonType type) {
        personService.createPerson(
                firstName, lastName, mobile, email, pesel, type);
    }

    public boolean remove(String personId,
                          String firstName,
                          String lastName,
                          String mobile,
                          String email,
                          String pesel,
                          PersonType type) {
        return personService.removePerson(
                personId, firstName, lastName, mobile, email, pesel, type);
    }

    public void modify(String personId,
                       String firstName,
                       String lastName,
                       String mobile,
                       String email,
                       String pesel,
                       PersonType type) {
        personService.modifyPerson(
                personId, firstName, lastName, mobile, email, pesel, type);
    }

    public void move(String personId,
                     String firstName,
                     String lastName,
                     String mobile,
                     String email,
                     String pesel,
                     PersonType typeFrom,
                     PersonType typeTo) {
        personService.movePerson(
                personId, firstName, lastName, mobile, email, pesel, typeFrom, typeTo);
    }
}

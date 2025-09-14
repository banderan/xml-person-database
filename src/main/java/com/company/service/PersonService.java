package com.company.service;

import com.company.model.Person;
import com.company.model.PersonType;

public interface PersonService {
    Person findPerson(String personId,
                      String firstName,
                      String lastName,
                      String mobile,
                      String email,
                      String pesel,
                      PersonType type);

    void createPerson(String firstName,
                      String lastName,
                      String mobile,
                      String email,
                      String pesel,
                      PersonType type);

    boolean removePerson(String personId,
                         String firstName,
                         String lastName,
                         String mobile,
                         String email,
                         String pesel,
                         PersonType type);

    void modifyPerson(String personId,
                      String firstName,
                      String lastName,
                      String mobile,
                      String email,
                      String pesel,
                      PersonType type);

    void movePerson(String personId,
                    String firstName,
                    String lastName,
                    String mobile,
                    String email,
                    String pesel,
                    PersonType typeFrom,
                    PersonType typeTo);
}

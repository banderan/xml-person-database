package com.company.repository;

import com.company.model.Person;
import com.company.model.PersonType;

public interface PersonRepository {
    Person findByFileName(String personId,
                          String firstName,
                          PersonType type);

    Person findById(String personId,
                    PersonType type);

    Person findByPersonFromFile(String firstName,
                                String lastName,
                                String mobile,
                                String email,
                                String pesel,
                                PersonType type);

    void create(String firstName,
                String lastName,
                String mobile,
                String email,
                String pesel,
                PersonType type);

    boolean removeByFileName(String personId,
                             String firstName,
                             PersonType type);

    boolean removeById(String personId,
                       PersonType type);

    boolean removeByPersonFromFile(String firstName,
                                   String lastName,
                                   String mobile,
                                   String email,
                                   String pesel,
                                   PersonType type);

    void modify(String personId,
                String firstName,
                String lastName,
                String mobile,
                String email,
                String pesel,
                PersonType type);

    void movePersonByFileName(String personId,
                              String firstName,
                              PersonType typeFrom,
                              PersonType typeTo);

    void movePersonById(String personId,
                        PersonType typeFrom,
                        PersonType typeTo);

    void movePersonByPersonFromFile(String firstName,
                                    String lastName,
                                    String mobile,
                                    String email,
                                    String pesel,
                                    PersonType typeFrom,
                                    PersonType typeTo);
}

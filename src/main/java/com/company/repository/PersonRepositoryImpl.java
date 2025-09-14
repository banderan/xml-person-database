package com.company.repository;

import com.company.converter.PersonXmlConverter;
import com.company.model.Person;
import com.company.model.PersonType;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PersonRepositoryImpl implements PersonRepository {
    private static final String PATH = "src" + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "data"
            + File.separator;
    private static final String DOT = ".";
    private final PersonXmlConverter converter;

    public PersonRepositoryImpl() {
        this.converter = new PersonXmlConverter();
    }

    @Override
    public Person findByFileName(String personId, String firstName, PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //find by file name
        String fileName = getFileName(personId, firstName, type);
        String fullPath = getFullPath(type, fileName);

        if (xmlFilesList.contains(fileName)) {
            try {
                return converter.fromXml(fullPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Person findById(String personId, PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //find by id
        try {
            for (String xmlFile : xmlFilesList) {
                String fileId = xmlFile.substring(0, xmlFile.indexOf(DOT));
                if (fileId.equals(personId)) {
                    return converter.fromXml(getFullPath(type, xmlFile));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Person findByPersonFromFile(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //find by file fields
        try {
            for (String xmlFileName : xmlFilesList) {
                Person person = converter.fromXml(getFullPath(type, xmlFileName));
                if (isFileContainsInputValue(firstName, lastName, mobile, email, pesel, person)) {
                    return person;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void create(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        if (type == null) {
            type = PersonType.INTERNAL;
        }

        int maxId = getMaxId(type);
        createWorker(String.valueOf(maxId), firstName, lastName, mobile, email, pesel, type);
    }

    @Override
    public boolean removeByFileName(String personId, String firstName, PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //remove by file name
        String fileName = getFileName(personId, firstName, type);
        if (xmlFilesList.contains(fileName)) {
            return removeXml(getFullPath(type, fileName));
        }
        return false;
    }

    @Override
    public boolean removeById(String personId, PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //remove by id
        for (String xmlFile : xmlFilesList) {
            String fileId = xmlFile.substring(0, xmlFile.indexOf(DOT));

            if (fileId.equals(personId)) {
                return removeXml(getFullPath(type, xmlFile));
            }
        }
        return false;
    }

    @Override
    public boolean removeByPersonFromFile(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        List<String> xmlFilesList = listXml(type);

        //remove by file fields
        try {
            for (String xmlFileName : xmlFilesList) {
                String fullPath = getFullPath(type, xmlFileName);
                Person person = converter.fromXml(fullPath);

                if (isFileContainsInputValue(firstName, lastName, mobile, email, pesel, person)) {
                    return removeXml(fullPath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public void modify(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        Person person = getPerson(personId, firstName, lastName, mobile, email, pesel);
        List<String> xmlFilesList = listXml(type);
        boolean isRemovedFile = false;

        //modify by id
        if (personId == null || !personId.trim().isEmpty()) {
            for (String xmlFile : xmlFilesList) {
                String fileId = xmlFile.substring(0, xmlFile.indexOf(DOT));

                if (fileId.equals(personId)) {
                    isRemovedFile = removeXml(getFullPath(type, xmlFile));
                    break;
                }
            }
        }
        if (isRemovedFile) {
            try {
                String fileName = getFileName(personId, firstName, type);
                converter.toXml(person, getFullPath(type, fileName));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void movePersonByFileName(
            String personId,
            String firstName,
            PersonType typeFrom,
            PersonType typeTo) {
        List<String> xmlFilesList = listXml(typeFrom);

        //movePerson by file name
        String fileName = getFileName(personId, firstName, typeFrom);
        if (xmlFilesList.contains(fileName)) {
            try {
                String fullPath = getFullPath(typeFrom, fileName);
                Person person = converter.fromXml(fullPath);
                removeXml(fullPath);

                String xmlFileWithUpdatedId = prepareNewFile(typeTo, person, fileName);

                fullPath = getFullPath(typeTo, xmlFileWithUpdatedId);
                converter.toXml(person, fullPath);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void movePersonById(String personId, PersonType typeFrom, PersonType typeTo) {
        List<String> xmlFilesList = listXml(typeFrom);

        //movePerson by id
        if (personId != null && !personId.trim().isEmpty()) {
            try {
                for (String xmlFile : xmlFilesList) {
                    String fileId = xmlFile.substring(0, xmlFile.indexOf(DOT));
                    if (fileId.equals(personId)) {
                        String pathFrom = getFullPath(typeFrom, xmlFile);
                        Person person = converter.fromXml(pathFrom);
                        removeXml(pathFrom);

                        String xmlFileWithUpdatedId = prepareNewFile(typeTo, person, xmlFile);

                        String fullPath = getFullPath(typeTo, xmlFileWithUpdatedId);
                        converter.toXml(person, fullPath);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void movePersonByPersonFromFile(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType typeFrom,
            PersonType typeTo) {
        List<String> xmlFilesList = listXml(typeFrom);

        //movePerson by file fields
        try {
            for (String xmlFileName : xmlFilesList) {
                String fullPath = getFullPath(typeFrom, xmlFileName);
                Person person = converter.fromXml(fullPath);

                if (isFileContainsInputValue(firstName, lastName, mobile, email, pesel, person)) {
                    removeXml(fullPath);

                    String xmlFileWithUpdatedId = prepareNewFile(typeTo, person, xmlFileName);

                    fullPath = getFullPath(typeTo, xmlFileWithUpdatedId);
                    converter.toXml(person, fullPath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getMaxId(PersonType type) {
        List<String> xmlFilesList = listXml(type);
        List<Person> personList = xmlFilesList.stream()
                .map(f -> {
                    try {
                        return converter.fromXml(
                                PATH + type.toString().toLowerCase() + File.separator + f);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
        int maxId = 0;
        for (Person person : personList) {
            int personId = Integer.parseInt(person.getPersonId());
            if (maxId <= personId) {
                maxId = personId + 1;
            }
        }
        return maxId;
    }

    private void createWorker(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            PersonType type) {
        try {
            Person person = getPerson(personId, firstName, lastName, mobile, email, pesel);

            //Preparing first name for adding it to file name
            if (firstName == null || firstName.isEmpty()) {
                double personPositiveHashCode = Math.pow(person.hashCode(), 2);
                firstName = String.valueOf(personPositiveHashCode).substring(0, 8);
            }
            String fileName = getFileName(personId, firstName, type);

            converter.toXml(person,
                    PATH + type.toString().toLowerCase() + File.separator + fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isFileContainsInputValue(
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel,
            Person person) {
        return isEquals(firstName, person.getFirstName())
                && isEquals(lastName, person.getLastName())
                && isEquals(mobile, person.getMobile())
                && isEquals(email, person.getEmail())
                && isEquals(pesel, person.getPesel());
    }

    private static boolean isEquals(String input, String personField) {
        return ((input != null) ? input.trim() : "").equals(personField);
    }

    private List<String> listXml(PersonType type) {

        File file = new File(PATH + type.toString().toLowerCase());

        return Arrays.stream(Objects.requireNonNull(file.listFiles()))
                .filter(File::isFile)
                .map(File::getName)
                .toList();
    }

    private static boolean removeXml(String fullPath) {
        File file = new File(fullPath);
        return file.delete();
    }

    private static Person getPerson(
            String personId,
            String firstName,
            String lastName,
            String mobile,
            String email,
            String pesel) {
        Person person = new Person();
        person.setPersonId(personId);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setMobile(mobile);
        person.setEmail(email);
        person.setPesel(pesel);
        return person;
    }

    private static String getFileName(String personId, String firstName, PersonType type) {
        return personId + DOT + firstName + DOT + type.toString().toLowerCase() + ".xml";
    }

    private static String getFullPath(PersonType type, String fileName) {
        return PATH + type.toString().toLowerCase() + File.separator + fileName;
    }

    private String prepareNewFile(PersonType typeTo, Person person, String fileName) {
        int maxId = getMaxId(typeTo);
        person.setPersonId(String.valueOf(maxId));

        return maxId
                + fileName.substring(fileName.indexOf(DOT), fileName.length() - 12)
                + typeTo.toString().toLowerCase()
                + ".xml";
    }
}

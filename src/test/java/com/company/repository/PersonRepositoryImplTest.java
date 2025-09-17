package com.company.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.company.converter.PersonXmlConverter;
import com.company.model.Person;
import com.company.model.PersonType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonRepositoryImplTest {
    private static final String TEST_PATH = "src" + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "data"
            + File.separator;
    private static final String DOT = ".";
    private final PersonRepository repository = new PersonRepositoryImpl(TEST_PATH);
    private final PersonXmlConverter converter = new PersonXmlConverter();

    @BeforeEach
    void setUp() {
        try {
            Files.createDirectories(Path.of("src/test/resources/data/internal"));
            Files.createDirectories(Path.of("src/test/resources/data/external"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        createTestPerson(getFilledPerson(), PersonType.INTERNAL);
        createTestPerson(getNullPerson(), PersonType.EXTERNAL);
    }

    @AfterEach
    void tearDown() {
        deleteTestFiles(PersonType.INTERNAL);
        deleteTestFiles(PersonType.EXTERNAL);
    }

    @Test
    void findByFileName_PersonExists() {
        Person expected = getFilledPerson();
        Person result = repository.findByFileName("1", "John", PersonType.INTERNAL);
        assertEquals(expected, result);
    }

    @Test
    void findByFileName_PersonNotExists() {
        Person result = repository.findByFileName("99", "Ghost", PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void findByFileName_EmptyArguments() {
        Person result = repository.findByFileName("", "", PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void findById_PersonExists() {
        Person expected = getFilledPerson();
        Person result = repository.findById("1", PersonType.INTERNAL);
        assertEquals(expected, result);
    }

    @Test
    void findById_PersonNotExists() {
        Person result = repository.findById("9999", PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void findById_NullId() {
        Person result = repository.findById(null, PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void findByPersonFromFile_PersonExists() {
        Person expected = getFilledPerson();
        Person result = repository.findByPersonFromFile(
                "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        assertEquals(expected, result);
    }

    @Test
    void findByPersonFromFile_PersonNotExists() {
        Person result = repository.findByPersonFromFile(
                "Ghost", "Nobody",
                null, null, null, PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void findByPersonFromFile_EmptyArguments() {
        Person result = repository.findByPersonFromFile(
                "", "",
                "", "", "", PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void create_PersonWithAllFields() {
        Person result = repository.create(
                "Anna", "Smith",
                "111111111", "anna@test.com", "222222222", PersonType.INTERNAL);
        assertNotNull(result);
        assertEquals("Anna", result.getFirstName());
    }

    @Test
    void create_PersonWithNullFields() {
        Person result = repository.create(
                null, null,
                null, null, null, null);
        assertNotNull(result);
        assertEquals("INTERNAL", PersonType.INTERNAL.toString());
    }

    @Test
    void create_PersonWithEmptyFields() {
        Person result = repository.create(
                "", "",
                "", "", "", PersonType.EXTERNAL);
        assertNotNull(result);
    }

    @Test
    void removeByFileName_PersonExists() {
        boolean result = repository.removeByFileName(
                "1", "John", PersonType.INTERNAL);
        assertTrue(result);
    }

    @Test
    void removeByFileName_PersonNotExists() {
        boolean result = repository.removeByFileName(
                "999", "Ghost", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removeByFileName_EmptyArguments() {
        boolean result = repository.removeByFileName(
                "", "", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removeById_PersonExists() {
        boolean result = repository.removeById(
                "1", PersonType.INTERNAL);
        assertTrue(result);
    }

    @Test
    void removeById_PersonNotExists() {
        boolean result = repository.removeById(
                "999", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removeById_EmptyId() {
        boolean result = repository.removeById(
                "", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removeByPersonFromFile_PersonExists() {
        boolean result = repository.removeByPersonFromFile(
                "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        assertTrue(result);
    }

    @Test
    void removeByPersonFromFile_PersonNotExists() {
        boolean result = repository.removeByPersonFromFile(
                "Ghost", "Nobody",
                null, null, null, PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removeByPersonFromFile_EmptyArguments() {
        boolean result = repository.removeByPersonFromFile(
                "", "",
                "", "", "", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void modify_PersonExists() {
        Person result = repository.modify(
                "1", "Johnny", "Doe",
                "123456789", "johnny@doe.com", "987654321", PersonType.INTERNAL);
        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
    }

    @Test
    void modify_PersonNotExists() {
        Person result = repository.modify(
                "999", "Ghost", "X",
                null, null, null, PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void modify_EmptyArguments() {
        Person result = repository.modify(
                "", "", "",
                "", "", "", PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void movePersonByFileName_PersonExists() {
        boolean result = repository.movePersonByFileName(
                "1", "John", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertTrue(result);
    }

    @Test
    void movePersonByFileName_PersonNotExists() {
        boolean result = repository.movePersonByFileName(
                "999", "Ghost", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    @Test
    void movePersonByFileName_EmptyArguments() {
        boolean result = repository.movePersonByFileName(
                "", "", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    @Test
    void movePersonById_PersonExists() {
        boolean result = repository.movePersonById(
                "1", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertTrue(result);
    }

    @Test
    void movePersonById_PersonNotExists() {
        boolean result = repository.movePersonById(
                "999", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    @Test
    void movePersonById_EmptyId() {
        boolean result = repository.movePersonById(
                "", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    @Test
    void movePersonByPersonFromFile_PersonExists() {
        boolean result = repository.movePersonByPersonFromFile(
                "John", "Doe",
                "123456789", "john@doe.com", "987654321",
                PersonType.INTERNAL, PersonType.EXTERNAL);
        assertTrue(result);
    }

    @Test
    void movePersonByPersonFromFile_PersonNotExists() {
        boolean result = repository.movePersonByPersonFromFile(
                "Ghost", "Nobody",
                null, null, null,
                PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    @Test
    void movePersonByPersonFromFile_EmptyArguments() {
        boolean result = repository.movePersonByPersonFromFile(
                "", "",
                "", "", "", PersonType.INTERNAL, PersonType.EXTERNAL);
        assertFalse(result);
    }

    private void createTestPerson(Person person, PersonType type) {
        try {
            String fileName = getFileName(person.getPersonId(), person.getFirstName(), type);
            converter.toXml(person, getFullPath(type, fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteTestFiles(PersonType type) {
        File dir = new File(TEST_PATH + type.toString().toLowerCase());
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
    }

    private static Person getNullPerson() {
        Person p = new Person();
        p.setPersonId("1");
        return p;
    }

    private static Person getFilledPerson() {
        Person p = new Person();
        p.setPersonId("1");
        p.setFirstName("John");
        p.setLastName("Doe");
        p.setMobile("123456789");
        p.setEmail("john@doe.com");
        p.setPesel("987654321");
        return p;
    }

    private static String getFileName(String personId, String firstName, PersonType type) {
        return personId + DOT + firstName + DOT + type.toString().toLowerCase() + ".xml";
    }

    private static String getFullPath(PersonType type, String fileName) {
        return TEST_PATH + type.toString().toLowerCase() + File.separator + fileName;
    }
}

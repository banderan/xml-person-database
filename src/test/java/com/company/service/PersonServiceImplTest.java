package com.company.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.company.model.Person;
import com.company.model.PersonType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonServiceImplTest {

    private static final String TEST_PATH = "src" + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "data"
            + File.separator;

    private PersonService service;

    @BeforeEach
    void setUp() {
        try {
            Files.createDirectories(Path.of("src/test/resources/data/internal"));
            Files.createDirectories(Path.of("src/test/resources/data/external"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        service = new PersonServiceImpl(TEST_PATH);
        deleteAllTestFiles();

        service.createPerson(
                "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        service.createPerson(
                "Jane", "Doe",
                "111111111", "jane@doe.com", "123456789", PersonType.EXTERNAL);
    }

    @AfterEach
    void tearDown() {
        deleteAllTestFiles();
    }

    @Test
    void findPerson_PersonExists() {
        Person p = service.findPerson(
                "0", "John", null,
                null, null, null, PersonType.INTERNAL);
        assertNotNull(p);
        assertEquals("John", p.getFirstName());
    }

    @Test
    void findPerson_PersonNotExists() {
        Person p = service.findPerson(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertNull(p);
    }

    @Test
    void findPerson_EmptyArguments() {
        Person p = service.findPerson(
                "", "", "",
                "", "", "", PersonType.INTERNAL);
        assertNull(p);
    }

    @Test
    void createPerson_PersonWithAllFields() {
        service.createPerson(
                "Anna", "Smith",
                "222222222", "anna@test.com", "111111111", PersonType.INTERNAL);
        Person p = service.findPerson(
                null, "Anna", "Smith",
                "222222222", "anna@test.com", "111111111", PersonType.INTERNAL);
        assertNotNull(p);
        assertEquals("Anna", p.getFirstName());
    }

    @Test
    void createPerson_PersonWithNullFields() {
        service.createPerson(
                null, null,
                null, null, null, null);
        Person p = service.findPerson(
                null, null, null,
                null, null, null, PersonType.INTERNAL);
        assertNotNull(p);
    }

    @Test
    void createPerson_PersonWithEmptyFields() {
        service.createPerson(
                "", "",
                "", "", "", PersonType.EXTERNAL);
        Person p = service.findPerson(
                null, "", "",
                "", "", "", PersonType.EXTERNAL);
        assertNotNull(p);
    }

    @Test
    void removePerson_PersonExists() {
        boolean result = service.removePerson(
                null, "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        assertTrue(result);
    }

    @Test
    void removePerson_PersonNotExists() {
        boolean result = service.removePerson(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void removePerson_EmptyArguments() {
        boolean result = service.removePerson(
                "", "", "",
                "", "", "", PersonType.INTERNAL);
        assertFalse(result);
    }

    @Test
    void modifyPerson_PersonExists() {
        service.modifyPerson(
                "0", "Johnny", "Doe",
                "123456789", "johnny@doe.com", "987654321", PersonType.INTERNAL);
        Person p = service.findPerson(
                "0", "Johnny", null,
                null, null, null, PersonType.INTERNAL);
        assertNotNull(p);
        assertEquals("Johnny", p.getFirstName());
    }

    @Test
    void modifyPerson_PersonNotExists() {
        service.modifyPerson(
                "999", "Ghost", "X",
                null, null, null, PersonType.INTERNAL);
        Person p = service.findPerson(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertNull(p);
    }

    @Test
    void modifyPerson_EmptyArguments() {
        service.modifyPerson(
                "", "", "",
                "", "", "", PersonType.INTERNAL);
        Person p = service.findPerson(
                "", "", "",
                "", "", "", PersonType.INTERNAL);
        assertNull(p);
    }

    @Test
    void movePerson_PersonExists() {
        service.movePerson(
                "0", "John", null,
                null, null, null, PersonType.INTERNAL, PersonType.EXTERNAL);

        Person p = service.findPerson(
                "0", null, null,
                null, null, null, PersonType.EXTERNAL);

        assertNotNull(p);
    }

    @Test
    void movePerson_PersonNotExists() {
        service.movePerson(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL, PersonType.EXTERNAL);
        Person p = service.findPerson(
                "999", "Ghost", null,
                null, null, null, PersonType.EXTERNAL);
        assertNull(p);
    }

    @Test
    void movePerson_EmptyArguments() {
        service.movePerson(
                "", "", "",
                "", "", "", null, null);
    }

    private void deleteAllTestFiles() {
        deleteFiles(PersonType.INTERNAL);
        deleteFiles(PersonType.EXTERNAL);
    }

    private void deleteFiles(PersonType type) {
        File dir = new File(TEST_PATH + type.toString().toLowerCase());
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
    }
}

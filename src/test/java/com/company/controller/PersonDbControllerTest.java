package com.company.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.company.model.Person;
import com.company.model.PersonType;
import com.company.service.PersonServiceImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonDbControllerTest {

    private static final String TEST_PATH = "src" + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "data"
            + File.separator;
    private PersonDbController controller;

    @BeforeAll
    static void setupAll() {
        try {
            Files.createDirectories(Path.of(TEST_PATH + "internal"));
            Files.createDirectories(Path.of(TEST_PATH + "external"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        controller = new PersonDbController(new PersonServiceImpl(TEST_PATH));
        clearTestData();
    }

    @AfterEach
    void tearDown() {
        clearTestData();
    }

    private void clearTestData() {
        deleteFiles("internal");
        deleteFiles("external");
    }

    private void deleteFiles(String folder) {
        File dir = new File(TEST_PATH + folder);
        if (dir.exists() && dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
    }

    @Test
    void create_PersonWithAllFields() {
        controller.create(
                "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        Person result = controller.find(
                null, "John", "Doe",
                "123456789", "john@doe.com", "987654321", PersonType.INTERNAL);
        assertNotNull(result);
    }

    @Test
    void create_PersonWithoutAllFields() {
        assertDoesNotThrow(() ->
                controller.create(
                        null, null,
                        null, null, null, null)
        );
    }

    @Test
    void create_PersonWithEmptyFields() {
        controller.create(
                "", "",
                "", "", "", PersonType.EXTERNAL);
        Person result = controller.find(
                null, "", "",
                "", "", "", PersonType.EXTERNAL);
        assertNotNull(result);
    }

    @Test
    void find_PersonWithAllFields() {
        controller.create(
                "Alice", "Smith",
                "111222333", "alice@test.com", "123456789", PersonType.INTERNAL);
        Person result = controller.find(
                null, "Alice", "Smith",
                "111222333", "alice@test.com", "123456789", PersonType.INTERNAL);
        assertNotNull(result);
    }

    @Test
    void find_PersonWithoutCreatIt() {
        Person result = controller.find(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void find_PersonWithoutFilledFieldsAndWithoutFile() {
        Person result = controller.find(
                "", "", "",
                "", "", "", null);
        assertNull(result);
    }

    @Test
    void modify_PersonWithAllFields() {
        controller.create(
                "Bob", "Brown",
                "222333444", "bob@test.com", "987654321", PersonType.INTERNAL);
        controller.modify(
                "0", "Bobby", "Brown",
                "222333444", "bob@test.com", "987654321", PersonType.INTERNAL);
        Person result = controller.find(
                null, "Bobby", "Brown",
                "222333444", "bob@test.com", "987654321", PersonType.INTERNAL);
        assertNotNull(result);
        assertEquals("Bobby", result.getFirstName());
    }

    @Test
    void modify_PersonWithoutFile() {
        controller.modify(
                "999", "Ghost", "X",
                null, null, null, PersonType.INTERNAL);
        Person result = controller.find(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertNull(result);
    }

    @Test
    void modify_PersonWithoutFilledFieldsAndWithoutFile() {
        controller.modify(
                "", "", "",
                "", "", "", null);
        Person result = controller.find(
                "", "", "",
                "", "", "", null);
        assertNull(result);
    }

    @Test
    void remove_PersonWithAllFields() {
        controller.create(
                "Charlie", "White",
                "555666777", "charlie@test.com", "111222333", PersonType.INTERNAL);
        boolean removed = controller.remove(
                null, "Charlie", "White",
                "555666777", "charlie@test.com", "111222333", PersonType.INTERNAL);
        assertTrue(removed);
    }

    @Test
    void remove_PersonWithoutFile() {
        boolean removed = controller.remove(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL);
        assertFalse(removed);
    }

    @Test
    void remove_PersonWithoutFilledFieldsAndWithoutFile() {
        boolean removed = controller.remove(
                "", "", "",
                "", "", "", null);
        assertFalse(removed);
    }

    @Test
    void move_PersonWithAllFields() {
        controller.create(
                "David", "King",
                "777888999", "david@test.com", "444555666", PersonType.INTERNAL);
        controller.move(
                null, "David", "King",
                "777888999", "david@test.com", "444555666",
                PersonType.INTERNAL, PersonType.EXTERNAL);
        Person result = controller.find(
                null, "David", "King",
                "777888999", "david@test.com", "444555666", PersonType.EXTERNAL);
        assertNotNull(result);
    }

    @Test
    void move_PersonWithoutFile() {
        controller.move(
                "999", "Ghost", null,
                null, null, null, PersonType.INTERNAL, PersonType.EXTERNAL);
        Person result = controller.find(
                "999", "Ghost", null,
                null, null, null, PersonType.EXTERNAL);
        assertNull(result);
    }

    @Test
    void move_PersonWithoutFilledFieldsAndWithoutFile() {
        controller.move(
                "", "", "", "", "", "", null, null);
    }
}

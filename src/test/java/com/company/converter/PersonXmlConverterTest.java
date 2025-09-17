package com.company.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.company.model.Person;
import com.company.model.PersonType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PersonXmlConverterTest {

    public static final String DOT = ".";
    private static final String TEST_PATH = "src" + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "data"
            + File.separator;
    private final PersonXmlConverter converter = new PersonXmlConverter();

    @BeforeAll
    static void setupAll() {
        try {
            Files.createDirectories(Path.of("src/test/resources/data/internal"));
            Files.createDirectories(Path.of("src/test/resources/data/external"));
            Files.createDirectories(Path.of(TEST_PATH + "internal"));
            Files.createDirectories(Path.of(TEST_PATH + "external"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void cleanUp() {
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

    private Person getFilledPerson() {
        Person p = new Person();
        p.setPersonId("0");
        p.setFirstName("John");
        p.setLastName("Doe");
        p.setMobile("123456789");
        p.setEmail("john@doe.com");
        p.setPesel("987654321");
        return p;
    }

    private Person getNullPerson() {
        Person p = new Person();
        p.setPersonId("0");
        return p;
    }

    private String getFileName(Person p, PersonType type) {
        return p.getPersonId()
                + DOT
                + p.getFirstName()
                + DOT
                + type.toString().toLowerCase()
                + ".xml";
    }

    private String getFullPath(PersonType type, String fileName) {
        return TEST_PATH + type.toString().toLowerCase() + File.separator + fileName;
    }

    private void assertXmlConversion(Person person, PersonType type) throws Exception {
        String fileName = getFileName(person, type);
        String fullPath = getFullPath(type, fileName);
        converter.toXml(person, fullPath);
        Person result = converter.fromXml(fullPath);
        assertEquals(getPersonField(person.getFirstName()), result.getFirstName());
        assertEquals(getPersonField(person.getLastName()), result.getLastName());
        assertEquals(getPersonField(person.getMobile()), result.getMobile());
        assertEquals(getPersonField(person.getEmail()), result.getEmail());
        assertEquals(getPersonField(person.getPesel()), result.getPesel());
    }

    private static String getPersonField(String personField) {
        return personField != null ? personField : "";
    }

    @Test
    void toXml_FilledPersonInternal() throws Exception {
        assertXmlConversion(getFilledPerson(), PersonType.INTERNAL);
    }

    @Test
    void toXml_NullPersonExternal() throws Exception {
        assertXmlConversion(getNullPerson(), PersonType.EXTERNAL);
    }

    @Test
    void toXml_NullPath() {
        Person p = getFilledPerson();
        assertThrows(Exception.class, () -> converter.toXml(p, null));
    }

    @Test
    void fromXml_FilledPerson() throws Exception {
        Person p = getFilledPerson();
        String fileName = getFileName(p, PersonType.INTERNAL);
        converter.toXml(p, getFullPath(PersonType.INTERNAL, fileName));
        Person result = converter.fromXml(getFullPath(PersonType.INTERNAL, fileName));
        assertEquals(p, result);
    }

    @Test
    void fromXml_NullPerson() throws Exception {
        Person p = getNullPerson();
        String fileName = getFileName(p, PersonType.EXTERNAL);
        converter.toXml(p, getFullPath(PersonType.EXTERNAL, fileName));
        Person result = converter.fromXml(getFullPath(PersonType.EXTERNAL, fileName));
        assertEquals("", result.getFirstName());
        assertEquals("", result.getLastName());
    }

    @Test
    void fromXml_NonExistingFile() {
        assertThrows(Exception.class, () -> converter.fromXml("non_existing.xml"));
    }

    @Test
    void fromXml_WrongFileStructure() {
        String fileName = TEST_PATH + "internal" + File.separator + "invalid.xml";
        assertThrows(Exception.class, () -> converter.fromXml(fileName));
    }
}

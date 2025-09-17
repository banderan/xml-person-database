# XML Person Database

## 📌 Project Overview
`xml-person-database` is a pure **Java SE** application for managing employee data stored in XML files.  
Each employee has their own XML file in the following directories:

- `Internal` – internal employees
- `External` – external employees

The system supports full **CRUD** operations, searching by any set of attributes, and moving employees between directories.

---

## 🚀 Features
- **Add a new employee** → `createPerson`
- **Remove an employee** → `removePerson`
- **Modify employee data** → `modifyPerson`
- **Search for employees** → `findPerson`
- **Move employees** between `Internal` and `External` directories
- Każdy pracownik przechowywany w osobnym pliku XML
- **Pure Java SE** – bez `javax.xml.bind.*` i bez frameworków

---

## 📂 Models

### Person
- `personId`
- `firstName`
- `lastName`
- `mobile`
- `email`
- `pesel`

### PersonType
- `INTERNAL`
- `EXTERNAL`

---

## 🛠 Usage Guide

The application uses the `PersonDbController` controller, which provides simple methods for working with the person database.

### 1. Creating a new person
```java
PersonDbController controller = new PersonDbController();
controller.create(
    "Jan", 
    "Kowalski", 
    "123456789", 
    "jan.kowalski@example.com", 
    "89012345678", 
    PersonType.INTERNAL
);
```
➡️ An XML file will be created in the internal directory.

### 2. Search for a person
```java
Person person = controller.find(
    "1",           // personId
    null,          // firstName
    null,          // lastName
    null,          // mobile
    null,          // email
    null,          // pesel
    PersonType.INTERNAL
);
System.out.println(person.getFirstName());
```
➡️ You can search:

- by personId + firstName (search by XML file name),
- by personId only,
- or by all other fields (first name, last name, personal identification number, etc.).

### 3. Modifying personal data
```java
controller.modify(
    "1",          // personId
    "Janusz",     // firstName
    "Kowalski", 
    "987654321", 
    "janusz.kowalski@example.com", 
    "89012345678", 
    PersonType.INTERNAL
);
```
➡️ The old XML file will be deleted and replaced with the new one.

### 4. Removing a person
```java
boolean removed = controller.remove(
    "1", 
    "Janusz", 
    null, 
    null, 
    null, 
    null, 
    PersonType.INTERNAL
);
```
➡️ You can delete:

- by file name (personId + firstName),
- by personId alone,
- by personal data (first name, last name, social security number, etc.).

### 5. Moving a person between directories
```java
controller.move(
    "1", 
    "Janusz", 
    null, 
    null, 
    null, 
    null, 
    PersonType.INTERNAL, 
    PersonType.EXTERNAL
);
```
➡️ The person will be removed from the internal catalog and saved in the external catalog (they will receive a new personId).

---

## 📑 XML file structure

Each employee is recorded in a separate file named:
```txt
{personId}.{firstName}.{type}.xml
```
### Example (1.Janusz.internal.xml):
```xml
<person>
    <personId>1</personId>
    <firstName>Janusz</firstName>
    <lastName>Kowalski</lastName>
    <mobile>987654321</mobile>
    <email>janusz.kowalski@example.com</email>
    <pesel>89012345678</pesel>
</person>
```

# XML Person Database

## Project Overview
`xml-person-database` is a pure Java SE application for managing employee data stored in XML files. Each employee has a separate XML file located in either:

- `Internal` – internal employees
- `External` – external employees

The system supports full CRUD operations, moving employees between types, and searching by any combination of attributes.

---

## Features

- **Add a new employee** (`createPerson`)  
- **Remove an employee** (`removePerson`)  
- **Modify employee data** (`modifyPerson`)  
- **Search for employees** (`findPerson`)
- **Move employees** between `Internal` and `External` directories
- Each employee is stored in a separate XML file
- Pure Java SE, no `javax.xml.bind.*` or frameworks used

---

## Models
### Person
- personId
- firstName
- lastName
- mobile
- email
- pesel
### PersonType
- INTERNAL
- EXTERNAL


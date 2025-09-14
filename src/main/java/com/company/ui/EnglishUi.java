package com.company.ui;

import com.company.controller.PersonDbController;
import com.company.model.Person;
import com.company.model.PersonType;
import java.util.Scanner;

public class EnglishUi {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";
    private final PersonDbController personDbController;
    private final Scanner scanner;
    private boolean isAppRunning = true;
    private String personId;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private String pesel;

    public EnglishUi() {
        personDbController = new PersonDbController();
        scanner = new Scanner(System.in);
    }

    public void menuInAng() {
        while (isAppRunning) {
            clearTerminal();
            System.out.println(BLUE
                    + "|========================================================================="
                    + "================|");
            System.out.println("|               "
                    + GREEN
                    + "Welcome to Person DB by Bartosz Wójcik"
                    + BLUE
                    + "                                    |");
            System.out.println(
                    "|---------------------------------------------------------------------------"
                            + "--------------|");
            System.out.println("|"
                    + CYAN
                    + "Here are the actions you can perform in this program:"
                    + BLUE
                    + "                                    |");
            System.out.println(
                    "|---------------------------------------------------------------------------"
                            + "--------------|");
            System.out.println("|1. "
                    + YELLOW
                    + "Find a person in the database"
                    + BLUE
                    + "                                                         |");
            System.out.println("|2. "
                    + YELLOW
                    + "Create a new person record"
                    + BLUE
                    + "                                                            |");
            System.out.println("|3. "
                    + YELLOW
                    + "Remove a person from the database"
                    + BLUE
                    + "                                                     |");
            System.out.println("|4. "
                    + YELLOW
                    + "Modify a person's data"
                    + BLUE
                    + "                                                                |");
            System.out.println("|5. "
                    + YELLOW
                    + "Move a person between external and internal"
                    + BLUE
                    + "                                           |");
            System.out.println("|E. "
                    + RED
                    + "Exit the program"
                    + BLUE
                    + "                                                                      |");
            System.out.println(
                    "|---------------------------------------------------------------------------"
                            + "--------------|");
            System.out.println("|"
                    + PURPLE
                    + "NOTE: Until you exit the program, some actions will not take effect "
                    + "immediately."
                    + BLUE
                    + "         |");
            System.out.println(
                    "|---------------------------------------------------------------------------"
                            + "--------------|"
                            + RESET);

            String actionNumber = scanner.nextLine().toUpperCase();

            switch (actionNumber) {
                case "1" -> moveToFind();
                case "2" -> moveToCreate();
                case "3" -> moveToRemove();
                case "4" -> moveToModify();
                case "5" -> moveToMovePerson();
                case "E" -> isAppRunning = false;
                default -> throw new IllegalArgumentException(RED + "Invalid action!" + RESET);
            }
        }
    }

    private void moveToMovePerson() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "MOVING A PERSON"
                + GREEN
                + " =======================|");
        System.out.println(CYAN
                + "Options to move an XML file:");
        System.out.println("1. By providing ID + First Name");
        System.out.println("2. By providing only ID");
        System.out.println("3. By providing all data except ID" + RESET);

        printLine();
        System.out.println("ENTER SOURCE TYPE (INTERNAL/EXTERNAL)");
        String typeFrom = getType();
        printLine();
        System.out.println("ENTER TARGET TYPE (INTERNAL/EXTERNAL)");
        String typeTo = getType();
        PersonType personTypeFrom = PersonType.valueOf(typeFrom.toUpperCase());
        PersonType personTypeTo = PersonType.valueOf(typeTo.toUpperCase());
        setDataWithId();
        personDbController.move(
                personId, firstName, lastName, mobile, email, pesel, personTypeFrom, personTypeTo);
    }

    private void moveToModify() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "MODIFY PERSON"
                + GREEN
                + " =========================|");
        System.out.println(CYAN
                + "Enter the ID of the record you want to modify. Fill in other fields with the "
                + "new data."
                + RESET);
        printLine();
        System.out.println("ENTER TYPE (INTERNAL/EXTERNAL)");
        String type = getType();
        setDataWithId();
        PersonType personType = PersonType.valueOf(type.toUpperCase());
        personDbController.modify(
                personId, firstName, lastName, mobile, email, pesel, personType);
    }

    private void moveToRemove() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + RED
                + "REMOVE PERSON"
                + GREEN
                + " =========================|");
        System.out.println(CYAN
                + "Options to remove an XML file:");
        System.out.println("1. By providing ID + First Name");
        System.out.println("2. By providing only ID");
        System.out.println("3. By providing all data except ID" + RESET);

        printLine();
        System.out.println("ENTER TYPE (INTERNAL/EXTERNAL)");
        String type = getType();
        setDataWithId();
        PersonType personType = PersonType.valueOf(type.toUpperCase());
        personDbController.remove(
                personId, firstName, lastName, mobile, email, pesel, personType);
    }

    private void moveToCreate() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "CREATE NEW PERSON"
                + GREEN
                + " ======================|");
        System.out.println(CYAN
                + "Fill in the fields you want to generate a new XML record."
                + RESET);

        printLine();
        System.out.println("ENTER TYPE (INTERNAL/EXTERNAL)");
        String type = getType();
        setRestData();
        PersonType personType = PersonType.valueOf(type.toUpperCase());
        personDbController.create(
                firstName, lastName, mobile, email, pesel, personType);
    }

    private void moveToFind() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "FIND PERSON"
                + GREEN
                + " ===========================|");
        System.out.println(CYAN + "Options to search XML file:");
        System.out.println("1. By providing ID + First Name");
        System.out.println("2. By providing only ID");
        System.out.println("3. By providing all data except ID" + RESET);

        printLine();
        System.out.println("ENTER TYPE (INTERNAL/EXTERNAL)");
        String type = getType();
        setDataWithId();
        PersonType personType = PersonType.valueOf(type.toUpperCase());
        Person person = personDbController.find(
                personId, firstName, lastName, mobile, email, pesel, personType);
        showPerson(person);
    }

    private void showPerson(Person person) {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "PERSON DETAILS"
                + GREEN
                + " ========================|"
                + RESET);
        System.out.printf(CYAN
                + "Person ID: "
                + RESET + "%s%n", person.getPersonId());
        System.out.printf(CYAN + "First Name: " + RESET + "%s%n", person.getFirstName());
        System.out.printf(CYAN + "Last Name: " + RESET + "%s%n", person.getLastName());
        System.out.printf(CYAN + "Phone Number: " + RESET + "%s%n", person.getMobile());
        System.out.printf(CYAN + "Email: " + RESET + "%s%n", person.getEmail());
        System.out.printf(CYAN + "Pesel: " + RESET + "%s%n", person.getPesel());
        printLine();
        System.out.println(PURPLE + "Press ENTER to return to menu" + RESET);
        scanner.nextLine();
    }

    private void setDataWithId() {
        printLine();
        System.out.println("ENTER ID");
        personId = scanner.nextLine();
        setRestData();
    }

    private void setRestData() {
        printLine();
        System.out.println("ENTER FIRST NAME");
        firstName = scanner.nextLine();
        printLine();
        System.out.println("ENTER LAST NAME");
        lastName = scanner.nextLine();
        printLine();
        System.out.println("ENTER PHONE NUMBER");
        mobile = scanner.nextLine();
        printLine();
        System.out.println("ENTER EMAIL");
        email = scanner.nextLine();
        printLine();
        System.out.println("ENTER PESEL");
        pesel = scanner.nextLine();
        printLine();
    }

    private String getType() {
        String type = scanner.nextLine();
        if (!type.equalsIgnoreCase(String.valueOf(PersonType.INTERNAL))
                || !type.equalsIgnoreCase(String.valueOf(PersonType.EXTERNAL))) {

            while (!type.equalsIgnoreCase(String.valueOf(PersonType.INTERNAL))
                    && !type.equalsIgnoreCase(String.valueOf(PersonType.EXTERNAL))) {
                System.out.println("WRITE (INTERNAL/EXTERNAL)");
                type = scanner.nextLine();
            }
        }
        return type;
    }

    private void printLine() {
        System.out.println(BLUE
                + "|------------------------------------------------------------|"
                + RESET);
    }

    private void clearTerminal() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
}

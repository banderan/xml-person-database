package com.company.ui;

import com.company.controller.PersonDbController;
import com.company.model.Person;
import com.company.model.PersonType;
import java.util.Scanner;

class PolishUi {
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

    PolishUi() {
        personDbController = new PersonDbController();
        scanner = new Scanner(System.in);
    }

    void menuInPl() {
        while (isAppRunning) {
            clearTerminal();
            System.out.println(BLUE
                    + "|========================================================================="
                    + "================|");
            System.out.println("|               "
                    + GREEN
                    + "Witaj w Person DB stworzonym przez Bartosza Wójcika"
                    + BLUE
                    + "                       |");
            System.out.println(
                    "|---------------------------------------------------------------------------"
                            + "--------------|");
            System.out.println("|"
                    + CYAN
                    + "Poniżej widzisz dostępne akcje w programie:"
                    + BLUE
                    + "                                              |");
            System.out.println(
                    "|----------------------------------------------------------------------------"
                            + "-------------|");
            System.out.println("|1. "
                    + YELLOW
                    + "Znajdź osobę w bazie danych"
                    + BLUE
                    + "                                                           |");
            System.out.println("|2. "
                    + YELLOW
                    + "Stwórz nowy rekord osoby"
                    + BLUE
                    + "                                                              |");
            System.out.println("|3. "
                    + YELLOW
                    + "Usuń osobę z bazy danych"
                    + BLUE
                    + "                                                              |");
            System.out.println("|4. "
                    + YELLOW
                    + "Zmodyfikuj dane osoby"
                    + BLUE
                    + "                                                                 |");
            System.out.println("|5. "
                    + YELLOW
                    + "Przenieś osobę między external a internal"
                    + BLUE
                    + "                                             |");
            System.out.println("|E. "
                    + RED
                    + "Zamknij program"
                    + BLUE
                    + "                                                                       |");
            System.out.println(
                    "|----------------------------------------------------------------------------"
                            + "-------------|");
            System.out.println("|"
                    + PURPLE
                    + "UWAGA! Dopóki nie zamkniesz programu, nie zobaczysz efektów niektórych "
                    + "akcji."
                    + BLUE
                    + "            |");
            System.out.println(
                    "|-------------------------------------------------------------------------"
                            + "----------------|"
                            + RESET);

            String actionNumber = scanner.nextLine().toUpperCase();

            switch (actionNumber) {
                case "1" -> moveToFind();
                case "2" -> moveToCreate();
                case "3" -> moveToRemove();
                case "4" -> moveToModify();
                case "5" -> moveToMovePerson();
                case "E" -> isAppRunning = false;
                default -> throw new IllegalArgumentException(RED + "Niepoprawna akcja!" + RESET);
            }
        }
    }

    private void moveToMovePerson() {
        clearTerminal();
        System.out.println(GREEN
                + "|==================== "
                + YELLOW
                + "PRZENOSZENIE OSOBY"
                + GREEN
                + " ====================|");
        System.out.println(CYAN
                + "Opcje przenoszenia pliku XML:");
        System.out.println("1. Podając ID + Imię");
        System.out.println("2. Podając tylko ID");
        System.out.println("3. Podając wszystkie dane poza ID" + RESET);

        printLine();
        System.out.println("PODAJ TYP Z KTOREGO CHCESZ PRZENIEŚĆ (INTERNAL/EXTERNAL)");
        String typeFrom = getType();
        printLine();
        System.out.println("PODAJ TYP DO KTOREGO CHCESZ PRZENIEŚĆ (INTERNAL/EXTERNAL)");
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
                + "MODYFIKACJA OSOBY"
                + GREEN
                + " =====================|");
        System.out.println(CYAN
                + "Podaj ID rekordu do modyfikacji. Pozostałe pola wypełnij zgodnie z tym, jak "
                + "chcesz by wyglądały dane tej osoby."
                + RESET);
        printLine();
        System.out.println("PODAJ TYP (INTERNAL/EXTERNAL)");
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
                + "USUWANIE OSOBY"
                + GREEN
                + " ========================|");
        System.out.println(CYAN
                + "Opcje usuwania pliku XML:");
        System.out.println("1. Podając ID + Imię");
        System.out.println("2. Podając tylko ID");
        System.out.println("3. Podając wszystkie dane poza ID" + RESET);
        printLine();
        System.out.println("PODAJ TYP (INTERNAL/EXTERNAL)");
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
                + "TWORZENIE NOWEJ OSOBY"
                + GREEN
                + " =================|");
        System.out.println(CYAN
                + "Wypełnij pola które chcesz, aby wygenerować nowy rekord XML."
                + RESET);
        printLine();
        System.out.println("PODAJ TYP (INTERNAL/EXTERNAL)");
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
                + "WYSZUKIWANIE OSOBY"
                + GREEN
                + " =====================|");
        System.out.println(CYAN
                + "Opcje wyszukiwania pliku XML:");
        System.out.println("1. Podając ID + Imię");
        System.out.println("2. Podając tylko ID");
        System.out.println("3. Podając wszystkie dane poza ID" + RESET);
        printLine();
        System.out.println("PODAJ TYP (INTERNAL/EXTERNAL)");
        String type = getType();
        setDataWithId();
        PersonType personType = PersonType.valueOf(type.toUpperCase());
        Person person = personDbController.find(
                personId, firstName, lastName, mobile, email, pesel, personType);
        showPerson(person);
    }

    private void showPerson(Person person) {
        clearTerminal();
        System.out.printf(GREEN
                + "|==================== "
                + YELLOW
                + "SZCZEGÓŁY OSOBY"
                + GREEN
                + " =======================|%n"
                + RESET);
        System.out.printf(CYAN + "Person ID: " + RESET + "%s%n", person.getPersonId());
        System.out.printf(CYAN + "Imię: " + RESET + "%s%n", person.getFirstName());
        System.out.printf(CYAN + "Nazwisko: " + RESET + "%s%n", person.getLastName());
        System.out.printf(CYAN + "Numer telefonu: " + RESET + "%s%n", person.getMobile());
        System.out.printf(CYAN + "Email: " + RESET + "%s%n", person.getEmail());
        System.out.printf(CYAN + "Pesel: " + RESET + "%s%n", person.getPesel());
        printLine();
        System.out.println(PURPLE + "Naciśnij ENTER, aby wrócić do menu" + RESET);
        scanner.nextLine();
    }

    private void setDataWithId() {
        printLine();
        System.out.println("PODAJ ID");
        personId = scanner.nextLine();
        setRestData();
    }

    private void setRestData() {
        printLine();
        System.out.println("PODAJ IMIĘ");
        firstName = scanner.nextLine();
        printLine();
        System.out.println("PODAJ NAZWISKO");
        lastName = scanner.nextLine();
        printLine();
        System.out.println("PODAJ NR TELEFONU");
        mobile = scanner.nextLine();
        printLine();
        System.out.println("PODAJ EMAIL");
        email = scanner.nextLine();
        printLine();
        System.out.println("PODAJ PESEL");
        pesel = scanner.nextLine();
        printLine();
    }

    private String getType() {
        String type = scanner.nextLine();
        if (!type.equalsIgnoreCase(String.valueOf(PersonType.INTERNAL))
                || !type.equalsIgnoreCase(String.valueOf(PersonType.EXTERNAL))) {

            while (!type.equalsIgnoreCase(String.valueOf(PersonType.INTERNAL))
                    && !type.equalsIgnoreCase(String.valueOf(PersonType.EXTERNAL))) {
                System.out.println("WPISZ (INTERNAL/EXTERNAL)");
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

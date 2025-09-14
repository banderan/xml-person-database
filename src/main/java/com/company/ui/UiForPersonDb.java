package com.company.ui;

import java.util.Scanner;

public class UiForPersonDb {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private final PolishUi polishUi;
    private final EnglishUi englishUi;
    private final Scanner scanner;

    public UiForPersonDb() {
        scanner = new Scanner(System.in);
        polishUi = new PolishUi();
        englishUi = new EnglishUi();
    }

    public void startApp() {
        String language = choseLanguage();
        switch (language) {
            case "A" -> {
                clearTerminal();//clear terminal
                polishUi.menuInPl();
            }
            case "B" -> {
                clearTerminal();//clear terminal
                englishUi.menuInAng();
            }
            default -> throw new IllegalArgumentException("Invalid language");
        }
    }

    private String choseLanguage() {

        System.out.println(BLUE
                + "|============================================================================="
                + "============|");
        System.out.println("|"
                + GREEN
                + "Choose your language / Wybierz język"
                + BLUE
                + "                                                     |");
        System.out.println("|"
                + CYAN
                + "Press the corresponding key on your keyboard"
                + BLUE
                + "                                             |");
        System.out.println(
                "|-------------------------------------------------------------------------------"
                        + "----------|");
        System.out.println("|A. "
                + YELLOW
                + "Polish (PL)"
                + BLUE
                + "                                                                           |");
        System.out.println("|B. "
                + YELLOW
                + "English (ENG)"
                + BLUE
                + "                                                                         |");
        System.out.println(
                "|==============================================================================="
                        + "==========|"
                        + RESET);

        return scanner.nextLine().toUpperCase();
    }

    private void clearTerminal() {
        //"pseudo-czyszczenie"
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

    }
}

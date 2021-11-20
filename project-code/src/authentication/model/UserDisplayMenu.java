package authentication.model;

import java.util.Scanner;

public class UserDisplayMenu {

    public void displayMainMenu() {

        System.out.println("User Menu");
        Scanner scanner = null;

        while (true) {
            System.out.println("1. Write SQL Query.");
            System.out.println("2. Execute SQL Query.");
            System.out.println("3. Generate ERD.");
            System.out.println("4. Generate Data Dictionary.");
            System.out.println("5. Import SQL Dump.");

            System.out.println("Choose an option:");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    //"write sql query";
                    break;
                case "2":
                    //"execute sql query";
                    break;
                case "3":
                    //"generate ERD";
                    break;
                case "4":
                    //"Generate DD";
                    break;
                case "5":
                    //"Import sql dump";
                    break;
                default:
                    break;
            }
        }
    }
}
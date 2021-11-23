package authentication.model;

import java.util.Scanner;

public class UserDisplayMenu {

    private final Scanner scanner;

    public UserDisplayMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayMainMenu() {

        System.out.println("User Menu");

        while (true) {
            System.out.println("1. Write SQL Query.");
            System.out.println("2. Execute SQL Query.");
            System.out.println("3. Generate ERD.");
            System.out.println("4. Generate Data Dictionary.");
            System.out.println("5. Import SQL Dump.");
            System.out.println("6. Logout.");

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
                case "6":
                    Session.getInstance().logout();
                    return;
                default:
                    break;
            }
        }
    }
}
package authentication.model;

import QueryImplementation.QueryExecutor;
import analytics.Analytics;

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
            System.out.println("2. Generate ERD.");
            System.out.println("3. Export SQL Dump.");
            System.out.println("4. Analytics");
            System.out.println("5. Logout.");

            System.out.println("Choose an option:");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    //"write sql query";
                    QueryExecutor qobj = new QueryExecutor(scanner);
                    qobj.queryExecute();
                    break;
                case "2":
                    //"generate ERD";
                    break;
                case "3":
                    //"Export sql dump";
                    break;
                case "4":
                    new Analytics(scanner).performAnalytics();
                    break;
                case "5":
                    Session.getInstance().logout();
                    return;
                default:
                    break;
            }
        }
    }
}
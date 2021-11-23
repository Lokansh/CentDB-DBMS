import authentication.login.UserLogin;
import authentication.model.Session;
import authentication.model.UserDisplayMenu;
import authentication.registration.UserRegistration;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Session userSession = Session.getInstance();
//        accept user input
//        String userArgument = null;
//        userArgument = scanner.nextLine();
//        userArgument = userArgument.trim();
//        System.out.println(userArgument);
//
//
//        // "create database" to be replaced with PATTERN MATCHER : Lokansh working on the smae
//
//        if (userArgument.contains("create database")) {
//            String dbname = userArgument.replace("create database", "");
//            dbname = dbname.trim();
//            CentDB dbobj = new CentDB();
//            String db_filename = "datafiles/" + dbname + ".txt";
//            String writebackStr = dbname + ";";
//            String writebackType = "create database";
//            dbobj.writeback(db_filename, writebackStr, writebackType);
//        }

        while (true) {
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Quit");

            System.out.println("Choose an option:");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    new UserRegistration(scanner).register();
                    break;
                case "2":
                    new UserLogin(scanner).login();
                    if (userSession.isLoggedIn()) {
                        UserDisplayMenu userDisplayMenu = new UserDisplayMenu(scanner);
                        userDisplayMenu.displayMainMenu();
                    }
                    break;
                default:
                    userSession.logout();
                    System.exit(0);
            }

        }
    }
}

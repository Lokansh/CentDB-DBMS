import authentication.login.UserLogin;
import authentication.model.Session;
import authentication.model.UserDisplayMenu;
import authentication.registration.UserRegistration;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Session userSession = Session.getInstance();

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
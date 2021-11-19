import authentication.registration.UserRegistration;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
//        accept user input
        String userArgument = null;
        Scanner s = new Scanner(System.in);
        userArgument = s.nextLine();
        userArgument = userArgument.trim();
        System.out.println(userArgument);

        // "create database" to be replaced with PATTERN MATCHER : Lokansh working on the smae

        if (userArgument.contains("create database")) {
            String dbname = userArgument.replace("create database", "");
            dbname = dbname.trim();
            CentDB dbobj = new CentDB();
            String db_filename = "datafiles/" + dbname + ".txt";
            String writebackStr = dbname + ";";
            String writebackType = "create database";
            dbobj.writeback(db_filename, writebackStr, writebackType);
        }


        System.out.println("1. User Registration");
        System.out.println("2. User Login");

        String choice = s.nextLine();

        switch (choice) {
            case "1":
                new UserRegistration().register();
                break;
            case "2":
                // login
                break;
            default:
                System.exit(0);
        }


    }
}

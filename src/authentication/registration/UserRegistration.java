package authentication.registration;

import authentication.SecurityQuestion;
import authentication.utils.HashingService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserRegistration {

    private final static String USER_PROFILE_FILE_PATH = "User_Profile.txt";
    private final Scanner scanner;

    public UserRegistration(Scanner scanner) {
        this.scanner = scanner;
    }

    private void registerUser(String name, String username, String password, List<String> securityAnswers) {

        String hashedUsername = HashingService.hashText(username);
        String hashedPassword = HashingService.hashText(password);

        String delimiter = "|";

        if (checkUserExists(hashedUsername)) {
            System.out.println("Username taken");
            return;
        }

        try (FileWriter fileWriter = new FileWriter("User_Profile.txt", true)) {
            StringBuilder builder = new StringBuilder();

            builder.append(name)
                    .append(delimiter);

            builder.append(hashedUsername)
                    .append(delimiter)
                    .append(hashedPassword)
                    .append(delimiter);

            for (String answer : securityAnswers) {
                builder.append(answer)
                        .append(delimiter);
            }
            builder.setLength(builder.length() - 1);
            fileWriter.append(builder.toString());
            fileWriter.append(System.lineSeparator());
            System.out.println("User registered successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkUserExists(String username) {
        File userProfile = new File(USER_PROFILE_FILE_PATH);

        if (!userProfile.exists()) {
            return false;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(userProfile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split("\\|");
                String uname = userDetails[1];

                if (username.equals(uname)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void register() {

        System.out.println("Enter your name");
        String name = scanner.nextLine();

        System.out.println("Enter username");
        String username = scanner.nextLine();

        System.out.println("Enter password");
        String password = scanner.nextLine();

        SecurityQuestion securityQuestion = SecurityQuestion.getInstance();
        Map<Integer, String> securityQuest = securityQuestion.getSecurityQuestion();
        ArrayList<String> securityAnswers = new ArrayList<>();

        //Referred URL : https://www.baeldung.com/java-map-entry
        //Printing all our security questions(keys) and answers(value) in the file
        for (Map.Entry<Integer, String> entryAnswer : securityQuest.entrySet()) {
            System.out.println(entryAnswer.getValue());
            securityAnswers.add(scanner.nextLine());
        }
        registerUser(name, username, password, securityAnswers);
    }
}

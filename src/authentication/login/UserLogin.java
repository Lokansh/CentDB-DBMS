package authentication.login;

import authentication.SecurityQuestion;
import authentication.model.Session;
import authentication.model.User;
import authentication.utils.HashingService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLogin {

    public static final String USER_PROFILE_FILE_PATH = "User_Profile.txt";
    private final Scanner scanner;

    public UserLogin(Scanner scanner) {
        this.scanner = scanner;
    }

    public void login() {

        System.out.println("Enter username");
        String username = scanner.nextLine();

        System.out.println("Enter password");
        String password = scanner.nextLine();

        SecurityQuestion securityQuestion = SecurityQuestion.getInstance();

        int questionIndex = securityQuestion.generateRandomQuestion();

        String question = securityQuestion.getSecurityQuestion().get(questionIndex);

        System.out.println(question);
        String securityAnswer = scanner.nextLine();

        login(username, password, questionIndex, securityAnswer);
    }

    private void login(String username, String password, int questionIndex, String securityAnswer) {
        String hashedUsername = HashingService.hashText(username);
        String hashedPassword = HashingService.hashText(password);

        File userProfile = new File(USER_PROFILE_FILE_PATH);

        if (!userProfile.exists()) {
            System.out.println("User does not exists. Please register first.");
            return;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(USER_PROFILE_FILE_PATH))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split("\\|");

                String name = userDetails[0];
                String uname = userDetails[1];
                String pword = userDetails[2];

                String sans = userDetails[3 + questionIndex];

                if (hashedUsername.equals(uname)) {

                    if (!hashedPassword.equals(pword)) {
                        System.out.println("Invalid credentials");
                        return;
                    }

                    if (!securityAnswer.equals(sans)) {
                        System.out.println("Invalid credentials");
                        return;
                    }

                    List<String> securityAnswers = new ArrayList<>();
                    for (int i = 0; i < SecurityQuestion.getInstance().getSecurityQuestion().size(); i++) {
                        securityAnswers.add(userDetails[3 + i]);
                    }

                    User user = new User(name, hashedUsername, hashedPassword, securityAnswers);
                    Session.getInstance().setUser(user);
                    System.out.println("Login successful");
                    return;
                }

            }

            System.out.println("User does not exist");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

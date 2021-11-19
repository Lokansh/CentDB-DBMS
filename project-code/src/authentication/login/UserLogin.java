package authentication.login;

import authentication.SecurityQuestion;
import authentication.model.Session;
import authentication.model.User;
import authentication.utils.HashingService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLogin {

    public UserLogin() {
    }

    public void login() {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter username");
        String username = in.nextLine();

        System.out.println("Enter password");
        String password = in.nextLine();

        SecurityQuestion securityQuestion = SecurityQuestion.getInstance();

        int questionIndex = securityQuestion.generateRandomQuestion();

        String question = securityQuestion.getSecurityQuestion().get(questionIndex);

        System.out.println(question);
        String securityAnswer = in.nextLine();

        login(username, password, questionIndex, securityAnswer);
    }

    private void login(String username, String password, int questionIndex, String securityAnswer) {
        String hashedUsername = HashingService.hashText(username);
        String hashedPassword = HashingService.hashText(password);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("User_Profile.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split(" ");

                String uname = userDetails[0];
                String pword = userDetails[1];

                String sans = userDetails[2 + questionIndex];

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
                        securityAnswers.add(userDetails[2 + i]);
                    }

                    User user = new User(hashedUsername, hashedPassword, securityAnswers);
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

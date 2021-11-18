package authentication.registration;

import authentication.SecurityQuestion;
import authentication.utils.HashingService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRegistration {

    private void registerUser(String username, String password, List<String> securityAnswers) {

        String hashedUsername = HashingService.hashText(username);
        String hashedPassword = HashingService.hashText(password);

        try (FileWriter fileWriter = new FileWriter("User_Profile.txt", true)) {
            StringBuilder builder = new StringBuilder();

            builder.append(hashedUsername + " ")
                    .append(hashedPassword + " ");

            for (String answer : securityAnswers) {
                builder.append(answer + " ");
            }

            fileWriter.append(builder.toString());
            fileWriter.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        System.out.println("Enter password");
        String password = in.nextLine();

        List<String> answers = new ArrayList<>();
        for (String question : SecurityQuestion.securityQuestions) {
            System.out.println(question);
            String answer = in.nextLine();
            answers.add(answer);
        }

        registerUser(username, password, answers);
    }

}

package authentication.registration;

import authentication.SecurityQuestion;
import authentication.utils.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserRegistration {
    FileWriter fileWriter = new FileWriter("User_Profile.txt", true);

    public UserRegistration() throws IOException {
    }

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

    public void register() throws IOException {

        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        System.out.println("Enter password");
        String password = in.nextLine();

        SecurityQuestion securityQuestion = SecurityQuestion.getInstance();
        Map<Integer, String> securityQuest = securityQuestion.getSecurityQuestion();
        ArrayList<String> securityAnswers = new ArrayList<>();
        for (Map.Entry<Integer, String> entryAnswer : securityQuest.entrySet()) {
            fileWriter.append((entryAnswer.getValue()));
            securityAnswers.add(in.nextLine());
        }

    }
}

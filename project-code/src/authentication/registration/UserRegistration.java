package authentication.registration;

import authentication.SecurityQuestion;
import authentication.utils.HashingService;

import java.io.BufferedReader;
import java.io.FileReader;
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

        if (checkUserExists(hashedUsername)) {
            System.out.println("Username taken");
            return;
        }

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

    private boolean checkUserExists(String username) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("User_Profile.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] userDetails = line.split(" ");
                String uname = userDetails[0];

                if (username.equals(uname)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

        //Referred URL : https://www.baeldung.com/java-map-entry
        //Printing all our security questions(keys) and answers(value) in the file
        for (Map.Entry<Integer, String> entryAnswer : securityQuest.entrySet()) {
//            fileWriter.append((entryAnswer.getValue()));
            System.out.println(entryAnswer.getValue());
            securityAnswers.add(in.nextLine());
        }
        registerUser(username, password, securityAnswers);
    }
}

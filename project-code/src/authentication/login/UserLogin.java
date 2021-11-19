package authentication.login;

import authentication.SecurityQuestion;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserLogin {
    FileWriter fileWriter = new FileWriter("User_Profile.txt", true);

    public UserLogin() throws IOException {
    }

    public void login() throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter username");
        String username = in.nextLine();

        System.out.println("Enter password");
        String password = in.nextLine();


        SecurityQuestion securityQuestion = SecurityQuestion.getInstance();
        String randomSecurityQuestion = securityQuestion.generateRandomQuestion();
        int questionIndex = securityQuestion.questionIndex(randomSecurityQuestion);

        fileWriter.append(randomSecurityQuestion);
        String securityAnswer = in.nextLine();

    }

}

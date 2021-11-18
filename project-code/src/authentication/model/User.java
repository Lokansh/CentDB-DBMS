package authentication.model;

import java.util.List;

public class User {
    private final String username;
    private final String password;

    public User(String username, String password, List<String> securityQuestions) {
        this.username = username;
        this.password = password;
        this.securityQuestions = securityQuestions;
    }

    private final List<String> securityQuestions;
}
